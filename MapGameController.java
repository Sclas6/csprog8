import java.io.File;
import java.net.URL;
import java.util.*;
import java.sql.Timestamp; // score data para //
import java.text.SimpleDateFormat; // score data format //
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
import javafx.scene.text.Text;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.event.EventHandler;

public class MapGameController implements Initializable {
    public MapData maptom;
    public MoveTom tom;
    public MapData mapjerry;
    public MoveJerry jerry;
    public GridPane mapGrid;
    public GridPane itemGrid;
    public StackPane mapStack;
    public ImageView[] mapImageViews;
    public ImageView[] itemImageViews;
    //For Goal Jadging
    public boolean isgoal = false;
    //map player seeing
    public static boolean isjerrymap;
    //Making Goal Effects
    public Image goalImage = new Image("png/GOAL.png");
    public Image scoreWindow = new Image("png/ScoreWindow.png");
    public Image rem = new Image("png/REMNANT.png");
    public ImageView goalImageView = new ImageView(goalImage);
    public ImageView scoreWindowView = new ImageView(scoreWindow);
    public ImageView remnant = new ImageView(rem);
    public Button ranking = new Button("RANKING");
    public Button next = new Button("NEXT");
    public Button close = new Button("CLOSE");
    public Label yourScore = new Label("");
    public Text rank = new Text("");
//    public Group[] mapGroups;
    public static int score;
    public Label item_message;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        maptom = new MapData(21,15,-1);
        tom = new MoveTom(1,1,maptom);
        mapjerry = new MapData(21,15,0);
        jerry = new MoveJerry(1, 1, mapjerry);
        mapjerry.setItem(0);
//        mapGroups = new Group[mapData.getHeight()*mapData.getWidth()];
        mapImageViews = new ImageView[maptom.getHeight()*maptom.getWidth()];
        for(int y=0; y<maptom.getHeight(); y++){
            for(int x=0; x<maptom.getWidth(); x++){
                int index = y*maptom.getWidth() + x;
                mapImageViews[index] = maptom.getImageView(x,y);
            }
        }
        initViews();
        isjerrymap = false;
        mapPrint(tom, maptom);
    }

    public void mapPrint(MoveChara c, MapData m){
        int cx = c.getPosX();
        int cy = c.getPosY();
        mapGrid.getChildren().clear();
        mapImageViews = new ImageView[m.getHeight()*m.getWidth()];
        item_message.setText(c.getMessage());
        for(int y=0; y<m.getHeight(); y++){
            for(int x=0; x<m.getWidth(); x++){
                int index = y*m.getWidth() + x;
                //added
                mapImageViews[index] = m.getImageView(x,y);
                if (x==cx && y==cy) {
                    mapGrid.add(c.getCharaImageView(), x, y);
                } else {
                    mapGrid.add(mapImageViews[index], x, y);
                }
            }
        }
        if(isjerrymap==false && (tom.getPosX()*tom.getPosY()!=jerry.getPosX()*jerry.getPosY())){
            mapGrid.add(remnant,jerry.getPosX(),jerry.getPosY());
        }
        int item_count = MoveTom.getItemCount();
        itemImageViews = new ImageView[5];
        for(int i=0;i<5;i++){
            itemImageViews[i] = new ImageView(new Image("png/ITEM.png"));
        }
        for(int i=0;i<MoveTom.getItemCount();i++){
            itemGrid.add(itemImageViews[i],i,0);
        }
        System.out.println(item_count);
    }

    //run when chara reachs goal
    public void goalAction(MoveChara c,MapData m){
        if (c.isGoal(m)==true && isgoal==false){
            isgoal = true;
            makeScore();
            mapStack.getChildren().addAll(goalImageView,ranking,next);
            CsvManager.exportCsv();

        }
    }
    // Score format //
    public static String getScoreData(){
      Timestamp ts = new Timestamp(System.currentTimeMillis());
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm");
      String str = sdf.format(ts);
      String scoreData = MapGame.getName() + "," + getScore() + "," + str;
      return scoreData;
    }

    // make score //
    public void makeScore(){
      score = (int)(Math.random()*100000);
    }
    // reuturn //
    public static int getScore(){
        return score;
    }

    public void viewRanking(){
       mapStack.getChildren().removeAll(scoreWindowView, rank, yourScore);
       String ranking = CsvManager.getRanking();
       yourScore.setText("Your Score: " + Integer.toString(score));
       rank.setText(ranking);
       mapStack.getChildren().addAll(scoreWindowView, rank, yourScore, close);
    }

    //initializing goal effects
    @SuppressWarnings("static-access")
    public void initViews(){
        mapStack.setAlignment(goalImageView, Pos.CENTER);
        mapStack.setAlignment(ranking,Pos.BOTTOM_RIGHT);
        mapStack.setAlignment(next,Pos.BOTTOM_RIGHT);
        mapStack.setAlignment(close,Pos.BOTTOM_RIGHT);
        mapStack.setAlignment(yourScore,Pos.TOP_CENTER);
        mapStack.setAlignment(rank,Pos.CENTER);
        mapStack.setMargin(ranking, new Insets(90,140,88,0));
        mapStack.setMargin(next, new Insets(90,65,88,0));
        mapStack.setMargin(close, new Insets(0,40,32,0));
        mapStack.setMargin(scoreWindowView, new Insets(0,0,20,0));
        mapStack.setMargin(yourScore, new Insets(50,0,0,0));
        mapStack.setMargin(rank, new Insets(70,0,0,0));
        ranking.setPrefWidth(80);
        ranking.setPrefHeight(8);
        ranking.setOnAction((ActionEvent)-> {
            outputAction("ranking");
            viewRanking();
        });
        close.setPrefWidth(70);
        close.setPrefHeight(4);
        close.setOnAction((ActionEvent)->{
            outputAction("CLOSE ranking");
            mapStack.getChildren().removeAll(scoreWindowView,rank,yourScore,close);
        });
        next.setPrefWidth(70);
        next.setPrefHeight(4);
        next.setOnAction((ActionEvent)->{
            outputAction("NEXT MAP");
            isgoal = false;
            resetMap();
            mapStack.getChildren().removeAll(goalImageView,ranking,next);
            mapPrint(tom, maptom);
        });
        rank.setFont(Font.loadFont("file:font/ラノベPOP.otf",28));
        rank.setStyle("-fx-line-spacing: 8px;"+"-fx-stroke: #00CC00;");
        rank.setFill(Color.WHITE);
        yourScore.setFont(Font.loadFont("file:font/ラノベPOP.otf",60));
        yourScore.setTextFill(Color.WHITE);
        item_message.setFont(Font.loadFont("file:font/ラノベPOP.otf",28));
    }
    public void resetMap(){
        outputAction("RESET");
        initViews();
        maptom = new MapData(21,15,-1);
        tom = new MoveTom(1, 1, maptom);
        mapjerry = new MapData(21,15,0);
        jerry = new MoveJerry(1, 1, mapjerry);
        isgoal = false;
        MoveChara.setItem(0);
        MoveChara.message = "アイテム数: 0";
        mapStack.getChildren().removeAll(goalImageView,ranking,next);
    }
    //DEBUG THROUGH WALL
    public void func1ButtonAction(ActionEvent event) {
        System.out.println(mapjerry.getItem());
        maptom.fillMap(MapData.TYPE_NONE);
        maptom.setGoal(19,13);
        mapStack.getChildren().removeAll(goalImageView,ranking,next);
        mapPrint(tom, maptom);
    }
    //DEBUG RESET
    public void func2ButtonAction(ActionEvent event) {
        if(move == false){
            resetMap();
            mapPrint(tom, maptom);
            isjerrymap = false;
        }
    }
    public void moveTom(){
        timeline = new Timeline(
            new KeyFrame(
                new Duration(200),//1000ミリ秒
                new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event){
                        int dx,dy;
                        dx = jerry.getPosX()-tom.getPosX();
                        dy = jerry.getPosY()-tom.getPosY();
                        if(dx==tmpx&&dy==tmpy){
                            timeline.stop();
                            move = false;
                            //map2 = true;
                            //mapPrint(jerry,mapData2);
                        }else{
                            System.out.println("( "+dx+","+dy+" )");
                        }
                        if(Math.abs(dx)>Math.abs(dy)){
                            if(dx>0 &&maptom.getMap(tom.getPosX() + 1,tom.getPosY())!=MapData.TYPE_WALL){
                                tom.setCharaDir(MoveChara.TYPE_RIGHT);
                                tom.move(1, 0);
                            }
                            else if(dx<0 &&maptom.getMap(tom.getPosX() - 1,tom.getPosY())!=MapData.TYPE_WALL){
                                tom.setCharaDir(MoveChara.TYPE_LEFT);
                                tom.move(-1, 0);
                            }
                            else if(dy>0){
                                tom.setCharaDir(MoveChara.TYPE_DOWN);
                                tom.move(0, 1);
                            }
                            else if(dy<0){
                                tom.setCharaDir(MoveChara.TYPE_UP);
                                tom.move(0,-1);
                            }
                        }
                        else{
                            if(dy>0&&maptom.getMap(tom.getPosX(),tom.getPosY()+1)!=MapData.TYPE_WALL){
                                tom.setCharaDir(MoveChara.TYPE_DOWN);
                                tom.move(0, 1);
                            }
                            else if(dy<0&&maptom.getMap(tom.getPosX(),tom.getPosY()-1)!=MapData.TYPE_WALL){
                                tom.setCharaDir(MoveChara.TYPE_UP);
                                tom.move(0,-1);
                            }
                            else if(dx>0){
                                tom.setCharaDir(MoveChara.TYPE_RIGHT);
                                tom.move(1, 0);
                            }
                            else if(dx<0){
                                tom.setCharaDir(MoveChara.TYPE_LEFT);
                                tom.move(-1, 0);
                            }
                        }
                        goalAction(tom, maptom);
                        mapPrint(tom,maptom);
                        tmpx = dx;
                        tmpy = dy;
                    }
                }
            )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    //DEBUG GOAL
    public Timeline timeline;
    public int tmpx,tmpy;
    public boolean move = false;
    public void func3ButtonAction(ActionEvent event) {
        if(isjerrymap == true){
            isjerrymap = false;
            move = true;
            Image jerryhole = new Image(new File("png/JERRYHOLE.gif").toURI().toString());
            ImageView jerryholeimage = new ImageView(jerryhole);
            mapGrid.add(jerryholeimage, jerry.getPosX(), jerry.getPosY());
            Timeline changemap = new Timeline(
                new KeyFrame(
                    new Duration(1000),
                    new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent event){
                            mapPrint(tom, maptom);
                            Image jerryhole2 = new Image(new File("png/JERRYHOLE2.gif").toURI().toString());
                            ImageView jerryholeimage2 = new ImageView(jerryhole2);
                            mapGrid.add(jerryholeimage2, jerry.getPosX(), jerry.getPosY());
                        }
                    }
                )
            );
            Timeline movetom = new Timeline(
                new KeyFrame(
                    new Duration(2600),
                    new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent event){
                            moveTom();
                        }
                    }
                )
            );
            changemap.play();
            movetom.play();
        }
    }
    public void func4ButtonAction(ActionEvent event) {
        if(move == false){
            isjerrymap = true;
            mapPrint(jerry, mapjerry);
        }
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
        if(isjerrymap == true){
            outputAction("DOWN");
            jerry.setCharaDir(MoveChara.TYPE_DOWN);
            jerry.move(0, 1);
            mapPrint(jerry, mapjerry);
            goalAction(jerry, mapjerry);
        }
    }
    public void downButtonAction(ActionEvent event) {
        downButtonAction();
    }
    public void rightButtonAction(){
        if(isjerrymap==true){
            outputAction("RIGHT");
            jerry.setCharaDir(MoveChara.TYPE_RIGHT);
            jerry.move(1, 0);
            mapPrint(jerry, mapjerry);
            goalAction(jerry,mapjerry);
        }
    }
    public void rightButtonAction(ActionEvent event) {
       rightButtonAction();

    }
    public void leftButtonAction(){    /**左に進む*/
        if(isjerrymap==true){
            outputAction("DOWN");
            jerry.setCharaDir(MoveChara.TYPE_LEFT);
            jerry.move(-1, 0);
            mapPrint(jerry, mapjerry);
            goalAction(jerry,mapjerry);
        }
    }
    public void leftButtonAction(ActionEvent event) {
        leftButtonAction();
    }
    public void upButtonAction(){    /**上に進む*/
        if(isjerrymap==true){
            outputAction("UP");
            jerry.setCharaDir(MoveChara.TYPE_UP);
            jerry.move(0, -1);
            mapPrint(jerry, mapjerry);
            goalAction(jerry,mapjerry);
        }
    }
    public void upButtonAction(ActionEvent event) {
        upButtonAction();
    }
}
