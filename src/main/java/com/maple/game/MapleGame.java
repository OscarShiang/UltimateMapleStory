package com.maple.game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.Texture;

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

	
	private Entity destination;
	private Entity tomb;
	private boolean realDead;
	
	private Entity teleport1;
	private Entity teleport2;
	
	public int chooseItem = 0;
	public int item = 0;
	public Entity balloon;
    public Entity hole;
    public Entity surprise;
    public Entity brick;
    
    private final int PLAYER_NUM = 2;
	
	// current progress
	private MapleStage stage;
	
	private int[] score;
	private String[] choosePlayer;	
	private Text[] scoreText;
	
	public boolean canPlace;
	
	@Override
	protected void initSettings(GameSettings settings) {
		settings.setTitle("Ultimate MapleStory");
		settings.setWidth(1600);
		settings.setHeight(900);
		
		score = new int[PLAYER_NUM];
		for(int i = 0; i < PLAYER_NUM; i++)
			score[i] = 0;
		
		choosePlayer = new String[PLAYER_NUM];
		for(int i = 0; i < 2; i++)
			choosePlayer[i] = null;
		
		
		player = new Entity[PLAYER_NUM];
		
		chosenPlayer = 0;
		chooseItem = 0;
	}
	

	Pane pane, rank;
	
	VBox menuBox, selectBox;
	
	private int chosenPlayer;
	
	protected void initUI() {
		player = new Entity[PLAYER_NUM];
		
		Texture title = FXGL.getAssetLoader().loadTexture("item/title.png");
		title.setScaleX(0.5);
		title.setScaleY(0.5);
		title.setX(100);
		title.setY(50);
		getGameScene().addUINode(title);
		
		
		// setting up menuBox
		Button start = getUIFactoryService().newButton("START");
		start.setOnAction(e -> {
        	getGameScene().removeUINode(menuBox);
        	getGameScene().addUINode(selectBox);
        	getGameScene().removeUINode(title);
        });
        
		Button quit = getUIFactoryService().newButton("QUIT");
        quit.setOnAction(e -> System.exit(0));
        
        menuBox = new VBox(10);
        menuBox.setTranslateX(getAppWidth()/2 - 100);
        menuBox.setTranslateY(500);
        menuBox.getChildren().addAll(
        		start, quit
        );
        
        
        rank = new Pane();
        rank.setBackground(new Background(new BackgroundImage(image("background/rank_background.png"), null, null, null, null)));
        rank.setTranslateX(getAppWidth() / 2 - 350);
        rank.setTranslateY(getAppHeight() / 2 - 250);
        rank.setPrefSize(700, 500);
        
        int i = 1;
        Texture red  = new Texture(image("item/red.png"));
        Texture green  = new Texture(image("item/green.png"));
        Texture blue  = new Texture(image("item/blue.png"));    
        Texture orange  = new Texture(image("item/orange.png"));
 
        Texture red_icon  = new Texture(image("item/rank_red.png"));
        Texture green_icon  = new Texture(image("item/rank_green.png"));
        Texture blue_icon  = new Texture(image("item/rank_blue.png"));           
        Texture orange_icon  = new Texture(image("item/rank_orange.png"));
        
        
        
        // setting up select box
        Button select_yeti = getUIFactoryService().newButton("Yeti");
        select_yeti.setOnAction(e -> {
        	choosePlayer[chosenPlayer] = "yeti";
            green.setVisible(false);        
            green_icon.setTranslateY(178 + 80 * chosenPlayer);
            green.setTranslateX(108 + 12 * 1 + green.getWidth()/2);
            green.setTranslateY(195 + 80 * 1 + 80 * chosenPlayer);
    		rank.getChildren().addAll( green_icon, green);
        	player[chosenPlayer] = getGameWorld().spawn("yeti");
        	select_yeti.setDisable(true);
        	if (++chosenPlayer >= 2) {
        		stage = MapleStage.SELECT;
        		getGameScene().removeUINode(selectBox);
        		getGameScene().addUINodes(pane);
        		chooseItem = 2;
        		
        		getGameScene().addUINode(rank);
        		
        	}
        });
        Button select_pig = getUIFactoryService().newButton("Pig");
        select_pig.setOnAction(e -> {
        	choosePlayer[chosenPlayer] = "pig";
            orange.setVisible(false);
            orange_icon.setTranslateY(178 + 80 * chosenPlayer);
            orange.setTranslateX(107 + 12 * 1 + orange.getWidth()/2);
            orange.setTranslateY(194 + 80 * 1 + 80 * chosenPlayer);
    		rank.getChildren().addAll( orange_icon, orange);
        	player[chosenPlayer] = getGameWorld().spawn("pig");
        	select_pig.setDisable(true);
        	if (++chosenPlayer >= 2) {
        		stage = MapleStage.SELECT;
        		getGameScene().removeUINode(selectBox);
        		getGameScene().addUINodes(pane);
        		chooseItem = 2;
        		
        		getGameScene().addUINode(rank);
        		
        	}
        });
        Button select_slime = getUIFactoryService().newButton("Slime");
        select_slime.setOnAction(e -> {
        	choosePlayer[chosenPlayer] = "slime";
            blue.setVisible(false); 
            blue_icon.setTranslateY(178 + 80 * chosenPlayer); 
            blue.setTranslateX(108 + 12 * 1 + blue.getWidth()/2);
            blue.setTranslateY(192 + 80 * 1 + 80 * chosenPlayer);
    		rank.getChildren().addAll( blue_icon, blue);
        	player[chosenPlayer] = getGameWorld().spawn("slime");
        	select_slime.setDisable(true);
        	if (++chosenPlayer >= 2) {
        		stage = MapleStage.SELECT;
        		getGameScene().removeUINode(selectBox);
        		getGameScene().addUINodes(pane);
        		chooseItem = 2;
        		
        		getGameScene().addUINode(rank);
        		
        	}
        });
        Button select_mushroom = getUIFactoryService().newButton("Mushroom");
        select_mushroom.setOnAction(e -> {
        	choosePlayer[chosenPlayer] = "mushroom";
            red.setVisible(false);
            red_icon.setTranslateY(178 + 80 * chosenPlayer);
   		 	red.setTranslateX(108 + 12 * 1 + red.getWidth()/2);
	     	red.setTranslateY(192 + 80 * 1 + 80 * chosenPlayer);
	   		rank.getChildren().addAll( red_icon, red);
        	player[chosenPlayer] = getGameWorld().spawn("mushroom");
        	select_mushroom.setDisable(true);
        	if (++chosenPlayer >= 2) {
        		stage = MapleStage.SELECT;
        		getGameScene().removeUINode(selectBox);
        		getGameScene().addUINodes(pane);
        		chooseItem = 2;
        		
        		getGameScene().addUINode(rank);
        		
        	}
        });
        Button select_back = getUIFactoryService().newButton("BACK");
        select_back.setOnAction(e -> {
        	getGameScene().removeUINode(selectBox);
        	getGameScene().addUINode(menuBox);
        	getGameScene().addUINode(title);
        	chosenPlayer = 0;
        	for (int j = 0; j < PLAYER_NUM; j++) {
        		player[j] = null;
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
        	canPlace = true;
        	pane.setVisible(false);
        	item = 1;
        	redballoon.setVisible(false);
        });
        redballoon.setTranslateX(150);
        redballoon.setTranslateY(150);
        
        Button hole = new Button("", new ImageView(image("item/hole.png")));
        hole.setStyle("-fx-background-color: transparent;");
        hole.setOnAction(e-> {
        	canPlace = true;
        	pane.setVisible(false);
        	item = 2;
        	hole.setVisible(false);
        });
        hole.setTranslateX(300);
        hole.setTranslateY(150);
        
        Button surprise = new Button("", new ImageView(image("item/surprise.png")));
        surprise.setStyle("-fx-background-color: transparent;");
        surprise.setOnAction(e-> {
        	canPlace = true;
        	pane.setVisible(false);
        	item = 3;
        	surprise.setVisible(false);
        });
        surprise.setTranslateX(600);
        surprise.setTranslateY(150);
        
        Button bomb = new Button("", new ImageView(image("item/bomb.png")));
        bomb.setOnAction(e-> {
        	canPlace = true;
        	pane.setVisible(false);
        	item = 4;
        	bomb.setVisible(false);
        });
        bomb.setTranslateX(150);
        bomb.setTranslateY(400);
        
        Button brick = new Button("", new ImageView(image("item/brick.png")));
        brick.setOnAction(e-> {
        	canPlace = true;
        	pane.setVisible(false);
        	item = 5;
        	brick.setVisible(false);
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
        
        //getGameScene().addUINodes(pane);
        
        pane.getChildren().addAll(bomb);
        pane.getChildren().addAll(brick);
//        getGameScene().addUINodes(pane);
        

        
        scoreText = new Text[2];
        for(int j = 0; j < 2; j++) {
            scoreText[j].setText("0");
        	scoreText[j].setTranslateX(640);
            scoreText[j].setTranslateY(215 + 80 * j);
            scoreText[j].setFont(Font.font(25));
        }

        rank.getChildren().addAll(scoreText);       

        //getGameScene().addUINode(rank);
    
        
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
	
	public void placeItem() {
		canPlace = false;
		
		if (chooseItem <= 0)
			stage = MapleStage.PLAY;
		else {
			pane.setVisible(true);
		}
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
		destination = getGameWorld().spawn("redflag", new Point2D(1500, 367));

		teleport1 = null;
		teleport1 = getGameWorld().spawn("teleport1", new Point2D(1039, 307));
		
		teleport2 = null;
		teleport2 = getGameWorld().spawn("teleport2", new Point2D(360, 627));
		/*balloon = null;
		balloon = getGameWorld().spawn("balloon");
		balloon.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(435, 413));
		*/
		/*isGenTeleport = false;
		teleport1 = null;
		teleport1 = getGameWorld().spawn("teleport1", new Point2D(470, 380));
		 */

	}
	
	@Override
	protected void onPreInit() {
        getSettings().setGlobalMusicVolume(0.5);
        loopBGM("maplestory.wav");
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
				playerDead(player);
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.TRAP) {
			@Override
			public void onCollisionBegin(Entity player, Entity hole) {
				player.setOpacity(0);
				player.getComponent(PlayerComponent.class).dead();
				playerDead(player);
				deadTomb(player);
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.TRAP) {
			@Override
			public void onCollisionBegin(Entity player, Entity surprise) {
				player.setOpacity(0);
				player.getComponent(PlayerComponent.class).dead();
				playerDead(player);
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
			public void onCollisionBegin(Entity p1, Entity p2) { 
				
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.DEADLINE) {
			@Override
			public void onCollisionBegin(Entity player, Entity deadline) {
				player.setOpacity(0);
				player.getComponent(PlayerComponent.class).dead();
				deadTomb(player);
				playerDead(player);
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER,  MapleType.ITEM) {
			public void onCollisionBegin(Entity player, Entity redflag) {
				player.getComponent(PlayerComponent.class).win();
				playerWin(player);
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.TELEPORT1) {
			public void onCollisionBegin(Entity player, Entity teleport1) {
				player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(360, 627));
			}
		});
	}
	
	public void playerDead(Entity player) {
		
	}
	
	public void playerWin(Entity player) {
		
	}
	
	public void deadTomb(Entity player) {
		if (!realDead) {
			tomb = getGameWorld().spawn("tomb");
			tomb.setPosition(new Point2D(player.getX(), 0));
			realDead = true;
		}
	}

	
	public void addPoint() {
		for(int j = 0; j < 2; j++) {
			scoreText[j].setText(Integer.toString(score[j]));
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}

}
