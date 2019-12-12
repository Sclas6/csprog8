import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class MapGame extends Application {
    Stage stage;
    MapScene mapScene = new MapScene();
    @Override
    public void start(Stage stage) throws Exception {
    /*    
    stage = primaryStage;
	primaryStage.setTitle("MAP GAME");
	Pane myPane_top = (Pane)FXMLLoader.load(getClass().getResource("MapGame.fxml"));
	Scene myScene = new Scene(myPane_top);
	primaryStage.setScene(myScene);
    primaryStage.show();
    */
        mapScene.primaryStage(stage);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
