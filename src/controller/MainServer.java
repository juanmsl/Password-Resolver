package controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class MainServer {
	public static final int CLIENT_PORT = 3000;
	public static final int PORT = 3001;
	public static int MACHINES = 3;
	
	private Map<Integer, Machine> machines;
	private DiscoverThread discoverThread;
	private ClientConection clientThread;
	
	public MainServer() {
		this.machines = new HashMap<>();
		this.clientThread = new ClientConection(this, MainServer.CLIENT_PORT);
		this.discoverThread = new DiscoverThread(this, MainServer.PORT);
	}
	
	public void initializeServer() {
		this.clientThread.start();
		this.discoverThread.start();
	}
	
	public void initializeClientConection() {
		this.clientThread.run();
	}
	
	public void addExternalMachine(int port, InetAddress host) {
		Machine machine = new Machine(port, host, this);
		this.machines.put(port, machine);
	}
	
	public boolean containsExternalMachine(int port) {
		return this.machines.containsKey(port);
	}
	
	public static void main(String[] args) {
		MainServer control = new MainServer();
		control.initializeServer();
	}
	
	public void resolve(Object object) {
		if (this.machines.size() == 0) {
			this.request(-1);
			System.out.println("[Server]: No available servers on the moment");
			this.request("No available servers on the moment");
			return;
		}
		
		this.request(0);
		System.out.println("[Server]: Decoding the hash, please wait");
		this.request("Decoding the hash, please wait");
		
		try {
			Class<? extends Object> message = object.getClass();
			
			Method getHash = message.getDeclaredMethod("getHash", null);
			Method getCharacters = message.getDeclaredMethod("getCharacters", null);
			Method getDictionary = message.getDeclaredMethod("getDictionary", null);
			
			String hash = (String) getHash.invoke(object, null);
			int characters = (int) getCharacters.invoke(object, null);
			String dictionary = (String) getDictionary.invoke(object, null);
			
			int n = dictionary.length() / this.machines.size();
			int i = 0;
			for (int port : this.machines.keySet()) {
				char first = dictionary.charAt(i);
				i += n + 1;
				i = ((i >= dictionary.length()) ? dictionary.length() - 1 : i);
				char last = dictionary.charAt(i);
				Machine m = this.machines.get(port);
				m.resolve(hash, characters, dictionary, first, last);
			}
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException event) {
			System.out.println("[Server]: Error [" + event.getMessage() + "]");
		}
	}
	
	public void request(Object object) {
		this.clientThread.request(object);
	}
	
	public void removeMachine(int port) {
		Machine m = this.machines.remove(port);
		System.out.println("[Server]: The machine " + m.getID() + " was removed");
	}
	
	public void stopOtherMachines() {
		for (int port : this.machines.keySet()) {
			Machine m = this.machines.get(port);
			m.stop();
		}
	}
}
