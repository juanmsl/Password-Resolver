package controller;

public class Machine {
	public static int MACHINE_CONSECUTIVE = 0;
	
	private int id;
	private int port;
	private MachineConection machineThread;
	
	public Machine(int port) {
		this.id = ++Machine.MACHINE_CONSECUTIVE;
		this.port = port;
		this.machineThread = new MachineConection(this.port);
	}
	
	public void initializeMachine() {
		this.machineThread.start();
	}
	
	@Override
	public String toString() {
		return String.format("Machine %s: [Port: %s]", this.id, this.port);
	}
	
	public void contar(int n) {
		this.machineThread.contar(n);
	}
}
