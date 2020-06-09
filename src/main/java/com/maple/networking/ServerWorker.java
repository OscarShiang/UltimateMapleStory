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
	
	private ObjectInputStream input;
	private ObjectOutputStream buf;
	
	private boolean stop;
	private int clientNum;
	
	/**
	 * Setting up a server worker
	 * @param socket set the socket
	 * @param clientNum determine the client number to use for recording score
	 */
	public ServerWorker(MapleGame game, Socket socket, int clientNum) {
		this.game = game;
		this.socket = socket;
		this.clientNum = clientNum;
		stop = false;
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
					
					// setting up score
					game.setScore(clientIn.score, clientNum);
					
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
