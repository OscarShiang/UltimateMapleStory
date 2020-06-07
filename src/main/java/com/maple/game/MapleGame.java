package com.maple.game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;

import com.maple.player.*;

import static com.almasb.fxgl.dsl.FXGL.*;

public class MapleGame extends GameApplication {
	
	private Entity player;
	private String IPaddress, Port;
	
	@Override
	protected void initSettings(GameSettings settings) {
		// TODO Auto-generated method stub
		settings.setTitle("MapleStory");
		settings.setWidth(70 * 15);
		settings.setHeight(70 * 10);
	}
	
	VBox vbox1, vbox2;
	
	protected void initUI() {
		TextField ip;
		ip = new TextField("IP address");
		Button create = getUIFactoryService().newButton("CREATE");
        create.setOnAction(e -> {
        	//ip = new TextField("IP address");
        	//getDialogService().showInputBox(" ", ipAddress -> {
        		//typePort();
        	//});
        	remove();
       	});
        
		Button join = getUIFactoryService().newButton("JOIN");
        join.setOnAction(e -> {
        	//getDialogService().showInputBox(" ", ipAddress -> {
        		//typePort();
        	//});
        	remove();
        });
        
		Button quit = getUIFactoryService().newButton("QUIT");
        quit.setOnAction(e -> {
        	System.exit(0);
        });
        
        vbox1 = new VBox(10);
        vbox1.setTranslateX(400);
        vbox1.setTranslateY(300);
        vbox1.getChildren().addAll(
                create, join, quit, ip
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
		setLevelFromMap("tmp.tmx");
		
		player = getGameWorld().spawn("player", 50, 50);
		
	}
	
	@Override
	protected void initPhysics() {
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.COIN) {
			@Override
			public void onCollisionBegin(Entity player, Entity coin) {
				coin.removeFromWorld();
			}
		});
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(MapleType.PLAYER, MapleType.PLATFORM) {
			@Override
			public void onCollisionBegin(Entity player, Entity platform) {
				player.getComponent(PlayerComponent.class).recover();
			}
		});
	}
	
	protected void type() {
		//getDialogService().showInputBox(" ", Port -> {
		//	
		//});
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
        
        vbox2 = new VBox(10);
        vbox2.setTranslateX(400);
        vbox2.setTranslateY(300);
        vbox2.getChildren().addAll(
                ip, port, ok
        );
        ip.setVisible(true);
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
