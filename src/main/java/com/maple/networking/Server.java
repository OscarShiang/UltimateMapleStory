package com.maple.networking;

import java.io.*;
import java.net.*;

import com.maple.game.MapleGame;

public class Server implements Runnable {
	public static final Integer DEFAULT_PORT = 8086;
	
	private MapleGame game;
	private ServerSocket server;
	private int connects;
	
	private ObjectOutputStream[] clients;
	
	public Server(MapleGame game) throws IOException {
		connects = 0;
		this.game = game;
		
		server = new ServerSocket(DEFAULT_PORT);
	}
	
	public void host() {
		while (connects < 4) {
			Socket socket = null;
			try {
				socket = server.accept();
				ServerWorker worker = new ServerWorker(game, socket, connects);
				worker.run();
				
				clients[connects] = worker.getReader();
				connects++;
			} catch (IOException e) {
				System.out.println("[SERVER] fail to accept connection");
				e.printStackTrace();
			}
		}
	}
	
	public void updateAll(ServerPacket packet) {
		for(ObjectOutputStream client : clients) {
			try {
				client.writeObject(packet);
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
