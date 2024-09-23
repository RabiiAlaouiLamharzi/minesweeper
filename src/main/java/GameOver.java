import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class GameOver {

    public void display() {

        Stage gameOverWindow = new Stage();
        gameOverWindow.setTitle("Game Over :(");

        // Main game over message
        Label title = new Label("GAME OVER");
        title.setStyle("-fx-text-fill: darkred; -fx-font-weight: bold; -fx-font-size: 48px;");

        // Informative subtitle about what went wrong
        Label subtitle = new Label("You stumbled upon toxic waste! The whole ocean is now infected and marine life is dead :(");
        subtitle.setPadding(new Insets(10, 0, 0, 0));
        subtitle.setWrapText(true);
        subtitle.setMaxWidth(500);

        // Buttons for user actions
        Button restartButton = new Button("Restart Level");
        Button menuButton = new Button("Main Menu");

        restartButton.setOnAction(e -> restart(gameOverWindow));
        menuButton.setOnAction(e -> menu(gameOverWindow));
        restartButton.setPrefWidth(150);
        menuButton.setPrefWidth(150);

        HBox buttonLayout = new HBox(10, restartButton, menuButton);
        buttonLayout.setPadding(new Insets(20, 0, 0, 0));
        buttonLayout.setAlignment(Pos.CENTER);

        // Set up the main layout of the window
        VBox mainLayout = new VBox(20, title, subtitle, buttonLayout);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);

        // Create the scene and display it
        Scene scene = new Scene(mainLayout, 20 * 31.5, 12 * 37);
        gameOverWindow.setScene(scene);
        gameOverWindow.show();
    }

    // Switches to the main menu
    public void menu(Stage currentStage) {
        currentStage.close();
        Menu menuWindow = new Menu();
        menuWindow.display();
    }

    // Restarts the game by closing the current window and re-launching LevelOne
    public void restart(Stage currentStage) {
        currentStage.close();
        LevelOne levelWindow = new LevelOne();
        levelWindow.display();
    }
}
