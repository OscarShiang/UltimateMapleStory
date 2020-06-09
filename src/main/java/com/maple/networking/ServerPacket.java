package com.maple.networking;

import java.io.*;

import com.maple.player.PlayerComponent;

public class ServerPacket implements Serializable {

	/**
	 * the server packet to package everything needed
	 */
	private static final long serialVersionUID = 1L;
	
	
	public int stage;
	public int[] score;
	public PlayerComponent yeti, mushroom, pig, slime;
	
	/**
	 * Save the game progress of player position, game stage, score
	 */
	public ServerPacket(int[] score, PlayerComponent yeti, PlayerComponent mushroom, PlayerComponent pig, PlayerComponent slime) {
		this.score = score;
		this.yeti = yeti;
		this.mushroom = mushroom;
		this.pig = pig;
		this.slime = slime;
	}
	
	public void setStage(int stage) {
		this.stage = stage;
	}
	
	public void setYeti(PlayerComponent yeti) {
		this.yeti = yeti;
	}
	
	public void setMushroom(PlayerComponent mushroom) {
		this.mushroom = mushroom;
	}
	
	public void setPig(PlayerComponent pig) {
		this.pig = pig;
	}
	
	public void setSlime(PlayerComponent slime) {
		this.slime = slime;
	}
}
