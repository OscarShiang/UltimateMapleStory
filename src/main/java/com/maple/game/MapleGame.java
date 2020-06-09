package com.maple.game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.physics.PhysicsComponent;

import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.maple.item.ItemType;
import com.maple.player.*;


import static com.almasb.fxgl.dsl.FXGL.*;

public class MapleGame extends GameApplication {
	
	private Entity player; // main player (No COPY)
	private Entity yeti, mushroom, slime, pig;
	
	private Entity destination;
	private Entity tomb;
	private Entity balloon;
	
	// GUI input
	private String IPaddress, Port;
	
	private int[] score;
	
	@Override
	protected void initSettings(GameSettings settings) {
		// TODO Auto-generated method stub
		settings.setTitle("MapleStory");
		settings.setWidth(1600);
		settings.setHeight(900);
		
		score = new int[4];
	}
	
	VBox vbox1, vbox2;
	Pane pane;
	
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
        
        //getGameScene().addUINode(vbox1);

        Button mushroom = new Button("", new ImageView(image("sprites/mushroom_sprite (Custom).png")));
        mushroom.setOnAction(e -> {
        	
        });
        mushroom.setPrefSize(100, 100);
        mushroom.setTranslateX(150);
        mushroom.setTranslateY(150);
//        mushroom.setStyle("-fx-background-color: transparent;"); // transparent background color
        
        pane = new Pane();
        pane.setBackground(new Background(new BackgroundImage(image("background/book.png"), null, null, null, null)));
        pane.setTranslateX(getAppWidth()/2 - 520);
        pane.setTranslateY(getAppHeight()/2 - 350);
        pane.setPrefSize(1040, 700);
        pane.getChildren().addAll(mushroom);
        getGameScene().addUINodes(pane);
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
		destination.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(1435, 413));
		
		balloon = null;
		balloon = getGameWorld().spawn("balloon");
		balloon.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(435, 413));
		
		player = null;
		player = getGameWorld().spawn("player");
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
	}
	
	public void deadTomb() {
		tomb = getGameWorld().spawn("tomb");
		tomb.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(player.getX(), 200));
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.PLATFORM) {
			@Override
			public void onCollisionBegin(Entity player, Entity balloon) {
				player.getComponent(PlayerComponent.class).recover();
			}
		});
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
	
	// interfaces of updating networking information
	public void setScore(int score, int clientNum) {
		this.score[clientNum] = score;
	}
	
	private void setPlayer(Entity player, PlayerComponent component) {
		player.getComponent(PlayerComponent.class).physics = component.physics;
		player.getComponent(PlayerComponent.class).isJump = component.isJump;
		player.getComponent(PlayerComponent.class).isDead = component.isDead;
		player.getComponent(PlayerComponent.class).isWin = component.isWin;
		player.getComponent(PlayerComponent.class).texture = component.texture;
	}
	
	public void setYeti(PlayerComponent component) {
		setPlayer(yeti, component);
	}
	
	public void setSlime(PlayerComponent component) {
		setPlayer(slime, component);
	}
	
	public void setPig(PlayerComponent component) {
		setPlayer(pig, component);
	}
	
	public void setMushroom(PlayerComponent component) {
		setPlayer(mushroom, component);
	}
	
	
	
	public static void main(String[] args) {
		launch(args);
	}

}
