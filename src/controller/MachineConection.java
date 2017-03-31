<<<<<<< HEAD
package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MachineConection extends Thread {
	private Machine machine;
	private ServerSocket serverSocket;
	private DataInputStream in;
	private DataOutputStream out;
	private int port;
	
	public MachineConection(int port, Machine machine) {
		try {
			this.machine = machine;
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
		Socket socket = null;
		try {
			System.out.println("Waiting for the machine that will use the port " + this.port + "...");
			socket = this.serverSocket.accept();
			System.out.println("...A machine was conected on port " + this.port);
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			while (true) {
				String word = this.in.readUTF();
				System.out.println("Machine " + this.machine.getId() + " - " + word);
				if (word != null) {
					this.machine.serverToClient(word);
				}
			}
		}
		catch (IOException event) {
			if (socket != null) {
				try {
					socket.close();
				}
				catch (IOException event1) {
					System.out.println("Error: [" + event1.getMessage() + "]");
				}
			}
			System.out.println("The machine " + this.machine.getId() + " has been disconected");
			this.machine.remove();
		}
	}
	
	public void resolve(String hashToFind, int characters, String dictionary, char first, char last) {
		try {
			this.out.writeUTF(hashToFind);
			this.out.writeInt(characters);
			this.out.writeUTF(dictionary);
			this.out.writeChar(first);
			this.out.writeChar(last);
		}
		catch (IOException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
	}
}
=======
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
>>>>>>> origin/master
