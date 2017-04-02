package controller;

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
	private ObjectOutputStream out;
	private int port;
	
	public ClientConection(Server server, int port) {
		try {
			this.server = server;
			this.port = port;
			this.in = null;
			this.out = null;
			System.out.println("Enable conection for client on port " + port + "...");
			this.serverSocket = new ServerSocket(port);
		}
		catch (IOException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
	}
	
	@Override
	public void run() {
		Socket socket = null;
		try {
			System.out.println("Waiting for the client that will use the port " + this.port + "...");
			socket = this.serverSocket.accept();
			System.out.println("...A client was conected on port " + this.port);
			this.in = new ObjectInputStream(socket.getInputStream());
			this.out = new ObjectOutputStream(socket.getOutputStream());
			while (true) {
				DecriptMessage message = (DecriptMessage) this.in.readObject();
				this.server.resolve(message);
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
			System.out.println("The client has been disconected");
			this.server.initializeClientConection();
		}
		catch (ClassNotFoundException event) {
			if (socket != null) {
				try {
					socket.close();
				}
				catch (IOException event1) {
					System.out.println("Error: [" + event1.getMessage() + "]");
				}
			}
			System.out.println("Error: [" + event.getMessage() + "]");
			this.server.initializeClientConection();
		}
	}
	
	public void request(String word) {
		try {
			this.out.writeUTF(word);
		}
		catch (IOException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
	}
}
