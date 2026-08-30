package jeryl;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Controller for the main chat window: wires up the text field, send
 * button, and scrolling dialog list to a Jeryl instance's
 * getResponse(String).
 */
public class MainWindow extends AnchorPane {
    private static final int AVATAR_SIZE = 100;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Jeryl jeryl;

    private final Image userImage = createAvatar(Color.web("#5b8def"));
    private final Image jerylImage = createAvatar(Color.web("#3aa17e"));

    @FXML
    private void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Supplies the Jeryl instance this window sends user input to, and
     * shows its welcome message as the first chat bubble.
     */
    public void setJeryl(Jeryl jeryl) {
        this.jeryl = jeryl;
        dialogContainer.getChildren().add(
                DialogBox.getJerylDialog(jeryl.welcomeMessage(), jerylImage));
    }

    /**
     * Sends the text field's content to Jeryl, shows both the user's
     * input and Jeryl's response as dialog bubbles, and exits the
     * application shortly after a "bye" response is shown.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank()) {
            return;
        }
        String response = jeryl.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getJerylDialog(response, jerylImage));
        userInput.clear();

        if (jeryl.isExit(input)) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(event -> Platform.exit());
            delay.play();
        }
    }

    /**
     * Draws a simple solid-colored circle as a placeholder avatar, so
     * the GUI doesn't depend on external image assets.
     */
    private static Image createAvatar(Color color) {
        WritableImage image = new WritableImage(AVATAR_SIZE, AVATAR_SIZE);
        PixelWriter writer = image.getPixelWriter();
        double radius = AVATAR_SIZE / 2.0;
        for (int y = 0; y < AVATAR_SIZE; y++) {
            for (int x = 0; x < AVATAR_SIZE; x++) {
                double dx = x - radius + 0.5;
                double dy = y - radius + 0.5;
                boolean insideCircle = dx * dx + dy * dy <= radius * radius;
                writer.setColor(x, y, insideCircle ? color : Color.TRANSPARENT);
            }
        }
        return image;
    }
}
