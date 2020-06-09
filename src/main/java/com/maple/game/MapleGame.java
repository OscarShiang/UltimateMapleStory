package com.maple.game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.physics.PhysicsComponent;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
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
import javafx.util.Duration;
import javafx.scene.text.Text;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.maple.item.ItemType;
import com.maple.mouse.Mouse;
import com.maple.player.*;

import static com.almasb.fxgl.dsl.FXGL.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.maple.item.ItemType;
import com.maple.player.*;
import com.maple.networking.*;

public class MapleGame extends GameApplication {
	
	private PlayerType playerType;
	private Entity player; // main player (No COPY)
	private Entity yeti, mushroom, slime, pig;
	
	private Entity destination;
	private Entity tomb;

	private Entity teleport1;
	private boolean isGenTeleport;

	private String IPaddress, Port;
	public boolean isChoose = false;
	public int item = 0;
	public Entity balloon;
    public Entity hole;
    public Entity surprise;
	
	// current progress
	private MapleStage stage;
	
	boolean isHost, isClient;
	
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
	Pane pane, rank;

	// network instances
	private Server server;
	private Client client;
	
	VBox menuBox, hostBox, clientBox;
	
	protected void initUI() {
		// setting up menuBox
		Button create = getUIFactoryService().newButton("CREATE");
        create.setOnAction(e -> {
        	stage = MapleStage.WAIT;
        	getGameScene().removeUINode(menuBox);
        	getGameScene().addUINode(hostBox);
        	
        	try {
				server = new Server(this);
			} catch (IOException e1) {
				System.out.println("[SERVER] can not create a server");
				e1.printStackTrace();
			}
       	});
        
		Button join = getUIFactoryService().newButton("JOIN");
        join.setOnAction(e -> {
        	stage = MapleStage.WAIT;
        	getGameScene().removeUINode(menuBox);
        	getGameScene().addUINode(clientBox);
        });
        
		Button quit = getUIFactoryService().newButton("QUIT");
        quit.setOnAction(e -> {
        	System.exit(0);
        });
        
        menuBox = new VBox(10);
        menuBox.setTranslateX(getAppWidth()/2 - 100);
        menuBox.setTranslateY(400);
        menuBox.getChildren().addAll(
                create, join, quit
        );
        
        // setting up clientBox
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
        		boolean fail = false;
        		
        		try {
					client = new Client(this, ip.getText(), Integer.parseInt(port.getText(), 10));
				} catch (NumberFormatException | IOException e1) {
					getDialogService().showMessageBox("Connection failed");
					e1.printStackTrace();
					fail = true;
				}
        		
        		if (!fail)
        			getGameScene().removeUINode(clientBox);
        	}
        });
        
        Button back = getUIFactoryService().newButton("BACK");
        back.setOnAction(e -> {
        	getGameScene().removeUINode(clientBox);
        	getGameScene().addUINode(menuBox);
        });
        
        clientBox = new VBox(10);
        clientBox.setTranslateX(getAppWidth()/2 - 100);
        clientBox.setTranslateY(400);
        clientBox.getChildren().addAll(
                ip, port, ok, back
        );
        
        // setting up hostBox
        InetAddress ip_addr = null;
		try {
			ip_addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) { e1.printStackTrace(); }
        
        Text host_ip = new Text();
        Text host_port = new Text();
        
        host_ip.setFont(Font.font(30));
        host_port.setFont(Font.font(30));
        
        host_ip.setText(ip_addr.getHostAddress());
        host_port.setText(Server.DEFAULT_PORT.toString());
        
        Button host_back = getUIFactoryService().newButton("BACK");
        host_back.setOnAction(e -> {
        	getGameScene().removeUINode(hostBox);
        	getGameScene().addUINode(menuBox);
        	
        	server = null;
        });
        
        hostBox = new VBox(10);
        hostBox.setTranslateX(getAppWidth()/2 - 100);
        hostBox.setTranslateY(400);
        hostBox.getChildren().addAll(
                host_ip, host_port, host_back
        );
        
        // initial show up
//        getGameScene().addUINode(menuBox);

        
        Button redballoon = new Button("", new ImageView(image("item/balloon.png")));
        redballoon.setOnAction(e -> {
        	pane.setVisible(false);
        	player.getComponent(PlayerComponent.class).start();
        	isChoose = true;
        	item = 1;
        });
        redballoon.setTranslateX(150);
        redballoon.setTranslateY(150);
        
        Button hole = new Button("", new ImageView(image("item/hole.png")));
        hole.setOnAction(e-> {
        	pane.setVisible(false);
        	player.getComponent(PlayerComponent.class).start();
        	isChoose = true;
        	item = 2;
        });
        hole.setTranslateX(300);
        hole.setTranslateY(150);
        
        Button surprise = new Button("", new ImageView(image("item/surprise.png")));
        surprise.setOnAction(e-> {
        	pane.setVisible(false);
        	player.getComponent(PlayerComponent.class).start();
        	isChoose = true;
        	item = 3;
        });
        surprise.setTranslateX(600);
        surprise.setTranslateY(150);
        
        pane = new Pane();
        pane.setBackground(new Background(new BackgroundImage(image("background/book.png"), null, null, null, null)));
        pane.setTranslateX(getAppWidth()/2 - 520);
        pane.setTranslateY(getAppHeight()/2 - 350);
        pane.setPrefSize(1040, 700);
        pane.getChildren().addAll(redballoon);
        pane.getChildren().addAll(hole);
        pane.getChildren().addAll(surprise);
        //getGameScene().addUINodes(pane);
        
        rank = new Pane();
        rank.setBackground(new Background(new BackgroundImage(image("background/rank.png"), null, null, null, null)));
        rank.setTranslateX(getAppWidth() / 2 - 350);
        rank.setTranslateY(getAppHeight() / 2 - 250);
        rank.setPrefSize(700, 500);
        getGameScene().addUINode(rank);
	}
	
	@Override
	protected void initInput() {
		getInput().addAction(new UserAction("left") {
			@Override
			protected void onAction() {
				if (stage == MapleStage.PLAY)
					return;
				player.getComponent(PlayerComponent.class).left();
			}
		}, KeyCode.A);
		
		getInput().addAction(new UserAction("right") {
			@Override
			protected void onAction() {
				if (stage == MapleStage.PLAY)
					return;
				player.getComponent(PlayerComponent.class).right();
			}
		}, KeyCode.D);
		
		getInput().addAction(new UserAction("jump") {
			@Override
			protected void onAction() {
				if (stage == MapleStage.PLAY)
					return;
				player.getComponent(PlayerComponent.class).jump();
			}
		}, KeyCode.W);
	}
	
	@Override
	protected void initGame() {
		getGameWorld().addEntityFactory(new MapleFactory(this));
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
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.TRAP) {
			@Override
			public void onCollisionBegin(Entity player, Entity hole) {
				player.setOpacity(0);
				player.getComponent(PlayerComponent.class).dead();

				deadTomb();
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.TRAP) {
			@Override
			public void onCollisionBegin(Entity player, Entity surprise) {
				player.setOpacity(0);
				player.getComponent(PlayerComponent.class).dead();

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
			public void onCollisionBegin(Entity tomb, Entity deadline) {
				getDialogService().showMessageBox("You died...");
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.TOMB, MapleType.PLAYER) {
			public void onCollisionBegin(Entity tomb, Entity player) {
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
		tomb.setPosition(new Point2D(player.getX(), 200));
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
	
	public PlayerType getPlayerType() {
		return playerType;
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}

}
