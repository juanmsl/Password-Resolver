package controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import client.DecriptMessage;

public class ClientConection extends Thread {
	private Server server;
	private ServerSocket serverSocket;
	private ObjectInputStream in;
	private ObjectOutputStream objectOutput;
	private int port;
	private DataOutputStream dataOutput;
	
	public ClientConection(Server server, int port) {
		try {
			this.server = server;
			this.port = port;
			this.in = null;
			this.objectOutput = null;
			this.dataOutput = null;
			System.out.println("[Client thread]: Enable conection for client on port " + port + "...");
			this.serverSocket = new ServerSocket(port);
		}
		catch (IOException event) {
			System.out.println("[Client thread]: Port " + port + " already in use");
			this.serverSocket = null;
		}
	}
	
	@Override
	public void run() {
		if (this.serverSocket != null) {
			Socket socket = null;
			try {
				System.out.println("[Client thread]: Waiting for the client that will use the port " + this.port + "...");
				socket = this.serverSocket.accept();
				System.out.println("[Client thread]: Client was conected on port " + this.port);
				this.in = new ObjectInputStream(socket.getInputStream());
				this.objectOutput = new ObjectOutputStream(socket.getOutputStream());
				this.dataOutput = new DataOutputStream(socket.getOutputStream());
				while (true) {
					Object message = this.in.readObject();
					this.server.resolve(message);
				}
			}
			catch (IOException event) {
				if (socket != null) {
					try {
						socket.close();
					}
					catch (IOException event1) {
						System.out.println("[Client thread]: Error: [" + event1.getMessage() + "]");
					}
				}
				System.out.println("[Client thread]: The client has been disconected");
				this.server.initializeClientConection();
			}
			catch (ClassNotFoundException event) {
				if (socket != null) {
					try {
						socket.close();
					}
					catch (IOException event1) {
						System.out.println("[Client thread]: Error: [" + event1.getMessage() + "]");
					}
				}
				System.out.println("[Client thread]: Error: [" + event.getMessage() + "]");
				this.server.initializeClientConection();
			}
		}
	}
	
	public void requestUTF(String word) {
		try {
			this.dataOutput.writeUTF(word);
		}
		catch (IOException event) {
			System.out.println("[Client thread]: Error: [" + event.getMessage() + "]");
		}
	}
	
	public void requestInt(int n) {
		try {
			this.dataOutput.writeInt(n);
		}
		catch (IOException event) {
			System.out.println("[Client thread]: Error: [" + event.getMessage() + "]");
		}
	}
	
	public void requestObject(Object object) {
		try {
			this.objectOutput.writeObject(object);
		}
		catch (IOException event) {
			System.out.println("[Client thread]: Error: [" + event.getMessage() + "]");
		}
	}
}
