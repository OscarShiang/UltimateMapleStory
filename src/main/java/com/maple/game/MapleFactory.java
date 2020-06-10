package com.maple.game;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyDef;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;


import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import com.maple.player.*;
import com.maple.item.*;

import static com.almasb.fxgl.dsl.FXGL.*;

import com.almasb.fxgl.dsl.components.LiftComponent;
import com.almasb.fxgl.dsl.views.ScrollingBackgroundView;

public class MapleFactory implements EntityFactory {
	private MapleGame game;
	
	
	public MapleFactory(MapleGame game) {
		this.game = game;
	}
	
	@Spawns("background")
	public Entity newBackground(SpawnData data) {
		return entityBuilder()
				.view(new ScrollingBackgroundView(texture("background/forest.png")))
                .zIndex(-1)
                .with(new GroundComponent(game))
                .with(new IrremovableComponent())
				.build();
	}
	
	@Spawns("platform")
	public Entity newPlatform(SpawnData data) {
		return entityBuilder()
				.type(MapleType.PLATFORM)
				.bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
				.with(new CollidableComponent(true))
                .with(new PhysicsComponent())
				.build();
	}
	
	@Spawns("wall")
	public Entity newWall(SpawnData data) {
		return entityBuilder()
				.type(MapleType.WALL)
				.bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
				.with(new CollidableComponent(true))
				.with(new PhysicsComponent())
				.build();
	}
	
	private Entity newPlayer(SpawnData data, PlayerType type) {
		PhysicsComponent physics = new PhysicsComponent();
		physics.setBodyType(BodyType.DYNAMIC);
		
		
        return entityBuilder()
                .type(MapleType.PLAYER)
                .from(data)
                .with(physics)
                .bbox(new HitBox(BoundingShape.box(80, 80)))
                .with(new CollidableComponent(true))
                .with(new PlayerComponent(type))
                .build();
	}
	
	@Spawns("player")
	public Entity newPlayer(SpawnData data) {
		PhysicsComponent physics = new PhysicsComponent();
		physics.setBodyType(BodyType.DYNAMIC);
		
        return entityBuilder()
                .type(MapleType.PLAYER)
                .from(data)
                .with(physics)
                .bbox(new HitBox(BoundingShape.box(80, 80)))
                .with(new CollidableComponent(true))
                .with(new PlayerComponent())
                .build();
	}
	
	@Spawns("tomb")
	public Entity newTomb(SpawnData data) {
		PhysicsComponent physics = new PhysicsComponent();
		physics.setBodyType(BodyType.DYNAMIC);
		
		return entityBuilder()
				.type(MapleType.TOMB)
				.from(data)
				.with(physics)
				.with(new CollidableComponent(true))
				.with(new ItemComponent(ItemType.tomb))
				.bbox(new HitBox(BoundingShape.box(94, 43)))
				.build();
	}

	
	@Spawns("slime")
	public Entity newSlime(SpawnData data) {
		return newPlayer(data, PlayerType.SLIME);
	}
	
	@Spawns("yeti")
	public Entity newYeti(SpawnData data) {
		return newPlayer(data, PlayerType.YETI);
	}
	
	@Spawns("mushroom")
	public Entity newMush(SpawnData data) {
		return newPlayer(data, PlayerType.MUSHROOM);
	}
	
	@Spawns("pig")
	public Entity newPig(SpawnData data) {
		return newPlayer(data, PlayerType.PIG);
	}
	
	@Spawns("redflag")
	public Entity newRedFlag(SpawnData data) {
		
		return entityBuilder()
				.type(MapleType.ITEM)
				.from(data)
				.with(new ItemComponent(ItemType.redflag))
				.bbox(new HitBox(BoundingShape.box(84, 100)))
				.with(new CollidableComponent(true))
				.build();
	}
	
	@Spawns("teleport2")
	public Entity newTeleport2(SpawnData data) {
		LiftComponent lift = new LiftComponent();
        lift.setGoingUp(true);
        lift.yAxisDistanceDuration(20, Duration.seconds(2.5));
		
		return entityBuilder()
				.type(MapleType.ITEM)
				.from(data)
				.with(new CollidableComponent(false))
				.with(new ItemComponent(ItemType.teleport2))
				.with(lift)
				.bbox(new HitBox(BoundingShape.box(47, 80)))
				.build();
	}
	
	@Spawns("balloon")
	public Entity newBalloon(SpawnData data) {
		PhysicsComponent physics = new PhysicsComponent();
		physics.setBodyType(BodyType.STATIC);
		
		LiftComponent lift = new LiftComponent();
        lift.setGoingUp(true);
        lift.yAxisDistanceDuration(20, Duration.seconds(1));
		
		return entityBuilder()
				.type(MapleType.PLATFORM)
				.from(data)
				.with(physics)
				.with(new CollidableComponent(true))
				.with(new ItemComponent(ItemType.balloon))
				.with(lift)
				.bbox(new HitBox(BoundingShape.box(70, 140)))
				.build();
	}

	@Spawns("teleport1")
	public Entity newTeleport1(SpawnData data) {
		LiftComponent lift = new LiftComponent();
        lift.setGoingUp(true);
        lift.yAxisDistanceDuration(20, Duration.seconds(2.5));
		
		return entityBuilder()
				.type(MapleType.TELEPORT1)
				.from(data)
				.bbox(new HitBox(BoundingShape.box(47, 80)))
				.with(new CollidableComponent(true))
				.with(lift)
				.with(new ItemComponent(ItemType.teleport1))
				.build();
	}
	
	@Spawns("deadline")
	public Entity newDeadline(SpawnData data) {
		return entityBuilder()
				.type(MapleType.DEADLINE)
				.from(data)
				.bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
				.with(new CollidableComponent(true))
				.build();
	}
	
	@Spawns("coin")
	public Entity newCoin(SpawnData data) {
		LiftComponent lift = new LiftComponent();
        lift.setGoingUp(true);
        lift.yAxisDistanceDuration(6, Duration.seconds(1));
		
		return entityBuilder()
				.type(MapleType.COIN)
				.from(data)
				.bbox(new HitBox(BoundingShape.box(25, 25)))
				.with(new ItemComponent(ItemType.coin))
				.with(lift)
				.with(new CollidableComponent(true))
				.build();
	}
	
	@Spawns("surprise")
	public Entity newSurprise(SpawnData data) {
		
		return entityBuilder()
				.type(MapleType.TRAP)
				.from(data)
				.with(new CollidableComponent(true))
				.with(new ItemComponent(ItemType.surprise))
				.bbox(new HitBox(new Point2D(30,15), BoundingShape.box(200, 90)))
				.build();
	}
	
	@Spawns("hole")
	public Entity newHole(SpawnData data) {
		
		return entityBuilder()
				.type(MapleType.TRAP)
				.from(data)
				.with(new CollidableComponent(true))
				.with(new ItemComponent(ItemType.hole))
				.bbox(new HitBox(new Point2D(25, 25),BoundingShape.box(50, 50)))
				.build();
	}
	
	@Spawns("brick")
	public Entity newBrick(SpawnData data) {
		
		return entityBuilder()
				.type(MapleType.PLATFORM)
				.from(data)
				.with(new PhysicsComponent())
				.with(new CollidableComponent(true))
				.with(new ItemComponent(ItemType.brick))
				.bbox(new HitBox(BoundingShape.box(110, 66)))
				.build();
	}
	
}
