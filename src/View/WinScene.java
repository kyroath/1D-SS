package View;

import static View.Main.game;

import Controller.Dungeon.Room.Fight;
import Models.Cards.AbstractCard;
import View.TreasureScene;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.util.Pair;



import java.awt.*;
import java.util.Arrays;
import java.util.List;
public class WinScene extends RoomScene  {

    Rectangle backToMenu;
    VBox box;

    public WinScene()
    {
        super(new StackPane());
        backToMenu = new Rectangle();
        root.setMinSize( width, height);
        box = new VBox();


    }
    @Override
    public void draw() {

    }


    @Override
    public void initialize() {

        System.out.println("I AM CALLED IN WINSCENE");

        addBackground();
        backToMenu = new Rectangle();
        backToMenu.setHeight(height/4);
        backToMenu.setWidth(width/8);
        backToMenu.setX(0);
        backToMenu.setY(50);
        backToMenu.setFill( new ImagePattern(new Image("BackToHome.png")));

        backToMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                game.currentScene = new MenuScene();
                Main.window.setScene(
                        game.currentScene);
                game.currentScene.initialize();

            }
        });



        if(!box.getChildren().contains(backToMenu))
        box.getChildren().add(backToMenu);
        if(!root.getChildren().contains(box))
        root.getChildren().add(box);



    }
    private void addBackground() {
        ImageView win = new ImageView(new Image("win.jpg"));
        win.setFitWidth(width);
        win.setFitHeight(height);


        ImageView lose = new ImageView(new Image("lose.png"));
        lose.setFitWidth(width);
        lose.setFitHeight(height);

        if(game.getPlayer().getCurrentHP() > 0) {
            if(! root.getChildren().contains(win))
            root.getChildren().add(win);
        }
        else {
            if(! root.getChildren().contains(lose))
            root.getChildren().add(lose);
        }

    }

}
