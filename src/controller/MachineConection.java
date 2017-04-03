package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MachineConection extends Thread {
	private Machine machine;
	private String hash;
	private int characters;
	private String dictionary;
	private char first;
	private char last;
	
	public MachineConection(Machine machine, String hash, int characters, String dictionary, char first, char last) {
		this.machine = machine;
		this.hash = hash;
		this.characters = characters;
		this.dictionary = dictionary;
		this.first = first;
		this.last = last;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("[Machine " + this.machine.getID() + "]: Conecting to external server [" + this.machine.getHost().getHostAddress() + " : " + this.machine.getPort() + "]...");
			Socket socket = new Socket(this.machine.getHost(), this.machine.getPort());
			System.out.println("[Machine " + this.machine.getID() + "]: Conected to external server [" + this.machine.getHost().getHostAddress() + " : " + this.machine.getPort() + "]");
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			output.writeObject(this.hash);
			output.writeObject(this.characters);
			output.writeObject(this.dictionary);
			output.writeObject(this.first);
			output.writeObject(this.last);
			System.out.println("[Machine " + this.machine.getID() + "]: Waiting for response of the external server...");
			int confirmation = (int) input.readObject();
			String password = (String) input.readObject();
			System.out.println("[Machine " + this.machine.getID() + "]: Response for the given hash [" + password + "]");
			if (confirmation == 0) {
				this.machine.serverToClient(password);
				this.machine.stopOtherMachines();
			}
			
			socket.close();
			System.out.println("[Machine " + this.machine.getID() + "]: Finished");
			this.machine.reset();
		}
		catch (IOException event) {
			System.out.println("[Machine " + this.machine.getID() + "]: This machine has been disconected");
			this.machine.remove();
		}
		catch (ClassNotFoundException event) {
			System.out.println("[Machine " + this.machine.getID() + "]: Error: [" + event.getMessage() + "]");
		}
	}
}
