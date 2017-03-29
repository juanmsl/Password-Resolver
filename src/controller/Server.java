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
	
	public void contar(int n) {
		for (int port : this.machines.keySet()) {
			Machine m = this.machines.get(port);
			m.sendInt(n);
		}
	}
	
	public void initializeServer() {
		this.clientThread.start();
		this.discoverThread.start();
	}
	
	public void addExternalMachine(int port) {
		Machine machine = new Machine(port);
		this.machines.put(port, machine);
	}
	
	public boolean containsExternalMachine(int port) {
		return this.machines.containsKey(port);
	}
	
	public static void main(String[] args) {
		Server control = new Server();
		control.initializeServer();
	}
}
