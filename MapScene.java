import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.*;

public class MapScene{
    public void primaryStage(Stage stage) throws Exception {
        stage.setTitle("MAP GAME");
        Pane myPane_top = (Pane)FXMLLoader.load(getClass().getResource("MapGame.fxml"));
        Scene myScene = new Scene(myPane_top);
        stage.setScene(myScene);
        stage.show();
    }

}