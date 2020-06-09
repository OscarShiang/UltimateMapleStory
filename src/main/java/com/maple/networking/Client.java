package com.maple.networking;

import java.io.*;
import java.net.*;

import com.maple.game.MapleGame;
import com.maple.game.MapleStage;
import com.maple.player.PlayerComponent;
import com.maple.player.PlayerType;

public class Client implements Runnable {

	private String ip;
	private int port;
	
	MapleGame game;
	
	private Socket socket;
	
	private ObjectInputStream input;
	private ObjectOutputStream buf;
	
	private ServerPacket packet;
	
	public Client(MapleGame game, String ip, int port) throws UnknownHostException, IOException {
		this.game = game;
		this.ip = ip;
		this.port = port;
	}
	
	public void sendClientData() {
		System.out.println("Send out data");
		try {
			buf.writeObject(new ClientPacket(game.getScores(), game.getPlayer().getComponent(PlayerComponent.class), game.getPlayerType()));
		} catch (IOException e) {
			System.out.println("[CLIENT] can not send files");
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			System.out.println("[CLIENT] try to make connection");
			socket = new Socket(ip, port);
			System.out.println("[CLIENT] connention accepted");
			
			game.selectCharacter();
			
			input = new ObjectInputStream(socket.getInputStream());
			buf = new ObjectOutputStream(socket.getOutputStream());
			
			while (!Thread.interrupted()) {
				packet = (ServerPacket)input.readObject();
				
				// reading player settings
				if (game.getPlayerType() != PlayerType.PIG)
					game.setPig(packet.pig);
				if (game.getPlayerType() != PlayerType.YETI)
					game.setYeti(packet.yeti);
				if (game.getPlayerType() != PlayerType.MUSHROOM)
					game.setMushroom(packet.mushroom);
				if (game.getPlayerType() != PlayerType.SLIME)
					game.setSlime(packet.slime);
				
				// reading score and stage
				for (int i = 0; i < 4; i++) {
					game.setScore(packet.score[i], i);
				}
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
		} catch (Exception e) {
			System.out.println("[CLIENT] connection error");
			e.printStackTrace();
		}
	}

}
