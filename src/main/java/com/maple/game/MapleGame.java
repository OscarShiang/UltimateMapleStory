package com.maple.game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.physics.PhysicsComponent;

import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.maple.item.ItemType;
import com.maple.player.*;


import static com.almasb.fxgl.dsl.FXGL.*;

public class MapleGame extends GameApplication {
	
	private Entity player;
	private Entity destination;
	private Entity tomb;
	private Entity balloon;
	private Entity teleport1;
	private String IPaddress, Port;
	private boolean isGenTeleport;
	
	@Override
	protected void initSettings(GameSettings settings) {
		// TODO Auto-generated method stub
		settings.setTitle("MapleStory");
		settings.setWidth(1600);
		settings.setHeight(900);
	}
	
	VBox vbox1, vbox2;
	
	protected void initUI() {
		Button create = getUIFactoryService().newButton("CREATE");
        create.setOnAction(e -> {
        	remove();
       	});
        
		Button join = getUIFactoryService().newButton("JOIN");
        join.setOnAction(e -> {
        	remove();
        });
        
		Button quit = getUIFactoryService().newButton("QUIT");
        quit.setOnAction(e -> {
        	System.exit(0);
        });
        
        vbox1 = new VBox(10);
        vbox1.setTranslateX(getAppWidth()/2 - 100);
        vbox1.setTranslateY(400);
        vbox1.getChildren().addAll(
                create, join, quit
        );

        getGameScene().addUINode(vbox1);
	}
	
	@Override
	protected void initInput() {
		getInput().addAction(new UserAction("left") {
			@Override
			protected void onAction() {
				player.getComponent(PlayerComponent.class).left();
			}
		}, KeyCode.A);
		
		getInput().addAction(new UserAction("right") {
			@Override
			protected void onAction() {
				player.getComponent(PlayerComponent.class).right();
			}
		}, KeyCode.D);
		
		getInput().addAction(new UserAction("jump") {
			@Override
			protected void onAction() {
				player.getComponent(PlayerComponent.class).jump();
			}
		}, KeyCode.W);
	}
	
	@Override
	protected void initGame() {
		getGameWorld().addEntityFactory(new MapleFactory());
		spawn("background");
		setLevelFromMap("map1.tmx");
		
		tomb = null;
		
		destination = null;
		destination = getGameWorld().spawn("redflag");
		destination.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(1500, 367));
		
		/*balloon = null;
		balloon = getGameWorld().spawn("balloon");
		balloon.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(435, 413));
		*/
		isGenTeleport = false;
		teleport1 = null;
		teleport1 = getGameWorld().spawn("teleport1");
		//teleport1.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(435, 450));
		
		
		player = null;
		player = getGameWorld().spawn("player", 250, 400);
		player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(250, 400));
		
		Viewport viewport = getGameScene().getViewport();

        viewport.setBounds(-1500, 0, 250 * 70, getAppHeight());
        viewport.bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
        viewport.setLazy(true);
	}
	
	@Override
	protected void initPhysics() {
		getPhysicsWorld().setGravity(0, 800);
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.COIN) {
			@Override
			public void onCollisionBegin(Entity player, Entity coin) {
				player.setOpacity(0);
				player.getComponent(PlayerComponent.class).dead();
				coin.removeFromWorld();
				player.setVisible(false);
				player.removeComponent(PhysicsComponent.class);
				deadTomb();
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.PLATFORM) {
			@Override
			public void onCollisionBegin(Entity player, Entity platform) {
				player.getComponent(PlayerComponent.class).recover();
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.DEADLINE) {
			@Override
			public void onCollisionBegin(Entity player, Entity deadline) {
				player.setOpacity(0);
				player.getComponent(PlayerComponent.class).dead();
				
				deadTomb();
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER,  MapleType.ITEM) {
			public void onCollisionBegin(Entity player, Entity redflag) {
				player.getComponent(PlayerComponent.class).win();
				getDialogService().showMessageBox("Finish!");
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.TOMB, MapleType.PLATFORM) {
			public void onCollisionBegin(Entity tomb, Entity platform) {
				getDialogService().showMessageBox("You died...");
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.TOMB, MapleType.DEADLINE) {
			public void onCollisionBegin(Entity tomb, Entity platform) {
				getDialogService().showMessageBox("You died...");
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.TOMB, MapleType.PLAYER) {
			public void onCollisionBegin(Entity tomb, Entity platform) {
				getDialogService().showMessageBox("You died...");
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.TELEPORT1) {
			public void onCollisionBegin(Entity player, Entity teleport1) {
			
				//player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(50, 50));
				
			}
		});
	}
	
	public void deadTomb() {
		tomb = getGameWorld().spawn("tomb");
		tomb.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(player.getX(), 0));
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.PLATFORM) {
			@Override
			public void onCollisionBegin(Entity player, Entity balloon) {
				player.getComponent(PlayerComponent.class).recover();
			}
		});
	}
	
	protected void onUpdate() {
		if(!isGenTeleport) {
			teleport1 = null;
			teleport1 = getGameWorld().spawn("teleport1");
			teleport1.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(435, 450));
			isGenTeleport = true;
			Duration.seconds(2);
		}
		else if(isGenTeleport) {
			teleport1.removeFromWorld();
			isGenTeleport = false;
			Duration.seconds(2);
		}
	}

	protected void type() {
		TextField ip = new TextField();
		ip.setPromptText("IP");
		TextField port = new TextField();
		port.setPromptText("port");
		ip.setFont(Font.font(15));
		port.setFont(Font.font(15));
		
		Button ok = getUIFactoryService().newButton("OK");
        ok.setOnAction(e -> {
        	if(ip.getText().isEmpty() || port.getText().isEmpty()) {
        		ip.clear();
        		port.clear();
        	}
        	else {
        		IPaddress = ip.getText();
        		Port = port.getText();
        		System.out.println(ip.getText());
        		getGameScene().removeUINode(vbox2);
        	}
        });
        
        Button back = getUIFactoryService().newButton("BACK");
        back.setOnAction(e -> {
        	getGameScene().removeUINode(vbox2);
        	getGameScene().addUINode(vbox1);
        });
        
        vbox2 = new VBox(10);
        //vbox2.setBackground();
        vbox2.setTranslateX(getAppWidth()/2 - 100);
        vbox2.setTranslateY(400);
        vbox2.getChildren().addAll(
                ip, port, ok, back
        );
        
        getGameScene().addUINode(vbox2);
	}
	
	protected void remove() {
		getGameScene().removeUINode(vbox1);
		type();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
