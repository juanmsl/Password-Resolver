<<<<<<< HEAD
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
			System.out.println("Server initialized in port " + this.serverSocket.getLocalPort() + " for external machines");
		}
		catch (IOException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				Socket socket = this.serverSocket.accept();
				System.out.println("Machine connected");
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				int port = -1;
				for (int i = 1; i <= Server.MACHINES; i++) {
					System.out.println(i + Server.PORT);
					if (!this.server.containsExternalMachine(i + Server.PORT)) {
						port = i + Server.PORT;
						this.server.addExternalMachine(port);
						break;
					}
				}
				System.out.println("Port for machine: " + port);
				out.writeInt(port);
				socket.close();
			}
		}
		catch (IOException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
		super.run();
	}
}
=======
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
			System.out.println("Server initialized in port " + this.serverSocket.getLocalPort() + " for external machines");
		}
		catch (IOException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				Socket socket = this.serverSocket.accept();
				System.out.println("Machine connected");
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				int port = -1;
				for (int i = 1; i <= Server.MACHINES; i++) {
					System.out.println(i + Server.PORT);
					if (!this.server.containsExternalMachine(i + Server.PORT)) {
						port = i + Server.PORT;
						this.server.addExternalMachine(port);
						break;
					}
				}
				System.out.println("Port for machine: " + port);
				out.writeInt(port);
				socket.close();
			}
		}
		catch (IOException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
		super.run();
	}
}
>>>>>>> origin/master
