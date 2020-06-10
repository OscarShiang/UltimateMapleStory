package com.maple.item;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.maple.game.MapleGame;

import javafx.scene.input.MouseEvent;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class GroundComponent extends Component {

    private ViewComponent view;
    private String currentTextureName = "lever0.png";
    
    Input input = getInput();

    private MapleGame game;
    
    public GroundComponent(MapleGame game) {
    	this.game = game;
    }
    
    @Override
    public void onAdded() {
    	view.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> trigger());
    }

    public void trigger() {
    	if (!game.canPlace)
    		return;
    	
    	if(game.chooseItem > 0 && game.item == 1) {
	    	game.balloon = null;
			game.balloon = getGameWorld().spawn("balloon");
			game.balloon.getComponent(PhysicsComponent.class).overwritePosition(input.getMousePositionWorld());
    	}
    	else if(game.chooseItem > 0 && game.item == 2) {
	    	game.hole = null;
			game.hole = getGameWorld().spawn("hole");
			game.hole.getComponent(PhysicsComponent.class).overwritePosition(input.getMousePositionWorld());
    	}
    	else if(game.chooseItem > 0 && game.item == 3) {
	    	game.surprise = null;
			game.surprise = getGameWorld().spawn("surprise");
			game.surprise.getComponent(PhysicsComponent.class).overwritePosition(input.getMousePositionWorld());
    	}
    	else if(game.chooseItem > 0 && game.item == 4) {
    		if(game.balloon.isWithin(new Rectangle2D(input.getMouseXWorld()-100, input.getMouseYWorld()-100, 200, 200))) {
    			game.balloon.removeFromWorld();	
    		}
    		else if(game.hole.isWithin(new Rectangle2D(input.getMouseXWorld()-100, input.getMouseYWorld()-100, 200, 200))) {
    			game.hole.removeFromWorld();	
    		}
    		else if(game.surprise.isWithin(new Rectangle2D(input.getMouseXWorld()-100, input.getMouseYWorld()-100, 200, 200))) {
    			game.surprise.removeFromWorld();
    		}
    		else if(game.brick.isWithin(new Rectangle2D(input.getMouseXWorld()-100, input.getMouseYWorld()-100, 200, 200))) {
    			game.brick.removeFromWorld();	
    		}
    	}
    	else if(game.chooseItem > 0 && game.item == 5) {
	    	game.brick = null;
			game.brick = getGameWorld().spawn("brick");
			game.brick.getComponent(PhysicsComponent.class).overwritePosition(input.getMousePositionWorld());
    	}
    	
    	System.out.println(game.chooseItem);
    	game.chooseItem--;
    	game.placeItem();
    }
}