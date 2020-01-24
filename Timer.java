import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class Timer {
    public static int a = 100;

    Timer() {
        Timeline timer = new Timeline(new KeyFrame(new Duration(1000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                a -= 1;
            }
        }));
        timer.setCycleCount(100);
        timer.play();
    }
}