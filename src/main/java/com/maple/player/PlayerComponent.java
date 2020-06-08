package com.maple.player;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlayerComponent extends Component {
	private PhysicsComponent physics;
	private boolean isJump;
	
	private AnimatedTexture texture;
	private AnimationChannel idle, walk, jump;
	
	// default
	public PlayerComponent() {
		isJump = false;
		
		Image image = image("sprites/mushroom_sprite (Custom).png");
		
		idle = new AnimationChannel(image, 5, 100, 100, Duration.seconds(1), 0, 1);
		walk = new AnimationChannel(image, 5, 100, 100, Duration.seconds(0.6), 2, 4);
		jump = new AnimationChannel(image, 5, 100, 100, Duration.seconds(1), 3, 3);
		
		texture = new AnimatedTexture(idle);
	}
	
	// custom
	public PlayerComponent(PlayerType type) {
		isJump = false;
		Image image;
		
		switch(type) {
		case  MUSHROOM:
			image = image("sprites/mushroom_sprite (Custom).png");
			idle = new AnimationChannel(image, 5, 80, 80, Duration.seconds(1), 0, 1);
			walk = new AnimationChannel(image, 5, 80, 80, Duration.seconds(0.6), 2, 4);
			jump = new AnimationChannel(image, 5, 80, 80, Duration.seconds(1), 3, 3);
			break;
		case YETI:
			image = image("sprites/yeti_idle_sprite (Custom).png");
			Image walk_image = image("sprites/yeti_walk_sprite (Custom).png");
			idle = new AnimationChannel(image, 7, 80, 80, Duration.seconds(1), 0, 5);
			walk = new AnimationChannel(walk_image, 4, 96, 80, Duration.seconds(0.6), 0, 3);
			jump = new AnimationChannel(image, 7, 80, 80, Duration.seconds(1), 6, 6);
			break;
		case PIG:
			image = image("sprites/pig_sprite (Custom).png");
			idle = new AnimationChannel(image, 8, 80, 80, Duration.seconds(1), 0, 3);
			walk = new AnimationChannel(image, 8, 80, 80, Duration.seconds(0.4), 5, 7);
			jump = new AnimationChannel(image, 8, 80, 80, Duration.seconds(1), 4, 4);
			break;
		case SLIME:
			image = image("sprites/slime_sprite (Custom).png");
			idle = new AnimationChannel(image, 10, 80, 80, Duration.seconds(1), 0, 2);
			walk = new AnimationChannel(image, 10, 80, 80, Duration.seconds(0.6), 4, 9);
			jump = new AnimationChannel(image, 10, 80, 80, Duration.seconds(1), 6, 6);
			break;
		}
		
		texture = new AnimatedTexture(idle);
	}
	
    @Override
    public void onAdded() {
//        entity.getTransformComponent().setScaleOrigin(new Point2D(0.2, 0.2));
        entity.getViewComponent().addChild(texture);
        texture.loop();

        physics.onGroundProperty().addListener((obs, old, isOnGround) -> {
            if (isOnGround) {
                //play("land.wav");
                isJump = false;
            }
        });
    }
    
    @Override
    public void onUpdate(double tpf) {
    	if (isJump) {
    		if (texture.getAnimationChannel() != jump) {
    			texture.loopAnimationChannel(jump);
    		}
    	} 	
    	else if (isMoving()) {
            if (texture.getAnimationChannel() != walk) {
                texture.loopAnimationChannel(walk);
            }
        } else {
            if (texture.getAnimationChannel() != idle) {
                texture.loopAnimationChannel(idle);
            }
        }
    }
	
    public boolean isMoving() {
    	return physics.isMovingX();
    }
    
	public void left() {
		getEntity().setScaleX(1);
		physics.setVelocityX(-200);
	}
	
	public void right() {
		getEntity().setScaleX(-1);
		physics.setVelocityX(200);
	}
	
	public void jump() {
		if (isJump)
			return;
		physics.setVelocityY(-400);
		
		isJump = true;
	}
	
	public void recover() {
		isJump = false;
	}
}
