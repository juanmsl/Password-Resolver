package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientConection extends Thread {
	private MainServer server;
	private ServerSocket serverSocket;
	private ObjectInputStream objectInput;
	private ObjectOutputStream objectOutput;
	private int port;
	
	public ClientConection(MainServer server, int port) {
		try {
			this.server = server;
			this.port = port;
			this.objectInput = null;
			this.objectOutput = null;
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
				this.objectInput = new ObjectInputStream(socket.getInputStream());
				this.objectOutput = new ObjectOutputStream(socket.getOutputStream());
				while (true) {
					Object message = this.objectInput.readObject();
					this.server.resolve(message);
				}
			}
			catch (IOException | ClassNotFoundException event) {
				if (socket != null) {
					try {
						socket.close();
					}
					catch (IOException event1) {
						System.out.println("[Client thread]: Error: [" + event1.getMessage() + "]");
					}
				}
				if (event instanceof ClassNotFoundException) {
					System.out.println("[Client thread]: Error: [" + event.getMessage() + "]");
				} else if (event instanceof IOException) {
					System.out.println("[Client thread]: The client has been disconected");
				}
				this.server.initializeClientConection();
			}
		}
	}
	
	public void request(Object object) {
		try {
			this.objectOutput.writeObject(object);
		}
		catch (IOException event) {
			System.out.println("[Client thread]: Error: [" + event.getMessage() + "]");
		}
	}
}
