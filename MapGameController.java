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
import javafx.animation.AnimationTimer;
import javafx.util.Duration;
import javafx.event.EventHandler;

public class MapGameController implements Initializable {
    public MapData mapData;
    public MoveTom tom;
    public MapData mapData2;
    public MoveJerry jerry;
    public GridPane mapGrid;
    public StackPane mapStack;
    public ImageView[] mapImageViews;
    //For Goal Jadging
    public boolean isgoal = false;
    //map player seeing
    public static boolean map2;
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
    public static int item_count;
    public Label label1;
    public Label time;
    public int count;
    public boolean is_timer_start = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapData = new MapData(21,15,-1);
        tom = new MoveTom(1,1,mapData);
        mapData2 = new MapData(21,15,0);
        jerry = new MoveJerry(1, 1, mapData2);
        mapData2.setItem(0);
//        mapGroups = new Group[mapData.getHeight()*mapData.getWidth()];
        mapImageViews = new ImageView[mapData.getHeight()*mapData.getWidth()];
        for(int y=0; y<mapData.getHeight(); y++){
            for(int x=0; x<mapData.getWidth(); x++){
                int index = y*mapData.getWidth() + x;
                mapImageViews[index] = mapData.getImageView(x,y);
            }
        }
        initViews();
        map2 = false;
        mapPrint(tom, mapData);
    }

    public void mapPrint(MoveChara c, MapData m){
        int cx = c.getPosX();
        int cy = c.getPosY();
        mapGrid.getChildren().clear();
        mapImageViews = new ImageView[m.getHeight()*m.getWidth()];
        label1.setText(c.getMessage());
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
        if(map2==false && (tom.getPosX()*tom.getPosY()!=jerry.getPosX()*jerry.getPosY())){
            mapGrid.add(remnant,jerry.getPosX(),jerry.getPosY());
        }
        if(is_timer_start == false){
          Timer(240);
          is_timer_start =true;
        }
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

    public void Timer(int c){
      count = c;
      Timeline timer = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>(){
              @Override
              public void handle(ActionEvent event) {
                  count -= 1;
                  time.setText(Integer.toString(count));
              }
          }));
          timer.setCycleCount(480);
          timer.play();
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
      score = (int)(count*1000 + MoveTom.getItemCount()*4000);
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
            mapPrint(tom, mapData);
        });
        rank.setFont(Font.loadFont("file:font/ラノベPOP.otf",28));
        rank.setStyle("-fx-line-spacing: 8px;"+"-fx-stroke: #00CC00;");
        rank.setFill(Color.WHITE);
        yourScore.setFont(Font.loadFont("file:font/ラノベPOP.otf",60));
        yourScore.setTextFill(Color.WHITE);
        label1.setFont(Font.loadFont("file:font/ラノベPOP.otf",28));
    }
    public void resetMap(){
        outputAction("RESET");
        initViews();
        mapData = new MapData(21,15,-1);
        tom = new MoveTom(1, 1, mapData);
        mapData2 = new MapData(21,15,0);
        jerry = new MoveJerry(1, 1, mapData2);
        isgoal = false;
        MoveChara.setItem(0);
        MoveChara.message = "アイテム数: 0";
        mapStack.getChildren().removeAll(goalImageView,ranking,next);
    }
    //DEBUG THROUGH WALL
    public void func1ButtonAction(ActionEvent event) {
        System.out.println(mapData2.getItem());
        mapData.fillMap(MapData.TYPE_NONE);
        mapData.setGoal(19,13);
        mapStack.getChildren().removeAll(goalImageView,ranking,next);
        mapPrint(tom, mapData);
    }
    //DEBUG RESET
    public void func2ButtonAction(ActionEvent event) {
        if(move == false){
            resetMap();
            mapPrint(tom, mapData);
            map2 = false;
        }
    }
    //DEBUG GOAL
    public Timeline timeline;
    public int tmpx,tmpy;
    public boolean move = false;
    public void func3ButtonAction(ActionEvent event) {
        if(map2 == true){
            map2 = false;
            move = true;
            mapPrint(tom, mapData);
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
                            }
                            System.out.println("( "+dx+","+dy+" )");
                                if(Math.abs(dx)>Math.abs(dy)){
                                    if(dx>0 &&mapData.getMap(tom.getPosX() + 1,tom.getPosY())!=MapData.TYPE_WALL){
                                        tom.setCharaDir(MoveChara.TYPE_RIGHT);
                                        tom.move(1, 0);
                                    }
                                    else if(dx<0 &&mapData.getMap(tom.getPosX() - 1,tom.getPosY())!=MapData.TYPE_WALL){
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
                                    if(dy>0&&mapData.getMap(tom.getPosX(),tom.getPosY()+1)!=MapData.TYPE_WALL){
                                        tom.setCharaDir(MoveChara.TYPE_DOWN);
                                        tom.move(0, 1);
                                    }
                                    else if(dy<0&&mapData.getMap(tom.getPosX(),tom.getPosY()-1)!=MapData.TYPE_WALL){
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
                                mapPrint(tom, mapData);
                                goalAction(tom, mapData);
                                tmpx = dx;
                                tmpy = dy;
                        }
                    }
                )
            );
            //タイマーの開始
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }
    }
    public void func4ButtonAction(ActionEvent event) {
        if(move == false){
            map2 = true;
            mapPrint(jerry, mapData2);
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
        if(map2 == true){
            outputAction("DOWN");
            jerry.setCharaDir(MoveChara.TYPE_DOWN);
            jerry.move(0, 1);
            mapPrint(jerry, mapData2);
            goalAction(jerry, mapData2);
        }
    }
    public void downButtonAction(ActionEvent event) {
        downButtonAction();
    }
    public void rightButtonAction(){
        if(map2==true){
            outputAction("RIGHT");
            jerry.setCharaDir(MoveChara.TYPE_RIGHT);
            jerry.move(1, 0);
            mapPrint(jerry, mapData2);
            goalAction(jerry,mapData2);
        }
    }
    public void rightButtonAction(ActionEvent event) {
       rightButtonAction();

    }
    public void leftButtonAction(){    /**左に進む*/
        if(map2==true){
            outputAction("DOWN");
            jerry.setCharaDir(MoveChara.TYPE_LEFT);
            jerry.move(-1, 0);
            mapPrint(jerry, mapData2);
            goalAction(jerry,mapData2);
        }
    }
    public void leftButtonAction(ActionEvent event) {
        leftButtonAction();
    }
    public void upButtonAction(){    /**上に進む*/
        if(map2==true){
            outputAction("UP");
            jerry.setCharaDir(MoveChara.TYPE_UP);
            jerry.move(0, -1);
            mapPrint(jerry, mapData2);
            goalAction(jerry,mapData2);
        }
    }
    public void upButtonAction(ActionEvent event) {
        upButtonAction();
    }
}
