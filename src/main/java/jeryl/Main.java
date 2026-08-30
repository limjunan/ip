package jeryl;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * JavaFX entry point: creates the primary Scene and Stage. For now this
 * is just a placeholder "Hello World" window; the real Jeryl UI (chat
 * bubbles wired up to the existing Ui/Storage/TaskList/Parser logic)
 * gets built on top of this in a later step.
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Label helloWorld = new Label("Hello World!");
        Scene scene = new Scene(helloWorld);

        stage.setScene(scene);
        stage.show();
    }
}
