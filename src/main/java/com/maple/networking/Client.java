package com.maple.networking;

import java.io.*;
import java.net.*;

public class Client implements Runnable {

	private String ip;
	private int port;
	
	private Socket socket;
	
	private ObjectInputStream input;
	private ObjectOutputStream buf;
	
	private ServerPacket serverPacket;
	
	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	@Override
	public void run() {
		try {
			socket = new Socket(ip, port);
			input = new ObjectInputStream(socket.getInputStream());
			buf = new ObjectOutputStream(socket.getOutputStream());
			
			while (!Thread.interrupted()) {
				serverPacket = (ServerPacket)input.readObject();
			}
			
			System.out.println("[CLIENT] shutdown");
			input.close();
			buf.close();
			
		} catch (IOException e) {
			System.out.println("[CLIENT] connection refused");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("[CLIENT] Packet error");
			e.printStackTrace();
		}
	}

}
