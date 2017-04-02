package controller;

import java.util.HashMap;
import java.util.Map;

import client.DecriptMessage;

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
	
	public void resolve(DecriptMessage message) {
		if (this.machines.size() == 0) {
			this.requestInt(-1);
			System.out.println("[Server]: No available servers on the moment");
			this.requestUTF("No available servers on the moment");
			return;
		}
		this.requestInt(0);
		System.out.println("[Server]: Decoding the hash, please wait");
		this.requestUTF("Decoding the hash, please wait");
		int n = message.getDictionary().length() / this.machines.size();
		int i = 0;
		for (int port : this.machines.keySet()) {
			char first = message.getDictionary().charAt(i);
			i += n;
			i = ((i >= message.getDictionary().length()) ? message.getDictionary().length() - 1 : i);
			char last = message.getDictionary().charAt(i);
			System.out.println(first + " " + last);
			Machine m = this.machines.get(port);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					m.resolve(message, first, last);
				}
			}).start();
		}
	}
	
	public void requestUTF(String word) {
		this.clientThread.requestUTF(word);
	}
	
	public void requestInt(int n) {
		this.clientThread.requestInt(n);
	}
	
	public void requestObject(Object object) {
		this.clientThread.requestObject(object);
	}
	
	public void removeMachine(int port) {
		this.machines.remove(port);
	}
}
