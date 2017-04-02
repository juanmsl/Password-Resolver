package controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DiscoverThread extends Thread {
	private Server server;
	private ServerSocket serverSocket;
	
	public DiscoverThread(Server server) {
		try {
			this.server = server;
			this.serverSocket = new ServerSocket(Server.PORT);
			System.out.println("[External machines thread]: Server initialized in port " + this.serverSocket.getLocalPort() + " for external machines");
		}
		catch (IOException event) {
			System.out.println("[External machines thread]: Port " + Server.PORT + " already in use");
			this.serverSocket = null;
		}
	}
	
	@Override
	public void run() {
		if (this.serverSocket != null) {
			try {
				while (true) {
					Socket socket = this.serverSocket.accept();
					System.out.println("[External machines thread]: Machine connected");
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					int port = -1;
					for (int i = 1; i <= Server.MACHINES; i++) {
						if (!this.server.containsExternalMachine(i + Server.PORT)) {
							port = i + Server.PORT;
							this.server.addExternalMachine(port);
							break;
						}
					}
					System.out.println("[External machines thread]: Port for machine: " + port);
					out.writeInt(port);
					socket.close();
				}
			}
			catch (IOException event) {
				System.out.println("[External machines thread]: Error: [" + event.getMessage() + "]");
			}
		}
	}
}
