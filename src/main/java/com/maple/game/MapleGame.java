package com.maple.game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.time.TimerAction;

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
import javafx.util.Duration;

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
		
		playerFinish = 0;
	}
	

	Pane pane, rank;
	
	VBox menuBox, selectBox;
	
	private Button redballoon_button, brick_button, bomb_button, surprise_button, hole_button;
	
	private int chosenPlayer;
	
    Texture red;
    Texture green;
    Texture blue;    
    Texture orange;

    Texture red_icon;
    Texture green_icon;
    Texture blue_icon;           
    Texture orange_icon;
	
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

        red  = new Texture(image("item/red.png"));
        green  = new Texture(image("item/green.png"));
        blue  = new Texture(image("item/blue.png"));    
        orange  = new Texture(image("item/orange.png"));

        red_icon  = new Texture(image("item/rank_red.png"));
        green_icon  = new Texture(image("item/rank_green.png"));
        blue_icon  = new Texture(image("item/rank_blue.png"));           
        orange_icon  = new Texture(image("item/rank_orange.png"));
        
        
        // setting up select box
        Button select_yeti = getUIFactoryService().newButton("Yeti");
        select_yeti.setOnAction(e -> {
        	choosePlayer[chosenPlayer] = "yeti";
            green.setVisible(false);
        	//green.setScaleX(10);
            green_icon.setTranslateY(178 + 80 * chosenPlayer);
            //green.setTranslateX(108 + 10 * score[0] + green.getWidth()/2);
            //green.setTranslateY(195 + 80 * chosenPlayer);
    		rank.getChildren().addAll( green_icon, green);
        	player[chosenPlayer] = getGameWorld().spawn("yeti");
        	player[chosenPlayer].getComponent(PlayerComponent.class).playerNum = chosenPlayer;
        	select_yeti.setDisable(true);
        	if (++chosenPlayer >= 2) {
        		stage = MapleStage.SELECT;
        		getGameScene().removeUINode(selectBox);
        		getGameScene().addUINodes(pane);
        		chooseItem = 2;
        		
//        		getGameScene().addUINode(rank);
        		
        	}
        });
        Button select_pig = getUIFactoryService().newButton("Pig");
        select_pig.setOnAction(e -> {
        	choosePlayer[chosenPlayer] = "pig";
            orange.setVisible(false);
        	//orange.setScaleX(20);
        	orange_icon.setTranslateY(178 + 80 * chosenPlayer);
            //orange.setTranslateX(107 + 15 * score[1] + orange.getWidth()/2);
            //orange.setTranslateY(194 + 80 * chosenPlayer);
    		rank.getChildren().addAll( orange_icon, orange);
        	player[chosenPlayer] = getGameWorld().spawn("pig");
        	player[chosenPlayer].getComponent(PlayerComponent.class).playerNum = chosenPlayer;
        	select_pig.setDisable(true);
        	if (++chosenPlayer >= 2) {
        		stage = MapleStage.SELECT;
        		getGameScene().removeUINode(selectBox);
        		getGameScene().addUINodes(pane);
        		chooseItem = 2;
        		
//        		getGameScene().addUINode(rank);
        		
        	}
        });
        Button select_slime = getUIFactoryService().newButton("Slime");
        select_slime.setOnAction(e -> {
        	choosePlayer[chosenPlayer] = "slime";
            blue.setVisible(false); 
        	//blue.setScaleX(20);
            blue.setVisible(false); 
            blue_icon.setTranslateY(178 + 80 * chosenPlayer); 
            //blue.setTranslateX(108 + 12 * 1 + 230 + blue.getWidth()/2);
            //blue.setTranslateY(192 + 80 * chosenPlayer);
    		rank.getChildren().addAll( blue_icon, blue);
        	player[chosenPlayer] = getGameWorld().spawn("slime");
        	player[chosenPlayer].getComponent(PlayerComponent.class).playerNum = chosenPlayer;
        	select_slime.setDisable(true);
        	if (++chosenPlayer >= 2) {
        		stage = MapleStage.SELECT;
        		getGameScene().removeUINode(selectBox);
        		getGameScene().addUINodes(pane);
        		chooseItem = 2;
        		
//        		getGameScene().addUINode(rank);
        		
        	}
        });
        Button select_mushroom = getUIFactoryService().newButton("Mushroom");
        select_mushroom.setOnAction(e -> {
        	choosePlayer[chosenPlayer] = "mushroom";
            red.setVisible(false);
        	//red.setScaleX(10);
            red_icon.setTranslateY(178 + 80 * chosenPlayer);
   		 	//red.setTranslateX(108 + 12 * 1 + 230 + red.getWidth()/2);
	     	//red.setTranslateY(192 + 80 * chosenPlayer);
	   		rank.getChildren().addAll( red_icon, red);
        	player[chosenPlayer] = getGameWorld().spawn("mushroom");
        	player[chosenPlayer].getComponent(PlayerComponent.class).playerNum = chosenPlayer;
        	select_mushroom.setDisable(true);
        	if (++chosenPlayer >= 2) {
        		stage = MapleStage.SELECT;
        		getGameScene().removeUINode(selectBox);
        		getGameScene().addUINodes(pane);
        		chooseItem = 2;
        		
//        		getGameScene().addUINode(rank);	
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
        
        redballoon_button = new Button("", new ImageView(image("item/balloon.png")));
        redballoon_button.setStyle("-fx-background-color: transparent;");
        redballoon_button.setOnAction(e -> {
        	canPlace = true;
        	pane.setVisible(false);
        	item = 1;
        	redballoon_button.setVisible(false);
        });
        redballoon_button.setTranslateX(150);
        redballoon_button.setTranslateY(150);
        
        hole_button = new Button("", new ImageView(image("item/hole.png")));
        hole_button.setStyle("-fx-background-color: transparent;");
        hole_button.setOnAction(e-> {
        	canPlace = true;
        	pane.setVisible(false);
        	item = 2;
        	hole_button.setVisible(false);
        });
        hole_button.setTranslateX(300);
        hole_button.setTranslateY(150);
        
        surprise_button = new Button("", new ImageView(image("item/surprise.png")));
        surprise_button.setStyle("-fx-background-color: transparent;");
        surprise_button.setOnAction(e-> {
        	canPlace = true;
        	pane.setVisible(false);
        	item = 3;
        	surprise_button.setVisible(false);
        });
        surprise_button.setTranslateX(600);
        surprise_button.setTranslateY(150);
        
        bomb_button = new Button("", new ImageView(image("item/bomb.png")));
        bomb_button.setStyle("-fx-background-color: transparent;");
        bomb_button.setOnAction(e-> {
        	canPlace = true;
        	pane.setVisible(false);
        	item = 4;
        	bomb_button.setVisible(false);
        });
        bomb_button.setTranslateX(150);
        bomb_button.setTranslateY(400);
        
        brick_button = new Button("", new ImageView(image("item/brick.png")));
        brick_button.setStyle("-fx-background-color: transparent;");
        brick_button.setOnAction(e-> {
        	canPlace = true;
        	pane.setVisible(false);
        	item = 5;
        	brick_button.setVisible(false);
        });
        brick_button.setTranslateX(300);
        brick_button.setTranslateY(400);
        
        pane = new Pane();
        pane.setBackground(new Background(new BackgroundImage(image("background/book.png"), null, null, null, null)));
        pane.setTranslateX(getAppWidth()/2 - 520);
        pane.setTranslateY(getAppHeight()/2 - 350);
        pane.setPrefSize(1040, 700);
        pane.getChildren().addAll(redballoon_button);
        pane.getChildren().addAll(hole_button);
        pane.getChildren().addAll(surprise_button);
        
        //getGameScene().addUINodes(pane);
        
        pane.getChildren().addAll(bomb_button);
        pane.getChildren().addAll(brick_button);
//        getGameScene().addUINodes(pane);
        

        
        /*Text scoreMushroom = new Text("0");
        scoreMushroom.setTranslateX(640);
        scoreMushroom.setTranslateY(215);
        scoreMushroom.setFont(Font.font(25));
        Text scoreYeti = new Text("0");
        scoreYeti.setTranslateX(640);
        scoreYeti.setTranslateY(295);
        scoreYeti.setFont(Font.font(25));
        Text scoreSlime = new Text("0");
        scoreSlime.setTranslateX(640);
        scoreSlime.setTranslateY(215);
        scoreSlime.setFont(Font.font(25));
        Text scorePig = new Text("0");
        scorePig.setTranslateX(640);
        scorePig.setTranslateY(215);
        scorePig.setFont(Font.font(25));
        rank.getChildren().addAll(scoreMushroom, scoreYeti, scoreSlime, scorePig);*/    
        

        scoreText = new Text[2];
        //scoreText[0] = new Text("10");
        //scoreText[1] = new Text("20");

        for(int j = 0; j < 2; j++) {
            scoreText[j] = new Text("0");
        	scoreText[j].setTranslateX(640);
            scoreText[j].setTranslateY(215 + 80 * j);
            scoreText[j].setFont(Font.font(25));
        }

        //rank.getChildren().addAll(scoreText);       

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
		
		if (chooseItem <= 0) {
			System.out.println(chooseItem);
			stage = MapleStage.PLAY;
			playerFinish = 0;
		} else {
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
				playerDead();
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.TRAP) {
			@Override
			public void onCollisionBegin(Entity player, Entity hole) {
				player.setOpacity(0);
				player.getComponent(PlayerComponent.class).dead();
				playerDead();
				deadTomb(player);
			}
		});
		
//		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.TRAP) {
//			@Override
//			public void onCollisionBegin(Entity player, Entity surprise) {
//				player.setOpacity(0);
//				player.getComponent(PlayerComponent.class).dead();
//				playerDead();
//				deadTomb(player);
//			}
//		});
//		
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
				playerDead();
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER,  MapleType.ITEM) {
			@Override
			public void onCollisionBegin(Entity player, Entity redflag) {
				if (player.getComponent(PlayerComponent.class).isDead)
					return;
				player.getComponent(PlayerComponent.class).win();
				playerWin(player);
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.TELEPORT1) {
			@Override
			public void onCollisionBegin(Entity player, Entity teleport1) {
				player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(360, 627));
			}
		});
	}
	
	private int playerFinish;
	
	public void nextRound() {
		playerFinish = 0;
		
		for (int i = 0; i < 2; i++) {
			player[i].getComponent(PhysicsComponent.class).overwritePosition(new Point2D(50, 50));	
			player[i].setOpacity(1);
		}
		
		if (tomb != null) {
			getGameWorld().removeEntity(tomb);
		}
		
		getGameScene().addUINode(rank);
		stage = MapleStage.RESULT;
		
		runOnce(() ->{
			getGameScene().removeUINode(rank);
			pane.setVisible(true);
			stage = MapleStage.SELECT;
			
			// reset the mechanisms
			redballoon_button.setVisible(true);
	        brick_button.setVisible(true);
	        bomb_button.setVisible(true);
	        surprise_button.setVisible(true);
	        hole_button.setVisible(true);
			
	        addPoint();
	        realDead = false;
	        
			chooseItem = 2;
			
			// unfreeze players
			for (int i = 0; i < 2; i++) {
				player[i].getComponent(PlayerComponent.class).restore();
			}
		}, Duration.seconds(3));
	}
	
	public void playerDead() {
		if (++playerFinish >= 2) {
			nextRound();
		}
	}
	
	public void playerWin(Entity player) {
		player.getComponent(PlayerComponent.class).restore();

		if (++playerFinish >= 2) {
			nextRound();
		}
		score[player.getComponent(PlayerComponent.class).playerNum]++;
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
			
			/*if(score[j] > 0) {
				switch(choosePlayer[j]) {
				case "yeti" :
					green.setVisible(true);
					green.setScaleX(score[j]);
					green.setTranslateX(108 + 12 * score[j] + green.getWidth()/2);
					break;
				case "pig" :
					orange.setVisible(true);
					orange.setScaleX(score[j]);
					orange.setTranslateX(108 + 12 * score[j] + orange.getWidth()/2);
					break;
				case "mushroom" :
					red.setVisible(true);
					red.setScaleX(score[j]);
		   		 	red.setTranslateX(108 + 12 * score[j] + red.getWidth()/2);
					break;
				case "slime" :
					blue.setVisible(true);
					blue.setScaleX(score[j]);
					blue.setTranslateX(108 + 12 * score[j] + blue.getWidth()/2);
					break;
				}
			}*/
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}

}
