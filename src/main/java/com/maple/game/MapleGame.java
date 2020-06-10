package com.maple.game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;

import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import com.almasb.fxgl.entity.Entity;
import com.maple.player.*;

import static com.almasb.fxgl.dsl.FXGL.*;

public class MapleGame extends GameApplication {
	
	private Entity[] player;

	public Entity yeti, mushroom, slime, pig;
	
	private Entity destination;
	private Entity tomb;
	private boolean realDead;
	
	private Entity teleport1;
	private boolean isGenTeleport;
	
	public boolean isChoose = false;
	public int item = 0;
	public Entity balloon;
    public Entity hole;
    public Entity surprise;
    
    private final int PLAYER_NUM = 2;
	
	// current progress
	private MapleStage stage;
	
	private int[] score;
	
	@Override
	protected void initSettings(GameSettings settings) {
		settings.setTitle("Ultimate MapleStory");
		settings.setWidth(1600);
		settings.setHeight(900);
		
		score = new int[PLAYER_NUM];
		for(int i = 0; i < PLAYER_NUM; i++)
			score[i] = 0;
		
		chosenPlayer = 0;
	}
	

	Pane pane, rank;
	
	VBox menuBox, selectBox;
	
	private int chosenPlayer = 0;
	
	protected void initUI() {
		player = new Entity[PLAYER_NUM];
		
		// setting up menuBox
		Button start = getUIFactoryService().newButton("START");
		start.setOnAction(e -> {
        	getGameScene().removeUINode(menuBox);
        	getGameScene().addUINode(selectBox);
        });
        
		Button quit = getUIFactoryService().newButton("QUIT");
        quit.setOnAction(e -> {
        	System.exit(0);
        });
        
        menuBox = new VBox(10);
        menuBox.setTranslateX(getAppWidth()/2 - 100);
        menuBox.setTranslateY(400);
        menuBox.getChildren().addAll(
                start, quit
        );
        
        // setting up select box
        Button select_yeti = getUIFactoryService().newButton("Yeti");
        select_yeti.setOnAction(e -> {
        	System.out.println(chosenPlayer);
        	player[chosenPlayer] = getGameWorld().spawn("yeti");
        	if (++chosenPlayer >= 2) {
        		getGameScene().removeUINode(selectBox);
        		stage = MapleStage.PLAY;
        		getGameScene().removeUINode(selectBox);
        		getGameScene().addUINodes(pane);
        	}
        });
        Button select_pig = getUIFactoryService().newButton("Pig");
        select_pig.setOnAction(e -> {
        	System.out.println(chosenPlayer);
        	player[chosenPlayer] = getGameWorld().spawn("pig");
        	if (++chosenPlayer >= 2) {
        		getGameScene().removeUINode(selectBox);
        		stage = MapleStage.SELECT;
        		getGameScene().removeUINode(selectBox);
        		getGameScene().addUINodes(pane);
        	}
        });
        Button select_slime = getUIFactoryService().newButton("Slime");
        select_slime.setOnAction(e -> {
        	System.out.println(chosenPlayer);
        	player[chosenPlayer] = getGameWorld().spawn("slime");
        	if (++chosenPlayer >= 2) {
        		getGameScene().removeUINode(selectBox);
        		stage = MapleStage.SELECT;
        		getGameScene().removeUINode(selectBox);
        		getGameScene().addUINodes(pane);
        	}
        });
        Button select_mushroom = getUIFactoryService().newButton("Mushroom");
        select_mushroom.setOnAction(e -> {
        	System.out.println(chosenPlayer);
        	player[chosenPlayer] = getGameWorld().spawn("mushroom");
        	if (++chosenPlayer >= 2) {
        		getGameScene().removeUINode(selectBox);
        		stage = MapleStage.SELECT;
        		getGameScene().removeUINode(selectBox);
        		getGameScene().addUINodes(pane);
        	}
        });
        Button select_back = getUIFactoryService().newButton("BACK");
        select_back.setOnAction(e -> {
        	getGameScene().removeUINode(selectBox);
        	getGameScene().addUINode(menuBox);
        	
        	chosenPlayer = 0;
        	for (int i = 0; i < PLAYER_NUM; i++) {
        		player[i] = null;
        	}
        });
        
        selectBox = new VBox(10);
        selectBox.setTranslateX(getAppWidth()/2 - 100);
        selectBox.setTranslateY(400);
        selectBox.getChildren().addAll(
        		select_yeti, select_pig, select_slime, select_mushroom, select_back
        );
        
        // initial show up
         getGameScene().addUINode(menuBox);

        
        Button redballoon = new Button("", new ImageView(image("item/balloon.png")));
        redballoon.setStyle("-fx-background-color: transparent;");
        redballoon.setOnAction(e -> {
        	pane.setVisible(false);
        	isChoose = true;
        	item = 1;
        });
        redballoon.setTranslateX(150);
        redballoon.setTranslateY(150);
        
        Button hole = new Button("", new ImageView(image("item/hole.png")));
        hole.setStyle("-fx-background-color: transparent;");
        hole.setOnAction(e-> {
        	pane.setVisible(false);
        	isChoose = true;
        	item = 2;
        });
        hole.setTranslateX(300);
        hole.setTranslateY(150);
        
        Button surprise = new Button("", new ImageView(image("item/surprise.png")));
        surprise.setStyle("-fx-background-color: transparent;");
        surprise.setOnAction(e-> {
        	pane.setVisible(false);
        	isChoose = true;
        	item = 3;
        });
        surprise.setTranslateX(600);
        surprise.setTranslateY(150);
        
        Button bomb = new Button("", new ImageView(image("item/bomb.png")));
        bomb.setOnAction(e-> {
        	pane.setVisible(false);
        	isChoose = true;
        	item = 4;
        });
        bomb.setTranslateX(150);
        bomb.setTranslateY(400);
        
        Button brick = new Button("", new ImageView(image("item/brick.png")));
        brick.setOnAction(e-> {
        	pane.setVisible(false);
        	isChoose = true;
        	item = 5;
        });
        brick.setTranslateX(300);
        brick.setTranslateY(400);
        
        pane = new Pane();
        pane.setBackground(new Background(new BackgroundImage(image("background/book.png"), null, null, null, null)));
        pane.setTranslateX(getAppWidth()/2 - 520);
        pane.setTranslateY(getAppHeight()/2 - 350);
        pane.setPrefSize(1040, 700);
        pane.getChildren().addAll(redballoon);
        pane.getChildren().addAll(hole);
        pane.getChildren().addAll(surprise);
        pane.getChildren().addAll(bomb);
        pane.getChildren().addAll(brick);
//        getGameScene().addUINodes(pane);
        
        rank = new Pane();
        rank.setBackground(new Background(new BackgroundImage(image("background/rank.png"), null, null, null, null)));
        rank.setTranslateX(getAppWidth() / 2 - 350);
        rank.setTranslateY(getAppHeight() / 2 - 250);
        rank.setPrefSize(700, 500);
        
        /* need to be modified

        Text scoreMushroom = new Text((Integer.toString(score[0])));
        scoreMushroom.setTranslateX(550);
        scoreMushroom.setTranslateY(125);
        scoreMushroom.setFont(Font.font(25));
        Text scoreYeti = new Text((Integer.toString(score[1])));
        scoreYeti.setTranslateX(550);
        scoreYeti.setTranslateY(215);
        scoreYeti.setFont(Font.font(25));
        Text scoreSlime = new Text((Integer.toString(score[2])));
        scoreSlime.setTranslateX(550);
        scoreSlime.setTranslateY(305);
        scoreSlime.setFont(Font.font(25));
        Text scorePig = new Text((Integer.toString(score[3])));
        scorePig.setTranslateX(550);
        scorePig.setTranslateY(395);
        scorePig.setFont(Font.font(25));
        rank.getChildren().addAll(scoreMushroom, scoreYeti, scoreSlime, scorePig);
        //getGameScene().addUINode(rank);
        
        */
        
	}
	
