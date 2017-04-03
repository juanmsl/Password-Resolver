package controller;

import java.net.InetAddress;

public class Machine {
	public static int MACHINE_CONSECUTIVE = 0;
	
	private int ID;
	private int port;
	private MainServer mainServer;
	private InetAddress host;
	private MachineConection machineConection;
	
	public Machine(int port, InetAddress host, MainServer server) {
		this.ID = ++Machine.MACHINE_CONSECUTIVE;
		this.port = port;
		this.mainServer = server;
		this.host = host;
		this.machineConection = null;
	}
	
	public int getID() {
		return this.ID;
	}
	
	public void serverToClient(Object object) {
		this.mainServer.request(object);
	}
	
	public void resolve(String hash, int characters, String dictionary, char first, char last) {
		this.machineConection = new MachineConection(this, hash, characters, dictionary, first, last);
		this.machineConection.start();
	}
	
	public void remove() {
		this.mainServer.removeMachine(this.port);
	}
	
	public void stopOtherMachines() {
		this.mainServer.stopOtherMachines();
	}
	
	public void reset() {
		this.machineConection = null;
	}
	
	public void stop() {
		if (this.machineConection != null) {
			this.machineConection.interrupt();
		}
	}
	
	public int getPort() {
		return this.port;
	}
	
	public InetAddress getHost() {
		return this.host;
	}
	
	@Override
	public String toString() {
		return String.format("Machine %s: [Port: %s]", this.ID, this.port);
	}
}
