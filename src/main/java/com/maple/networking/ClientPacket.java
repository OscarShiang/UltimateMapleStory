package com.maple.networking;

import java.io.*;

import com.maple.player.PlayerComponent;
import com.maple.player.PlayerType;

public class ClientPacket implements Serializable {

	/**
	 * Packet of client data package
	 */
	private static final long serialVersionUID = 1L;
	
	public int score;
	public PlayerType type;
	public PlayerComponent player;
	
	public ClientPacket(int score, PlayerComponent player, PlayerType type) {
		this.score = score;
		this.player = player;
		this.type = type;
	}
}
