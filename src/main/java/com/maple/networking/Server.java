package com.maple.networking;

import java.io.*;
import java.net.*;

import com.maple.game.MapleGame;
import com.maple.game.MapleStage;
import com.maple.player.PlayerComponent;

public class Server implements Runnable {
	public static final Integer DEFAULT_PORT = 8084;
	
	private MapleGame game;
	private ServerSocket server;
	private int connects;
	
	private ObjectOutputStream[] clients;
	
	public Server(MapleGame game) throws IOException {
		connects = 0;
		this.game = game;
		clients = new ObjectOutputStream[4];
	}
	
	public void host() {
		try {
			server = new ServerSocket(DEFAULT_PORT);
			
			System.out.println("[SERVER] start to listen to port");
			
//			while (connects < 4) {
				Socket socket = server.accept();
				System.out.println("[SERVER] connection accept");
				ServerWorker worker = new ServerWorker(game, socket, connects);
				Thread thr = new Thread(worker);
				thr.start();
				
				clients[connects] = worker.getReader();
				connects++;
//			}
			
		} catch (IOException e) {
			System.out.println("[SERVER] fail to accept connection");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("[SERVER] fail to accept connection");
			e.printStackTrace();
		}
		
		game.selectCharacter();
	}
	
	public void updateAll() {
		for(ObjectOutputStream client : clients) {
			try {
				client.writeObject(new ServerPacket(
						game.getScores(),
						game.yeti.getComponent(PlayerComponent.class),
						game.mushroom.getComponent(PlayerComponent.class),
						game.pig.getComponent(PlayerComponent.class),
						game.slime.getComponent(PlayerComponent.class))
				);
			} catch (IOException e) {
				System.out.println("[SERVER] can not send server packets");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		host();
	}
}
