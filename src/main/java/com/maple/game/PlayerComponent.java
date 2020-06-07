package com.maple.game;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlayerComponent extends Component {
	private PhysicsComponent physics;
	private boolean isJump;
	
	private AnimatedTexture texture;
	private AnimationChannel idle, walk, jump;
	
	public PlayerComponent() {
		isJump = false;
		
		Image image = image("resize/slime_sprite (Custom).png");
		
		idle = new AnimationChannel(image, 10, 100, 100, Duration.seconds(3), 0, 2);
		walk = new AnimationChannel(image, 10, 100, 100, Duration.seconds(1), 3, 9);
		
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
        if (isMoving()) {
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
		physics.setVelocityX(-150);
	}
	
	public void right() {
		getEntity().setScaleX(-1);
		physics.setVelocityX(150);
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
