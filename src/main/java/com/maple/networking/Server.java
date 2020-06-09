package com.maple.networking;

import java.io.*;
import java.net.*;

import com.maple.game.MapleGame;

public class Server implements Runnable {
	
	private MapleGame game;
	private ServerSocket socket;
	private int connects;
	
	public final int DEFAULT_PORT = 8084;
	
	public Server(MapleGame game) {
		connects = 0;
		this.game = game;
	}
	
	public void host() {
		try {
			socket = new ServerSocket(DEFAULT_PORT);

			while (connects <= 3) {
				
			}
		} catch (IOException e) {
			System.out.println("[SERVER] connection break...");
			e.printStackTrace();
		}
	}
	
	public void updateAll() {
		
	}

	@Override
	public void run() {
		host();
	}
}
