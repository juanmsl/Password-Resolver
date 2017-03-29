package machine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ExternalMachine {
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private int port;
	private String host;
	
	public ExternalMachine(String host, int port) {
		try {
			System.out.println("Conecting machine to " + host + " on port " + port);
			this.socket = new Socket(host, port);
			this.in = new DataInputStream(this.socket.getInputStream());
			this.out = null;
			this.host = host;
			this.port = this.in.readInt();
			if (this.port != -1) {
				this.initializeMachine();
			} else {
				System.out.println("Server full of external machines");
			}
		}
		catch (UnknownHostException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
		catch (IOException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
	}
	
	public void initializeMachine() {
		try {
			System.out.println("Conecting to server on port " + this.port + "...");
			this.socket = new Socket(this.host, this.port);
			System.out.println("Server conected");
			this.in = new DataInputStream(this.socket.getInputStream());
			this.out = new DataOutputStream(this.socket.getOutputStream());
			this.doActivities();
			this.socket.close();
		}
		catch (IOException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
	}
	
	private void doActivities() {
		try {
			int n = 0;
			while (n != -1) {
				n = this.in.readInt();
				this.contar(n);
				System.out.println("Recibido: " + n);
			}
		}
		catch (IOException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
	}
	
	public void contar(int n) {
		for (int i = 0; i < n; i++) {
			System.out.println(i);
		}
	}
	
	public static void main(String[] args) {
		int port = Integer.parseInt(args[1]);
		new ExternalMachine(args[0], port);
	}
}
