import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.Group;
import javafx.scene.Scene;

public class MapGameController implements Initializable {
    public MapData mapData;
    public MoveChara chara;
    public GridPane mapGrid;
    public StackPane mapStack;
    public ImageView[] mapImageViews;
    //For Goal Jadging
    public boolean isgoal = false;
    //Making Goal Effects
    public Image goalImage = new Image("png/GOAL.png");
    public ImageView goalImageView = new ImageView(goalImage);
    public Button ranking = new Button("RANKING");
    public Button next = new Button("NEXT");
//    public Group[] mapGroups;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapData = new MapData(21,15);
        chara = new MoveChara(1,1,mapData);
//        mapGroups = new Group[mapData.getHeight()*mapData.getWidth()];
        mapImageViews = new ImageView[mapData.getHeight()*mapData.getWidth()];
        for(int y=0; y<mapData.getHeight(); y++){
            for(int x=0; x<mapData.getWidth(); x++){
                int index = y*mapData.getWidth() + x;
                mapImageViews[index] = mapData.getImageView(x,y);
            }
        }
        mapPrint(chara, mapData);
    }

    public void mapPrint(MoveChara c, MapData m){
        int cx = c.getPosX();
        int cy = c.getPosY();
        mapGrid.getChildren().clear();
        for(int y=0; y<mapData.getHeight(); y++){
            for(int x=0; x<mapData.getWidth(); x++){
                int index = y*mapData.getWidth() + x;
                //added
                mapImageViews[index] = m.getImageView(x,y);
                if (x==cx && y==cy) {
                    mapGrid.add(c.getCharaImageView(), x, y);
                } else {
                    mapGrid.add(mapImageViews[index], x, y);
                }
            }
        }
    }

    //run when chara reachs goal
    public void goalAction(MoveChara c,MapData m){
        if (c.isGoal(m)==true && isgoal==false){
            isgoal = true;
            initGoalButton();
            mapStack.getChildren().addAll(goalImageView,ranking,next);
        }
    }
    //initializing goal effects
    public void initGoalButton(){
        mapStack.setAlignment(goalImageView, Pos.CENTER);
        mapStack.setAlignment(ranking,Pos.BOTTOM_RIGHT);
        mapStack.setAlignment(next,Pos.BOTTOM_RIGHT);
        mapStack.setMargin(ranking, new Insets(90,140,88,0));
        mapStack.setMargin(next, new Insets(90,65,88,0));
        ranking.setPrefWidth(80);
        ranking.setPrefHeight(8);
        next.setPrefWidth(60);
        next.setPrefHeight(8);
    }
    //DEBUG THROUGH WALL
    public void func1ButtonAction(ActionEvent event) {
        mapData.fillMap(MapData.TYPE_NONE);
        mapData.setGoal(19,13);
    }
    //DEBUG RESET
    public void func2ButtonAction(ActionEvent event) {
        outputAction("RESET");
        mapData = new MapData(21,15);
        chara = new MoveChara(1, 1, mapData);
        isgoal = false;
        mapStack.getChildren().removeAll(goalImageView,ranking,next);
        mapPrint(chara, mapData);
        //map2 = false;
    }
    //DEBUG GOAL
    public void func3ButtonAction(ActionEvent event) {
        if(isgoal != true){
            initGoalButton();
            mapStack.getChildren().addAll(goalImageView,ranking,next);    
        }
    }
    public void func4ButtonAction(ActionEvent event) {
    }

    public void keyAction(KeyEvent event){
        KeyCode key = event.getCode();
        if (key == KeyCode.DOWN){
            downButtonAction();
        }else if (key == KeyCode.RIGHT){
            rightButtonAction();
        }else if (key == KeyCode.LEFT){
            leftButtonAction();
        }else if (key == KeyCode.UP){
            upButtonAction();
        }
    }

    public void outputAction(String actionString) {
        System.out.println("Select Action: " + actionString);
    }

    public void downButtonAction(){
        outputAction("DOWN");
        chara.setCharaDir(MoveChara.TYPE_DOWN);
        chara.move(0, 1);
        mapPrint(chara, mapData);
        goalAction(chara, mapData);
    }
    public void downButtonAction(ActionEvent event) {
        downButtonAction();
    }

    public void rightButtonAction(){
        outputAction("RIGHT");
        chara.setCharaDir(MoveChara.TYPE_RIGHT);
        chara.move( 1, 0);
        mapPrint(chara, mapData);
        goalAction(chara,mapData);
    }
    public void rightButtonAction(ActionEvent event) {
       rightButtonAction();

    }
    public void leftButtonAction(){    /**左に進む*/
        chara.setCharaDir(MoveChara.TYPE_LEFT);
        chara.move( -1, 0);
        mapPrint(chara, mapData);
    }
    public void leftButtonAction(ActionEvent event) {
        leftButtonAction();
    }

    public void upButtonAction(){    /**上に進む*/
        outputAction("UP");
        chara.setCharaDir(MoveChara.TYPE_UP);
        chara.move( 0, -1);
        mapPrint(chara, mapData);
    }
    public void upButtonAction(ActionEvent event) {
        upButtonAction();
    }
}
