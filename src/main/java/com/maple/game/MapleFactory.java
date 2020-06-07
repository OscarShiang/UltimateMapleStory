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
	
	@Spawns("coin")
	public Entity newCoin(SpawnData data) {
		return entityBuilder()
				.type(MapleType.COIN)
				.viewWithBBox(new Circle(35, Color.GOLD))
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
}
