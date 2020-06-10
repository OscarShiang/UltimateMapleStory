package com.maple.player;

import com.almasb.fxgl.entity.component.Component;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.image;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class tmpPlayerComponent extends Component {

    private PhysicsComponent physics;

    private AnimatedTexture texture;

    private AnimationChannel idle, walk, jump;
    
    public boolean isJump;
	public boolean isDead;
	public boolean isWin;


    public tmpPlayerComponent() {
    	isJump = false;
		isDead = true;
		isWin = false;
    	
        Image image = image("sprites/mushroom_sprite (Custom).png");

        idle = new AnimationChannel(image, 5, 100, 100, Duration.seconds(1), 0, 1);
		walk = new AnimationChannel(image, 5, 100, 100, Duration.seconds(0.6), 2, 4);
		jump = new AnimationChannel(image, 5, 100, 100, Duration.seconds(1), 3, 3);
		
		texture = new AnimatedTexture(idle);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        entity.getViewComponent().addChild(texture);

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

    private boolean isMoving() {
        return physics.isMovingX();
    }

    public void left() {
        getEntity().setScaleX(-1);
        physics.setVelocityX(-170);
    }

    public void right() {
        getEntity().setScaleX(1);
        physics.setVelocityX(170);
    }

    public void stop() {
        physics.setVelocityX(0);
    }

    public void jump() {
    	if (isJump || isDead || isWin)
			return;
		physics.setVelocityY(-400);
		
		isJump = true;
    }
    
    public void recover() {
		isJump = false;
	}
}
