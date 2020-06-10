package com.maple.networking;

import java.io.*;

import com.maple.player.PlayerComponent;
import com.maple.player.PlayerInfo;

public class ServerPacket implements Serializable {

	/**
	 * the server packet to package everything needed
	 */
	private static final long serialVersionUID = 1L;
	
	
	public int stage;
	public int[] score;
	public PlayerInfo yeti, mushroom, pig, slime;
	
	/**
	 * Save the game progress of player position, game stage, score
	 */
	public ServerPacket(int[] score, PlayerInfo yeti, PlayerInfo mushroom, PlayerInfo pig, PlayerInfo slime) {
		this.score = score;
		this.yeti = yeti;
		this.mushroom = mushroom;
		this.pig = pig;
		this.slime = slime;
	}
	
	public void setStage(int stage) {
		this.stage = stage;
	}
	
	public void setYeti(PlayerInfo yeti) {
		this.yeti = yeti;
	}
	
	public void setMushroom(PlayerInfo mushroom) {
		this.mushroom = mushroom;
	}
	
	public void setPig(PlayerInfo pig) {
		this.pig = pig;
	}
	
	public void setSlime(PlayerInfo slime) {
		this.slime = slime;
	}
}
