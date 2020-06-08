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
			idle = new AnimationChannel(image, 1, 143, 171, Duration.seconds(1), 0, 0);
			break;
		}
		
		texture = new AnimatedTexture(idle);
	}
	
	public void onAdded() {
		entity.getViewComponent().addChild(texture);
		texture.loop();
	}
	
}
