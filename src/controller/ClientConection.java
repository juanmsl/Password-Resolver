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
		try {
			System.out.println("Waiting for the client that will use the port " + this.port + "...");
			Socket socket = this.serverSocket.accept();
			System.out.println("...A client was conected on port " + this.port);
			String linea = "";
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			while (!linea.equals("-1")) {
				linea = this.in.readUTF();
				int number = Integer.parseInt(linea);
				this.server.contar(number);
				System.out.println("Recived: " + number);
			}
			socket.close();
		}
		catch (IOException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
	}
}
