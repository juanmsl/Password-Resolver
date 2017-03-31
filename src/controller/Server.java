package controller;

import java.util.HashMap;
import java.util.Map;

public class Server {
	public static final int CLIENT_PORT = 3000;
	public static final int PORT = 3001;
	public static int MACHINES = 3;
	
	private Map<Integer, Machine> machines;
	private DiscoverThread discoverThread;
	private ClientConection clientThread;
	
	public Server() {
		this.machines = new HashMap<>();
		this.clientThread = new ClientConection(this, Server.CLIENT_PORT);
		this.discoverThread = new DiscoverThread(this);
	}
	
	public void initializeServer() {
		this.clientThread.start();
		this.discoverThread.start();
	}
	
	public void initializeClientConection() {
		this.clientThread.run();
	}
	
	public void addExternalMachine(int port) {
		Machine machine = new Machine(port, this);
		this.machines.put(port, machine);
	}
	
	public boolean containsExternalMachine(int port) {
		return this.machines.containsKey(port);
	}
	
	public static void main(String[] args) {
		Server control = new Server();
		control.initializeServer();
	}
	
	public void resolve(String hashToFind, int characters, String dictionary) {
		if (this.machines.size() == 0) {
			this.requestToClient("No available servers on the moment");
			return;
		}
		int n = dictionary.length() / this.machines.size();
		int i = 0;
		for (int port : this.machines.keySet()) {
			char first = dictionary.charAt(i);
			i += n;
			i = ((i >= dictionary.length()) ? dictionary.length() - 1 : i);
			char last = dictionary.charAt(i);
			System.out.println(first + " " + last);
			Machine m = this.machines.get(port);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					m.resolve(hashToFind, characters, dictionary, first, last);
				}
			}).start();
			
		}
	}
	
	public void requestToClient(String word) {
		this.clientThread.request(word);
	}
	
	public void removeMachine(int port) {
		this.machines.remove(port);
	}
}
