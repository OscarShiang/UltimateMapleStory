package com.maple.player;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

import java.io.Serializable;

import com.maple.game.*;
import com.maple.item.*;

public class PlayerComponent extends Component {
	
	public PhysicsComponent physics;
	public boolean isJump;
	public boolean isDead;
	public boolean isWin;
	
	public int playerNum;
	
	public AnimatedTexture texture;
	private AnimationChannel idle, walk, jump;
	
	public PlayerInfo info;

	// default
	public PlayerComponent() {
		isJump = false;
		isDead = false;
		isWin = false;
		
		Image image = image("sprites/mushroom_sprite (Custom).png");
		
		idle = new AnimationChannel(image, 5, 80, 80, Duration.seconds(1), 0, 1);
		walk = new AnimationChannel(image, 5, 80, 80, Duration.seconds(0.6), 2, 4);
		jump = new AnimationChannel(image, 5, 80, 80, Duration.seconds(1), 3, 3);
		
		texture = new AnimatedTexture(idle);
		
		physics = entity.getComponent(PhysicsComponent.class);
	}
	
	// custom
	public PlayerComponent(PlayerType type) {
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
			walk = new AnimationChannel(image, 8, 80, 80, Duration.seconds(0.6), 5, 7);
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
        
        info = new PlayerInfo();

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
    	
    	info.x = entity.getX();
    	info.y = entity.getY();
    	info.scale = entity.getScaleX();
    }
	
    public boolean isMoving() {
    	return physics.isMovingX();
    }
    
    public void left() {
		if(!isDead && !isWin) {
			getEntity().setScaleX(1);
			physics.setVelocityX(-200);
		}
		else
			physics.setVelocityX(0);
	}
	
	public void right() {
		if(!isDead && !isWin) {
			getEntity().setScaleX(-1);
			physics.setVelocityX(200);
		}
		else
			physics.setVelocityX(0);
	}
	
	public void jump() {
		if (isJump || isDead || isWin)
			return;
		physics.setVelocityY(-400);
		
		isJump = true;
	}
	
	public void start() {
		isDead = false;
	}
	
	public void dead() {
		physics.setVelocityX(0);
		isDead = true;
	}
	
	public void win() {
		physics.setVelocityX(0);
		isWin = true;
	}
	
	public void restore() {
		isDead = false;
		isWin = false;
	}
	
	public void recover() {
		isJump = false;
	}
}
