package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MachineConection extends Thread {
	private ServerSocket serverSocket;
	private DataInputStream in;
	private DataOutputStream out;
	private int port;
	
	public MachineConection(int port) {
		try {
			this.in = null;
			this.out = null;
			this.port = port;
			System.out.println("Enable conection on port " + port + "...");
			this.serverSocket = new ServerSocket(port);
			System.out.println("Conection enabled on port " + port);
		}
		catch (IOException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Waiting for the machine that will use the port " + this.port + "...");
			Socket socket = this.serverSocket.accept();
			System.out.println("...A machine was conected on port " + this.port);
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
		}
		catch (IOException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
			event.printStackTrace();
		}
	}
	
	public void writeInt(int n) {
		try {
			this.out.writeInt(n);
		}
		catch (IOException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
	}
}