	@Override
	protected void initInput() {
		getInput().addAction(new UserAction("left_P1") {
			@Override
			protected void onAction() {
				if (stage != MapleStage.PLAY)
					return;
				
				player[0].getComponent(PlayerComponent.class).left();
			}
		}, KeyCode.A);
		
		getInput().addAction(new UserAction("right_P1") {
			@Override
			protected void onAction() {
				if (stage != MapleStage.PLAY)
					return;
				player[0].getComponent(PlayerComponent.class).right();
			}
		}, KeyCode.D);
		
		getInput().addAction(new UserAction("jump_P1") {
			@Override
			protected void onAction() {
				if (stage != MapleStage.PLAY)
					return;
				player[0].getComponent(PlayerComponent.class).jump();
			}
		}, KeyCode.W);
		
		getInput().addAction(new UserAction("left_P2") {
			@Override
			protected void onAction() {
				if (stage != MapleStage.PLAY)
					return;
				
				player[1].getComponent(PlayerComponent.class).left();
			}
		}, KeyCode.LEFT);
		
		getInput().addAction(new UserAction("right_P2") {
			@Override
			protected void onAction() {
				if (stage != MapleStage.PLAY)
					return;
				player[1].getComponent(PlayerComponent.class).right();
			}
		}, KeyCode.RIGHT);
		
		getInput().addAction(new UserAction("jump_P2") {
			@Override
			protected void onAction() {
				if (stage != MapleStage.PLAY)
					return;
				player[1].getComponent(PlayerComponent.class).jump();
			}
		}, KeyCode.UP);
	}
	
