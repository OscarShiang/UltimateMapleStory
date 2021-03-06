package com.maple.item;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.*;

import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class ItemComponent extends Component {
	private PhysicsComponent physics;
	private AnimatedTexture texture;
	private AnimationChannel idle;
	
	public ItemComponent() {
		Image image = image("item/sculpture.png");
		idle = new AnimationChannel(image, 1, 84, 100, Duration.seconds(1), 0, 0);
		
		texture = new AnimatedTexture(idle);
	}
	
	public ItemComponent(ItemType type) {
		Image image;
		switch(type) {
			case redflag:
				image = image("item/sculpture.png");
				idle = new AnimationChannel(image, 1, 84, 100, Duration.seconds(1), 0, 0);
				break;
			case tomb:
				image = image("item/tomb.png");
				idle = new AnimationChannel(image, 1, 94, 43, Duration.seconds(1), 0, 0);
				break;
			case coin:
				image = image("item/coin_sprite.png");
				idle = new AnimationChannel(image, 4, 25, 25, Duration.seconds(0.4), 0, 3);
				break;
			case balloon:
				image = image("item/balloon.png");
				idle = new AnimationChannel(image, 1, 71, 113, Duration.seconds(1), 0, 0);
				break;
			case teleport1:
				image = image("item/teleport1.png");
				idle = new AnimationChannel(image, 1, 47, 78, Duration.seconds(1), 0, 0);
				break;
			case hole:
				image = image("item/hole_sprite.png");
				idle = new AnimationChannel(image, 4, 105, 105, Duration.seconds(0.4), 0, 3);
				break;
			case surprise:
				image = image("item/prianha_sprite.png");
				idle = new AnimationChannel(image, 12, 255, 119, Duration.seconds(2), 0, 11);
				break;
			case brick:
				image = image("item/brick.png");
				idle = new AnimationChannel(image, 1, 110, 66, Duration.seconds(1), 0, 0);
				break;
			case teleport2:
				image = image("item/teleport2.png");
				idle = new AnimationChannel(image, 1, 47, 78, Duration.seconds(1), 0, 0);
				break;
		}
		
		texture = new AnimatedTexture(idle);
	}
	
	public void onAdded() {
		entity.getViewComponent().addChild(texture);
		texture.loop();
	}
	
}
