package com.maple.networking;

import java.io.*;
import java.net.*;

/**
 * A server worker processing packets from client
 */
public class ServerWorker implements Runnable {
	private Socket socket;
	
	private ObjectInputStream input;
	private ObjectOutputStream buf;
	
	private boolean stop;
	private int clientNum;
	
	/**
	 * Setting up a server worker
	 * @param socket set the socket
	 * @param clientNum determine the client num to use for recording score
	 */
	public ServerWorker(Socket socket, int clientNum) {
		this.socket = socket;
		stop = false;
		this.clientNum = clientNum;
	}
	
	public void run() {
		try  {
			input = new ObjectInputStream(socket.getInputStream());
			buf = new ObjectOutputStream(socket.getOutputStream());
			
			while (!Thread.interrupted() && !stop) {
				try {
					ClientPacket clientIn = (ClientPacket)input.readObject();
					
					// setting player entity
					switch(clientIn.type) {
					case YETI:
						
						break;
					case SLIME:
						
						break;
					case PIG:
						
						break;
					case MUSHROOM:
						
						break;
					}
					
					// setting up 
					
				} catch (ClassNotFoundException e) {
					System.out.println("[SERVER] worker: object read failed");
					e.printStackTrace();
				}
			}
			
			System.out.println("[SERVER] worker: shudown");
			
			socket.close();
			input.close();
			buf.close();
			
		} catch (IOException e) {
			System.out.println("[SERVER] woker: connection failed");
			e.printStackTrace();
		}
	}
}