	public void startGame() {
		Viewport viewport = getGameScene().getViewport();

        viewport.setBounds(-1500, 0, 250 * 70, getAppHeight());
        viewport.bindToEntity(player[0], getAppWidth() / 2, getAppHeight() / 2);
        viewport.setLazy(true);
        
        stage = MapleStage.PLAY;
	}
	
	@Override
	protected void initGame() {
		getGameWorld().addEntityFactory(new MapleFactory(this));
		spawn("background");
		setLevelFromMap("map1.tmx");
		tomb = null;
		realDead = false;
		
		destination = null;
		destination = getGameWorld().spawn("redflag", new Point2D(1435, 413));
		
		isGenTeleport = false;
		teleport1 = getGameWorld().spawn("teleport1", new Point2D(470, 380));
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
				deadTomb(player);
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.TRAP) {
			@Override
			public void onCollisionBegin(Entity player, Entity hole) {
				player.setOpacity(0);
				player.getComponent(PlayerComponent.class).dead();

				deadTomb(player);
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.TRAP) {
			@Override
			public void onCollisionBegin(Entity player, Entity surprise) {
				player.setOpacity(0);
				player.getComponent(PlayerComponent.class).dead();

				deadTomb(player);
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.PLATFORM) {
			@Override
			public void onCollisionBegin(Entity player, Entity platform) {
				player.getComponent(PlayerComponent.class).recover();
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.PLAYER) {
			@Override
			public void onCollisionBegin(Entity p1, Entity p2) { }
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.DEADLINE) {
			@Override
			public void onCollisionBegin(Entity player, Entity deadline) {
				player.setOpacity(0);
				player.getComponent(PlayerComponent.class).dead();
				deadTomb(player);
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
				player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(100, 100));
				//teleport();
			}
		});
	}
	
	/*public void teleport() {
		teleportPos();
	}
	
	public void teleportPos() {
		if (player != null) {
			//player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(100, 100));
			player.setZ(Integer.MAX_VALUE);
		}
	}*/
	
	public void deadTomb(Entity player) {
		if (!realDead) {
			tomb = getGameWorld().spawn("tomb");
			tomb.setPosition(new Point2D(player.getX(), 0));
			realDead = true;
		}
	}

	
	public static void main(String[] args) {
		launch(args);
	}

}
