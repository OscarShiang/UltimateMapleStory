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
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class GroundComponent  extends Component {

    private ViewComponent view;
    private String currentTextureName = "lever0.png";
    private Entity balloon;
    private Entity hole;
    private Entity surprise;
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
    	if(game.isChoose == true && game.item == 1) {
	    	balloon = null;
			balloon = getGameWorld().spawn("balloon");
			balloon.getComponent(PhysicsComponent.class).overwritePosition(input.getMousePositionWorld());
    	}
    	else if(game.isChoose == true && game.item == 2) {
	    	hole = null;
			hole = getGameWorld().spawn("hole");
			hole.getComponent(PhysicsComponent.class).overwritePosition(input.getMousePositionWorld());
    	}
    	else if(game.isChoose == true && game.item == 3) {
	    	surprise = null;
			surprise = getGameWorld().spawn("surprise");
			surprise.getComponent(PhysicsComponent.class).overwritePosition(input.getMousePositionWorld());
    	}   
		game.isChoose = false;
        
    }
}