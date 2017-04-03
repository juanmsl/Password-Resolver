package machine;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ExternalMachine {
	private int port;
	private ExternalMachineThread externalMachineThread;
	
	public ExternalMachine(String host, int port) {
		try {
			System.out.println("Asking for my final port in [" + host + " : " + port + "]");
			Socket socket = new Socket(host, port);
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			this.port = (int) in.readObject();
			if (this.port != -1) {
				this.externalMachineThread = new ExternalMachineThread(this.port);
				this.externalMachineThread.start();
			} else {
				System.out.println("Server full of external machines");
			}
			socket.close();
		}
		catch (IOException | ClassNotFoundException event) {
			System.out.println("[External machine]: Server not founded in [" + host + " : " + port + "]");
		}
	}
	
	public static void main(String[] args) {
		int port = Integer.parseInt(args[1]);
		new ExternalMachine(args[0], port);
	}
}
