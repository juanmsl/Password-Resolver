package machine;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ExternalMachineThread extends Thread {
	private ServerSocket serverSocket;
	
	public ExternalMachineThread(int port) {
		try {
			this.serverSocket = new ServerSocket(port);
			System.out.println("[External machine thread]: Server initialized in port " + this.serverSocket.getLocalPort() + "...");
		}
		catch (IOException event) {
			System.out.println("[External machine thread]: Port " + port + " already in use");
			this.serverSocket = null;
		}
	}
	
	@Override
	public void run() {
		if (this.serverSocket != null) {
			try {
				while (true) {
					System.out.println("[External machine thread]: Waiting for the main server...");
					Socket socket = this.serverSocket.accept();
					System.out.println("[External machine thread]: Main server connected");
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					String hashToFind = (String) in.readObject();
					int characters = (int) in.readObject();
					String dictionary = (String) in.readObject();
					char first = (char) in.readObject();
					char last = (char) in.readObject();
					System.out.println("-------------------------------------------------------------------------");
					System.out.println("Recived hash: " + hashToFind);
					System.out.println("Finding...");
					String word = this.getTextFromHash(dictionary, characters, first, last, hashToFind);
					System.out.println("Password" + ((word == null) ? " not" : "") + " founded: " + word);
					System.out.println("-------------------------------------------------------------------------");
					if (word != null) {
						out.writeObject(0);
						out.writeObject(word);
					} else {
						out.writeObject(-1);
						out.writeObject("Password not founded");
					}
					socket.close();
				}
			}
			catch (IOException | ClassNotFoundException event) {
				System.out.println("[External machine thread]: Main server has disconected");
				try {
					this.serverSocket.close();
				}
				catch (IOException event1) {
					System.out.println("[External machine thread]: Error: [" + event1.getMessage() + "]");
				}
			}
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
}
