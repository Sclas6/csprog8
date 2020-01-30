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
    public GridPane lifeGrid;
    public StackPane mapStack;
    public ImageView[] mapImageViews;
    public ImageView[] itemImageViews;
    public ImageView[] lifeImageViews;
    //For Goal Jadging
    public boolean isgoal = false;
    //map player seeing
    public static boolean isjerrymap = false;
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
    public static int score;
    public Label time;
    public static Timeline timer;
    public Label item_message;
    public Label life_message;
    public int count;
    public static boolean is_timer_start = false;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        maptom = new MapData(21,15,99);
        tom = new MoveTom(1,1,maptom);
        mapjerry = new MapData(21,15,-99);
        jerry = new MoveJerry(1, 1, mapjerry);
        mapjerry.setItem(0,MapData.Type.jerry);
        mapImageViews = new ImageView[maptom.getHeight()*maptom.getWidth()];
        for(int y=0; y<maptom.getHeight(); y++){
            for(int x=0; x<maptom.getWidth(); x++){
                int index = y*maptom.getWidth() + x;
                mapImageViews[index] = maptom.getImageView(x,y);
            }
        }
        initViews();
        isjerrymap = false;
        Timer(240);
        timer.stop();
        mapPrint(tom, maptom);
    }

    public void mapPrint(MoveChara c, MapData m){
        int cx = c.getPosX();
        int cy = c.getPosY();
        mapGrid.getChildren().clear();
        mapImageViews = new ImageView[m.getHeight()*m.getWidth()];
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
        itemImageViews = new ImageView[Math.max(tom.getItemCount(),jerry.getItemCount())];
        lifeImageViews = new ImageView[5];
        itemGrid.getChildren().clear();
        if(isjerrymap==true){
            for(int i=0;i<jerry.getItemCount();i++){
                itemImageViews[i] = new ImageView(new Image("png/ITEM2.png"));
                itemGrid.add(itemImageViews[i],i,0);
            }
            for(int i=0;i<5;i++){
                if(i<jerry.life){
                    lifeImageViews[i] = new ImageView(new Image("png/LIFE.png"));
                }
                else{
                    lifeImageViews[i] = new ImageView(new Image("png/LIFE_2.png"));
                }
                lifeGrid.add(lifeImageViews[i],i,0);
                life_message.setText("たいりょく:");
            }
        }else{
            for(int i=0;i<tom.getItemCount();i++){
                itemImageViews[i] = new ImageView(new Image("png/ITEM.png"));
                itemGrid.add(itemImageViews[i],i,0);
            }
            lifeGrid.getChildren().clear();
            life_message.setText("");
        }
        if(jerry.life<=0){
            gameOver();
        }
    }

    //run when chara reachs goal
    public void goalAction(MoveChara c,MapData m){
        if (c.isGoal(m)==true && isgoal==false){
            timer.stop();
            isgoal = true;
            makeScore();
            mapStack.getChildren().addAll(goalImageView,ranking,next);
            CsvManager.exportCsv();
            /*while(timer.getStatus()==Status.PAUSED){
                timer.stop();
            }*/
            //is_timer_start = false;
        }
    }
    /**SETTING TIMER */
    public void Timer(int c){
        count = c;
        count += 1;
        timer = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                count -= 1;
                time.setText("残り時間: "+Integer.toString(count));
                if(count<0){
                    gameOver();
                }
            }
        }));
        timer.setCycleCount(c*2);
    }

    public void gameOver(){
        resetMap();
        mapPrint(tom, maptom);
        isjerrymap = false;
        timer.stop();
        time.setText("LOADING...");
        MapGame.gameOver();
        SoundPlayer.stop(Sound.Type.GameBgm);
        SoundPlayer.play(Sound.Type.GameOver);
    }

    /**MAKING SCORE FORMAT */
    public static String getScoreData(){
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm");
        String str = sdf.format(ts);
        String scoreData = MapGame.getName() + "," + getScore() + "," + str;
        return scoreData;
    }

    //**MAKING SCORE */
    public void makeScore(){
        score = (int)(count*1000 + tom.getItemCount()*4000);
    }

    //**GET SCORE */
    public static int getScore(){
        return score;
    }

    //**VIEW RANKING */
    public void viewRanking(){
        mapStack.getChildren().removeAll(scoreWindowView, rank, yourScore);
        String ranking = CsvManager.getRanking();
        yourScore.setText("Your Score: " + Integer.toString(score));
        rank.setText(ranking);
        mapStack.getChildren().addAll(scoreWindowView, rank, yourScore, close);
    }

    //**INITIALIZE VIEWING OBJECT */
    @SuppressWarnings("static-access")
    public void initViews(){
        mapStack.setAlignment(goalImageView, Pos.CENTER);
        mapStack.setAlignment(ranking,Pos.BOTTOM_RIGHT);
        mapStack.setAlignment(next,Pos.BOTTOM_RIGHT);
        mapStack.setAlignment(close,Pos.BOTTOM_RIGHT);
        mapStack.setAlignment(yourScore,Pos.TOP_CENTER);
        mapStack.setAlignment(rank,Pos.CENTER);
        mapStack.setMargin(ranking, new Insets(90,185,65,0));
        mapStack.setMargin(next, new Insets(90,110,65,0));
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
            //mapPrint(tom, maptom);
        });
        rank.setFont(Font.loadFont("file:font/rPOP.otf",28));
        rank.setStyle("-fx-line-spacing: 8px;"+"-fx-stroke: #00CC00;");
        rank.setFill(Color.WHITE);
        yourScore.setFont(Font.loadFont("file:font/rPOP.otf",60));
        yourScore.setTextFill(Color.WHITE);
        item_message.setFont(Font.loadFont("file:font/rPOP.otf",28));
        life_message.setFont(Font.loadFont("file:font/rPOP.otf",28));
        time.setFont(Font.loadFont("file:font/rPOP.otf", 28));
    }

    //**RESET MAP */
    public void resetMap(){
        outputAction("RESET");
        initViews();
        maptom = new MapData(21,15,99);
        tom = new MoveTom(1, 1, maptom);
        mapjerry = new MapData(21,15,-99);
        jerry = new MoveJerry(1, 1, mapjerry);
        isgoal = false;
        tom.setItem(0);
        jerry.setItem(0);
        jerry.setLife(5);
        mapStack.getChildren().removeAll(goalImageView,ranking,next);
        timer.stop();
        Timer(240);
        is_timer_start = false;
        time.setText("LOADING...");
        mapPrint(tom, maptom);
        isjerrymap = false;
        timer.play();
        is_timer_start = true;
    }
    
    //**DEBUG THROUGH WALL */
    /*public void func1ButtonAction(ActionEvent event) {
        System.out.println(mapjerry.getItem());
        maptom.fillMap(MapData.TYPE_NONE);
        mapjerry.fillMap(MapData.TYPE_NONE);
        tom.setItem(5);
        maptom.setGoal(19,13);
        mapStack.getChildren().removeAll(goalImageView,ranking,next);
        mapPrint(tom, maptom);
    }*/

    //**DEBUG RESET */
    /*public void func2ButtonAction(ActionEvent event) {
        if(move == false){
            resetMap();
            mapPrint(tom, maptom);
            isjerrymap = false;
        }
    }*/

    public void func1ButtonAction(ActionEvent event) {
        if(isjerrymap&&jerry.getItemCount()>0&&jerry.life<5){
            jerry.life+=1;
            jerry.setItem(jerry.getItemCount()-1);
            mapPrint(jerry,mapjerry);
            outputAction("HEAL");
        }
    }

    public void func2ButtonAction(ActionEvent event) {
        if(isjerrymap&&jerry.getItemCount()>0){
            int charaDir = jerry.getCharaDir();
            switch (charaDir) {
                case 0:
                    if(mapjerry.getMap(jerry.getPosX(),jerry.getPosY()+1)==MapData.TYPE_WALL&&jerry.getPosY()+1!=14){
                        SoundPlayer.play(Sound.Type.WallEx);
                        mapjerry.setMap(jerry.getPosX(),jerry.getPosY()+1,MapData.TYPE_NONE);
                        mapjerry.setImageViews();
                        jerry.setItem(jerry.getItemCount()-1);
                        mapPrint(jerry,mapjerry);
                    }
                    break;
                case 1:
                    if(mapjerry.getMap(jerry.getPosX()-1,jerry.getPosY())==MapData.TYPE_WALL&&jerry.getPosX()-1!=0){
                        SoundPlayer.play(Sound.Type.WallEx);
                        mapjerry.setMap(jerry.getPosX()-1,jerry.getPosY(),MapData.TYPE_NONE);
                        mapjerry.setImageViews();
                        jerry.setItem(jerry.getItemCount()-1);
                        mapPrint(jerry,mapjerry);
                    }
                    break;
                case 2:
                    if(mapjerry.getMap(jerry.getPosX()+1,jerry.getPosY())==MapData.TYPE_WALL&&jerry.getPosX()+1!=20){
                        SoundPlayer.play(Sound.Type.WallEx);
                        mapjerry.setMap(jerry.getPosX()+1,jerry.getPosY(),MapData.TYPE_NONE);
                        mapjerry.setImageViews();
                        jerry.setItem(jerry.getItemCount()-1);
                        mapPrint(jerry,mapjerry);
                    }
                    break;
                case 3:
                    if(mapjerry.getMap(jerry.getPosX(),jerry.getPosY()-1)==MapData.TYPE_WALL&&jerry.getPosY()-1!=0){
                        SoundPlayer.play(Sound.Type.WallEx);
                        mapjerry.setMap(jerry.getPosX(),jerry.getPosY()-1,MapData.TYPE_NONE);
                        mapjerry.setImageViews();
                        jerry.setItem(jerry.getItemCount()-1);
                        mapPrint(jerry,mapjerry);
                    }
                    break;
            }
        }
    }

    //**TOM AI */
    public void moveTom(){
        timeline = new Timeline(
            new KeyFrame(
                new Duration(200),//1000ミリ秒
                new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event){
                        if((jerry.getPosX()==tom.getPosX())&&(jerry.getPosY()==tom.getPosY())){
                            jerry.life-=1;
                            SoundPlayer.play(Sound.Type.Damage);
                            timeline.stop();
                            move = false;
                            //System.out.println(jerry.life);
                        }
                        int dx,dy;
                        dx = jerry.getPosX()-tom.getPosX();
                        dy = jerry.getPosY()-tom.getPosY();
                        if(dx==tmpx&&dy==tmpy){
                            timeline.stop();
                            move = false;
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

    //**CHANGE TOM VIEW */
    public Timeline timeline;
    public int tmpx,tmpy;
    public boolean move = false;
    public void func3ButtonAction(ActionEvent event) {
        if(isjerrymap == true){
            isjerrymap = false;
            move = true;
            SoundPlayer.play(Sound.Type.Dig);
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
                            SoundPlayer.play(Sound.Type.Dig);
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

    //**CHANGE JERRY VIEW */
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
