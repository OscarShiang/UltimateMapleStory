package com.maple.networking;

import java.io.*;
import java.net.*;

public class Server implements Runnable {
	private ServerSocket socket;
	private int connects;
	
	public final int DEFAULT_PORT = 8084;
	
	public Server() {
		connects = 0;
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
