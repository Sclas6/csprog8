import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class MapGame extends Application {
    public static Stage stage;
    public static Scene myScene;
    Scene ruleScene;
    public static String player_name;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        primaryStage.setTitle("MAP GAME");
        Pane myPane_top = (Pane) FXMLLoader.load(getClass().getResource("MapGame.fxml"));
        myScene = new Scene(myPane_top);
        Scene startScene = initStartScene();
        stage.setScene(startScene);
        stage.show();
        SoundPlayer.play(Sound.Type.MenuBgm);
    }

    public static void main(String[] args) {
        // player_name = (args.length!=1)?"guest":args[0];
        launch(args);
    }

    public static String getName() {
        return player_name;
    }

    public static void gameStart() {
        stage.setScene(myScene);
        stage.show();
        MapGameController.timer.play();
        MapGameController.is_timer_start=false;
        SoundPlayer.stop(Sound.Type.MenuBgm);
        SoundPlayer.play(Sound.Type.GameBgm);
    }
    @SuppressWarnings("static-access")
    public static void gameOver(){
        Image overImage = new Image("png/OVER.png");
        ImageView overImageView = new ImageView(overImage);
        Button contBtn = new Button("Continue");
        StackPane sp = new StackPane();
        contBtn.setOnMouseClicked(event -> gameStart());
        sp.setAlignment(contBtn,Pos.BOTTOM_RIGHT);
        sp.setMargin(contBtn,new Insets(0,10,10,0));
        sp.getChildren().addAll(overImageView,contBtn);
        Scene overScene = new Scene(sp);
        
        stage.setScene(overScene);
        stage.show();
    }
    @SuppressWarnings("static-access")
    Scene initStartScene() {
        FlowPane startPane = new FlowPane();
        startPane.setPadding(new Insets(5, 5, 5, 5));
        Label label = new Label("name: ");
        TextField field = new TextField();
        field.setPromptText("名前を入力してね");
        Image startImage = new Image("png/START.png");
        ImageView startImageView = new ImageView(startImage);
        Button startBtn = new Button("ゲームスタート!");
        Button ruleBtn = new Button("ルール");
        ruleBtn.setPrefWidth(150);
        ruleBtn.setPrefHeight(50);
        startBtn.setPrefWidth(150);
        startBtn.setPrefHeight(50);
        startPane.getChildren().addAll(startImageView,ruleBtn,startBtn,label,field);
        startPane.setMargin(label,new Insets(0,0,0,80));
        startBtn.setOnMouseClicked(event -> {
        player_name = (field.getText().equals(""))?"guest":field.getText();
        System.out.println("["+player_name+"]");
        gameStart();
        }
            );
        ruleBtn.setOnMouseClicked(event -> gameStart());

        return new Scene(startPane,680,510);
    }
}
