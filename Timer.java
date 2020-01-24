import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
public class Timer extends Application{
  public static void main(String[] args) {
    launch(args);
  }
  @Override
  public void start(Stage stage) throws Exception{
    stage.setTitle("Time");
    stage.setHeight(100);
    stage.setWidth(300);
    Label label = new Label();
    label.setText("60");
    label.setFont(new Font(20));
    Group g = new Group();
    g.getChildren().add(label);
    Scene scene = new Scene(g);
    Timeline timer = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                label.setText(String.valueOf(Integer.parseInt(label.getText()) - 1));
            }
        }));
        timer.setCycleCount(60);
        timer.play();
    stage.setScene(scene);
    stage.show();
  }
}
