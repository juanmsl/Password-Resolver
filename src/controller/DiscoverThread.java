package controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class DiscoverThread extends Thread {
	private MainServer mainServer;
	private ServerSocket serverSocket;
	private int port;
	
	public DiscoverThread(MainServer server, int port) {
		try {
			this.port = port;
			this.mainServer = server;
			this.serverSocket = new ServerSocket(port);
			System.out.println("[Discover thread]: Server initialized in port " + port + " for external machines");
		}
		catch (IOException event) {
			System.out.println("[Discover thread]: Port " + port + " already in use");
			this.serverSocket = null;
		}
	}
	
	@Override
	public void run() {
		if (this.serverSocket != null) {
			try {
				while (true) {
					Socket socket = this.serverSocket.accept();
					System.out.println("[Discover thread]: Machine connected");
					InetAddress host = socket.getLocalAddress();
					ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
					int port = -1;
					for (int i = 1; i <= MainServer.MACHINES; i++) {
						if (!this.mainServer.containsExternalMachine(i + this.port)) {
							port = i + this.port;
							this.mainServer.addExternalMachine(port, host);
							break;
						}
					}
					System.out.println("[Discover thread]: Machine configured [" + host.getHostAddress() + " : " + port + "]");
					output.writeObject(port);
					socket.close();
				}
			}
			catch (IOException event) {
				System.out.println("[Discover thread]: Error: [" + event.getMessage() + "]");
			}
		}
	}
}
