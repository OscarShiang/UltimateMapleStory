package com.maple.networking;

import java.io.*;
import java.net.*;

import com.maple.game.MapleGame;
import com.maple.game.MapleStage;
import com.maple.player.PlayerComponent;

public class Server implements Runnable {
	public static final Integer DEFAULT_PORT = 8086;
	
	private MapleGame game;
	private ServerSocket server;
	private int connects;
	
	public final static int CLIENT_NUM = 1;
	
	private ObjectOutputStream[] clients;
	
	public Server(MapleGame game) throws IOException {
		connects = 0;
		this.game = game;
		clients = new ObjectOutputStream[CLIENT_NUM];
		server = new ServerSocket(DEFAULT_PORT);
	}
	
	@Override
	public void run() {
		try {
			System.out.println("[SERVER] start to listen to port");
			
			while (connects < CLIENT_NUM) {
				Socket socket = server.accept();
				System.out.println("[SERVER] connection accept");
				ServerWorker worker = new ServerWorker(game, socket, connects);
				Thread thr = new Thread(worker);
				thr.start();
				
				clients[connects] = worker.getReader();
				connects++;
			}
			
		} catch (IOException e) {
			System.out.println("[SERVER] fail to accept connection");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("[SERVER] fail to accept connection");
			e.printStackTrace();
		}
		
		game.setStage(MapleStage.SELECT);
	}
	
	public void updateAll() {
		for (int i = 0; i < CLIENT_NUM; i++) {
			try {
				clients[i].writeObject(
						new ServerPacket(
							game.getScores(),
							game.yeti.getComponent(PlayerComponent.class),
							game.mushroom.getComponent(PlayerComponent.class),
							game.pig.getComponent(PlayerComponent.class),
							game.slime.getComponent(PlayerComponent.class)
						)
				);
			} catch (IOException e) {
				System.out.println("[SERVER] can not send server packets");
				e.printStackTrace();
			}
		}
	}
}
