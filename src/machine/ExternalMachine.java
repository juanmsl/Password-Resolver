package machine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
			while (true) {
				String hashToFind = this.in.readUTF();
				int characters = this.in.readInt();
				String dictionary = this.in.readUTF();
				char first = this.in.readChar();
				char last = this.in.readChar();
				System.out.println("Recived hash: " + hashToFind + "\nFinding...");
				String word = this.getTextFromHash(dictionary, characters, first, last, hashToFind);
				System.out.println("Password" + ((word == null) ? " not" : "") + " founded: " + word);
				if (word != null) {
					this.out.writeUTF(word);
				}
			}
		}
		catch (IOException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
	}
	
	private String getTextFromHash(String dictionary, int characters, char first, char last, String hashToFind) {
		String firstPassword = "";
		String lastPassword = "";
		for (int i = 0; i < characters; i++) {
			firstPassword += String.valueOf(first);
			lastPassword += String.valueOf(last);
		}
		System.out.println("Resolving from " + firstPassword + " to " + lastPassword);
		while (!firstPassword.equals(lastPassword)) {
			String hash = this.getHashOf(firstPassword);
			if (hash.equals(hashToFind)) { return firstPassword; }
			firstPassword = this.getNextCombination(characters, dictionary, firstPassword);
		}
		return null;
	}
	
	private String getNextCombination(int n, String dic, String word) {
		int x[] = new int[n];
		for (int i = 0; i < n; i++) {
			x[i] = dic.indexOf(word.charAt(i));
		}
		
		x[n - 1] = (((x[n - 1] + 1) < dic.length()) ? x[n - 1] + 1 : 0);
		x[n - 2] = ((x[n - 1] > 0) ? x[n - 2] : x[n - 2] + 1);
		
		for (int i = n - 2; i > 0; i--) {
			x[i - 1] = ((x[i] == dic.length()) ? x[i - 1] + 1 : x[i - 1]);
			x[i] = ((x[i] < dic.length()) ? x[i] : 0);
		}
		
		String next = "";
		for (int i = 0; i < n; i++) {
			next += String.valueOf(dic.charAt(x[i]));
		}
		return next;
	}
	
	private String getHashOf(String text) {
		String hash = null;
		try {
			MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
			sha256.update(text.getBytes("UTF-8"));
			byte[] digest = sha256.digest();
			StringBuffer stringBuffer = new StringBuffer();
			for (byte element : digest) {
				stringBuffer.append(String.format("%02x", element));
			}
			hash = stringBuffer.toString();
		}
		catch (NoSuchAlgorithmException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
		catch (UnsupportedEncodingException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
		return hash;
	}
	
	public static void main(String[] args) {
		int port = Integer.parseInt(args[1]);
		new ExternalMachine(args[0], port);
	}
}
