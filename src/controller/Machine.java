package controller;

import client.DecriptMessage;

public class Machine {
	public static int MACHINE_CONSECUTIVE = 0;
	
	private int id;
	private int port;
	private MachineConection machineThread;
	private Server server;
	
	public Machine(int port, Server server) {
		this.id = ++Machine.MACHINE_CONSECUTIVE;
		this.port = port;
		this.server = server;
		this.machineThread = new MachineConection(this.port, this);
		this.machineThread.start();
	}
	
	public int getId() {
		return this.id;
	}
	
	public void serverToClient(String word) {
		this.server.requestToClient(word);
	}
	
	@Override
	public String toString() {
		return String.format("Machine %s: [Port: %s]", this.id, this.port);
	}
	
	public void resolve(DecriptMessage message, char first, char last) {
		this.machineThread.resolve(message, first, last);
	}
	
	public void remove() {
		this.server.removeMachine(this.port);
	}
}
