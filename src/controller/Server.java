package controller;

import java.lang.reflect.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.DecriptMessage;

public class Server {
	public static final int CLIENT_PORT = 3000;
	public static final int PORT = 3001;
	public static int MACHINES = 3;
	
	private Map<Integer, Machine> machines;
	private List<Thread> thread;
	private DiscoverThread discoverThread;
	private ClientConection clientThread;
	
	public Server() {
		this.machines = new HashMap<>();
		this.clientThread = new ClientConection(this, Server.CLIENT_PORT);
		this.discoverThread = new DiscoverThread(this);
	}
	
	public void initializeServer() {
		this.clientThread.start();
		this.discoverThread.start();
	}
	
	public void initializeClientConection() {
		this.clientThread.run();
	}
	
	public void addExternalMachine(int port) {
		Machine machine = new Machine(port, this);
		this.machines.put(port, machine);
	}
	
	public boolean containsExternalMachine(int port) {
		return this.machines.containsKey(port);
	}
	
	public static void main(String[] args) {
		Server control = new Server();
		control.initializeServer();
	}
	
	public void resolve(Object auxMessage) {
		if (this.machines.size() == 0) {
			this.requestInt(-1);
			System.out.println("[Server]: No available servers on the moment");
			this.requestUTF("No available servers on the moment");
			return;
		}
		
		this.requestInt(0);
		System.out.println("[Server]: Decoding the hash, please wait");
		this.requestUTF("Decoding the hash, please wait");
		//holo
			
		try {
			Method method = auxMessage.getClass().getMethod("getDictionary", null);
			String dictionary = (String) method.invoke(auxMessage, null);
			
			
			int n = dictionary.length() / this.machines.size();
			int i = 0;
			for (int port : this.machines.keySet()) {
				char first =dictionary.charAt(i);
				i += n + 1;
				i = ((i >= dictionary.length()) ? dictionary.length() - 1 : i);
				char last = dictionary.charAt(i);
				System.out.println(first + " " + last);
				Machine m = this.machines.get(port);
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						m.resolve(auxMessage, first, last);
					}
				}).start();
				
			}
			
		} catch (NoSuchMethodException | SecurityException e) {
			System.out.println("Error al acceder al metodo" + e);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println("Error al acceder al metodo" + e);
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.out.println("Error al acceder al metodo" + e);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.out.println("Error al acceder al metodo" + e);
			e.printStackTrace();
		}
	}
	
	public void finishProcees(){
		
	}
	public void requestUTF(String word) {
		this.clientThread.requestUTF(word);
	}
	
	public void requestInt(int n) {
		this.clientThread.requestInt(n);
	}
	
	public void requestObject(Object object) {
		this.clientThread.requestObject(object);
	}
	
	public void removeMachine(int port) {
		this.machines.remove(port);
	}
}
