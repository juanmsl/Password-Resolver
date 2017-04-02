package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import client.DecriptMessage;

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
			System.out.println("[Machine " + machine.getId() + "]: Enable conection on port " + port + "...");
			this.serverSocket = new ServerSocket(port);
			System.out.println("[Machine " + machine.getId() + "]: Conection enabled on port " + port);
		}
		catch (IOException event) {
			System.out.println("[Machine " + machine.getId() + "]: Error: [" + event.getMessage() + "]");
			this.serverSocket = null;
		}
	}
	
	@Override
	public void run() {
		if (this.serverSocket != null) {
			Socket socket = null;
			try {
				System.out.println("[Machine " + this.machine.getId() + "]: Waiting for the machine that will use the port " + this.port + "...");
				socket = this.serverSocket.accept();
				System.out.println("[Machine " + this.machine.getId() + "]: Conected on port " + this.port);
				this.in = new DataInputStream(socket.getInputStream());
				this.out = new DataOutputStream(socket.getOutputStream());
				while (true) {
					String word = this.in.readUTF();
					System.out.println("[Machine " + this.machine.getId() + "]: " + word);
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
						System.out.println("[Machine " + this.machine.getId() + "]: Error: [" + event1.getMessage() + "]");
					}
				}
				System.out.println("[Machine " + this.machine.getId() + "]: This machine has been disconected");
				this.machine.remove();
			}
		}
	}
	
	public void resolve(DecriptMessage message, char first, char last) {
		try {
			this.out.writeUTF(message.getHash());
			this.out.writeInt(message.getCharacters());
			this.out.writeUTF(message.getDictionary());
			this.out.writeChar(first);
			this.out.writeChar(last);
		}
		catch (IOException event) {
			System.out.println("[Machine " + this.machine.getId() + "]: Error: [" + event.getMessage() + "]");
		}
	}
}
