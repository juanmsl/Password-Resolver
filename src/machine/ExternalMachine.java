package machine;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ExternalMachine {
	private Socket socket;
	private DataInputStream in;
	
	public ExternalMachine(String host, int port) {
		try {
			System.out.println("Conecting machine to " + host + " on port " + port);
			this.socket = new Socket(host, port);
			this.in = new DataInputStream(this.socket.getInputStream());
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
			int n = 0;
			while (n != -1) {
				n = this.in.readInt();
				this.contar(n);
				System.out.println("Recibido: " + n);
			}
			this.socket.close();
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
		ExternalMachine client = new ExternalMachine(args[0], port);
		client.initializeMachine();
	}
}
