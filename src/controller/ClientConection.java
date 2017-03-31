package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientConection extends Thread {
	private Server server;
	private ServerSocket serverSocket;
	private DataInputStream in;
	private DataOutputStream out;
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
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			while (true) {
				String hashToFind = this.in.readUTF();
				int characters = this.in.readInt();
				String dictionary = this.in.readUTF();
				System.out.println("Recived hash to resolve: " + hashToFind);
				this.server.resolve(hashToFind, characters, dictionary);
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
