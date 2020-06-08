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

import com.maple.player.*;
import com.maple.item.*;

import static com.almasb.fxgl.dsl.FXGL.*;

import com.almasb.fxgl.dsl.views.ScrollingBackgroundView;

public class MapleFactory implements EntityFactory {
	@Spawns("background")
	public Entity newBackground(SpawnData data) {
		return entityBuilder()
				.view(new ScrollingBackgroundView(texture("background/forest.png")))
                .zIndex(-1)
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
	
	@Spawns("deadline")
	public Entity newDeadline(SpawnData data) {
		return entityBuilder()
				.type(MapleType.DEADLINE)
				.bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
				.with(new CollidableComponent(true))
				.build();
	}
	
	@Spawns("player")
	public Entity newPlayer(SpawnData data) {
		PhysicsComponent physics = new PhysicsComponent();
		physics.setBodyType(BodyType.DYNAMIC);
		
        return entityBuilder()
                .type(MapleType.PLAYER)
                .with(physics)
                .bbox(new HitBox(BoundingShape.box(100, 100)))
                .with(new CollidableComponent(true))
                .with(new PlayerComponent())
                .build();
	}
	
	@Spawns("slime")
	public Entity newSlime(SpawnData data) {
		PhysicsComponent physics = new PhysicsComponent();
		physics.setBodyType(BodyType.DYNAMIC);
		
        return entityBuilder()
                .type(MapleType.PLAYER)
                .with(physics)
                .bbox(new HitBox(BoundingShape.box(100, 100)))
                .with(new CollidableComponent(true))
                .with(new PlayerComponent(PlayerType.SLIME))
                .build();
	}
	
	@Spawns("yeti")
	public Entity newYeti(SpawnData data) {
		PhysicsComponent physics = new PhysicsComponent();
		physics.setBodyType(BodyType.DYNAMIC);
		
        return entityBuilder()
                .type(MapleType.PLAYER)
                .bbox(new HitBox(BoundingShape.box(100, 100)))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new PlayerComponent(PlayerType.YETI))
                .build();
	}
	
	@Spawns("mushroom")
	public Entity newMush(SpawnData data) {
		PhysicsComponent physics = new PhysicsComponent();
		physics.setBodyType(BodyType.DYNAMIC);
		
        return entityBuilder()
                .type(MapleType.PLAYER)
                .with(physics)
                .bbox(new HitBox(BoundingShape.box(100, 100)))
                .with(new CollidableComponent(true))
                .with(new PlayerComponent(PlayerType.MUSHROOM))
                .build();
	}
	
	@Spawns("pig")
	public Entity newPig(SpawnData data) {
		PhysicsComponent physics = new PhysicsComponent();
		physics.setBodyType(BodyType.DYNAMIC);
		
        return entityBuilder()
                .type(MapleType.PLAYER)
                .with(physics)
                .bbox(new HitBox(BoundingShape.box(100, 100)))
                .with(new CollidableComponent(true))
                .with(new PlayerComponent(PlayerType.PIG))
                .build();
	}
	

	@Spawns("coin")
	public Entity newCoin(SpawnData data) {
		return entityBuilder()
				.type(MapleType.COIN)
				.with(new ItemComponent(ItemType.coin))
				.with(new CollidableComponent(true))
				.bbox(new HitBox(BoundingShape.box(30, 30)))
				.build();
	}
	
	@Spawns("redflag")
	public Entity newRedFlag(SpawnData data) {
		PhysicsComponent physics = new PhysicsComponent();
		physics.setBodyType(BodyType.STATIC);
		
		return entityBuilder()
				.type(MapleType.ITEM)
				.with(physics)
				.with(new CollidableComponent(true))
				.with(new ItemComponent(ItemType.redflag))
				.bbox(new HitBox(BoundingShape.box(84, 100)))
				.build();
	}
	@Spawns("tomb")
	public Entity newTomb(SpawnData data) {
		PhysicsComponent physics = new PhysicsComponent();
		physics.setBodyType(BodyType.DYNAMIC);
		
		return entityBuilder()
				.type(MapleType.TOMB)
				.with(physics)
				.with(new CollidableComponent(true))
				.with(new ItemComponent(ItemType.tomb))
				.bbox(new HitBox(BoundingShape.box(94, 43)))
				.build();
	}
	
	@Spawns("balloon")
	public Entity newBalloon(SpawnData data) {
		PhysicsComponent physics = new PhysicsComponent();
		physics.setBodyType(BodyType.STATIC);
		
		return entityBuilder()
				.type(MapleType.PLATFORM)
				.with(physics)
				.with(new CollidableComponent(true))
				.with(new ItemComponent(ItemType.balloon))
				.bbox(new HitBox(BoundingShape.box(70, 140)))
				.build();
	}
}
