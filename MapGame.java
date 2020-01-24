import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class MapGame extends Application {
    Stage stage;
    Scene myScene;
    Scene ruleScene;
    //public Image startWindow = new Image("aaa.png");
    //public ImageView startWindowView = new ImageView(startWindow);
    public static String player_name;

    @Override
    public void start(Stage primaryStage) throws Exception {
      	stage = primaryStage;
      	primaryStage.setTitle("MAP GAME");
      	Pane myPane_top = (Pane)FXMLLoader.load(getClass().getResource("MapGame.fxml"));
      	myScene = new Scene(myPane_top);
        Scene startScene = initStartScene();
        stage.setScene(startScene);
        stage.show();
    }
    public static void main(String[] args) {
        //player_name = (args.length!=1)?"guest":args[0];
        launch(args);
    }
    public static String getName(){
        return player_name;
    }

    void gameStart() {
        stage.setScene(myScene);
        stage.show();
    }
    Scene initStartScene() {
        Pane startPane = new Pane();
        Label label = new Label("name: ");
        TextField field = new TextField();
        field.setPromptText("名前を入力してね");

        Button startBtn = new Button("ゲームスタート!");
        Button ruleBtn = new Button("ルール");
        ruleBtn.setPrefWidth(150);
        ruleBtn.setPrefHeight(50);
        startBtn.setPrefWidth(150);
        startBtn.setPrefHeight(50);
        BorderPane pane = new BorderPane();

        StackPane stackPane = new StackPane();
        stackPane.setAlignment(label,Pos.BOTTOM_CENTER);
        stackPane.setAlignment(ruleBtn,Pos.CENTER);
        stackPane.setAlignment(field,Pos.BOTTOM_CENTER);
        stackPane.setAlignment(startBtn,Pos.TOP_CENTER);
        stackPane.setMargin(label,new Insets(90,140,88,0));
        stackPane.setMargin(ruleBtn,new Insets(90,65,88,0));
        stackPane.setMargin(field,new Insets(0,40,32,0));
        stackPane.setMargin(startBtn,new Insets(38,0,0,0));
        stackPane.getChildren().addAll(label,field,startBtn,ruleBtn);
        startBtn.setOnMouseClicked(event -> {
        player_name = (field.getText().equals(""))?"guest":field.getText();
        System.out.println("["+player_name+"]");
        gameStart();
        }
            );
        ruleBtn.setOnMouseClicked(event -> gameStart());

        return new Scene(stackPane);
    }
}
