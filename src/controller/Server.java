package controller;

import java.util.ArrayList;
import java.util.List;

public class Server {
	public static int PORT = 3200;
	public static int MACHINES = 2;
	
	private List<Machine> machines;
	private ClientConection mainThread;
	
	private void loadConfiguration() {
		for (int port = 1; port <= Server.MACHINES; port++) {
			Machine machine = new Machine(port + Server.PORT);
			this.machines.add(machine);
			machine.initializeMachine();
		}
	}
	
	public Server() {
		this.machines = new ArrayList<>();
		this.loadConfiguration();
		this.mainThread = new ClientConection(this, Server.PORT);
	}
	
	public void contar(int n) {
		for (Machine m : this.machines) {
			m.contar(n);
		}
	}
	
	public void initializeServer() {
		this.mainThread.start();
	}
	
	public static void main(String[] args) {
		Server control = new Server();
		control.initializeServer();
	}
}
