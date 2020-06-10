package com.maple.networking;

import java.io.*;
import java.net.*;

import com.maple.game.*;

/**
 * A server worker processing packets from client
 */
public class ServerWorker implements Runnable {
	private MapleGame game;
	
	private Socket socket;
	
	private ObjectInputStream clientInput;
	private ObjectOutputStream buf;
	
	private boolean stop;
	private int clientNum;
	
	private int [] scores;
	
	/**
	 * Setting up a server worker
	 * @param socket set the socket
	 * @param clientNum determine the client number to use for recording score
	 * @throws IOException 
	 */
	public ServerWorker(MapleGame game, Socket socket, int clientNum) throws IOException {
		this.game = game;
		this.socket = socket;
		this.clientNum = clientNum;
		stop = false;
		scores = new int[4];
		
		socket.setKeepAlive(true);
		buf = new ObjectOutputStream(socket.getOutputStream());
		clientInput = new ObjectInputStream(socket.getInputStream());
	}
	
	public void run() {

		System.out.println("[SERVER] receive connection");
		
		try {
			System.out.println("[SERVER] receive connection");
			
			while (!Thread.interrupted() && !stop) {
				
					ClientPacket clientIn = (ClientPacket)clientInput.readObject();
					
					// setting player entity
					switch(clientIn.type) {
					case YETI:
						game.setYeti(clientIn.player);
						break;
					case SLIME:
						game.setSlime(clientIn.player);
						break;
					case PIG:
						game.setPig(clientIn.player);
						break;
					case MUSHROOM:
						game.setMushroom(clientIn.player);
						break;
					}
					
					scores = clientIn.score;
					
					// setting up score
					game.setScore(scores[clientNum], clientNum);
					
				
			}
			
			System.out.println("[SERVER] worker: shutdown");
			
			socket.close();
			clientInput.close();
			buf.close();
		} catch (IllegalStateException e) {
			System.out.println("Test");
			e.printStackTrace();

		} catch (Exception e) {
			System.out.println("[SERVER] worker: object read failed");
			e.printStackTrace();
		}
	}
	
	public ObjectOutputStream getReader() {
		return buf;
	}
}
