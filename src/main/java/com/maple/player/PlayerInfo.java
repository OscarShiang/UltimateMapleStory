package com.maple.player;

import java.io.Serializable;

import com.almasb.fxgl.physics.PhysicsComponent;

public class PlayerInfo implements Serializable {

	/**
	 * Player information that will be packaged
	 */
	private static final long serialVersionUID = 1L;
	
	public int x, y, scale;

	/**
	 * Package all information needed
	 * @param x x position of player
	 * @param y y position of player
	 * @param scale scaleX from player
	 */
	public PlayerInfo(int x, int y, int scale) {
		this.x = x;
		this.y = y;
		this.scale = scale;
	}
}
