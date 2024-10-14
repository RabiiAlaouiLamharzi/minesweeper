import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class GameWon {

    public void display() {

        Stage celebrationWindow = new Stage();
        celebrationWindow.setResizable(false);
        celebrationWindow.setTitle("Congratulations!");

        // Main winning message
        Label victoryMessage = new Label("YOU WIN!");
        victoryMessage.setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-font-size: 48px;");

        // Message about the achievement
        Label achievementMessage = new Label("Congratulations! You've successfully cleaned the ocean and saved marine life!");

        // Donation link to encourage further support
        Text donationLink = new Text("Donate to help save the ocean");
        donationLink.setFill(javafx.scene.paint.Color.BLUE);
        donationLink.setUnderline(true);
        donationLink.setOnMouseClicked(event -> openDonationLink());

        // Buttons for user actions
        Button restartButton = new Button("Restart Level");
        Button menuButton = new Button("Main Menu");

        restartButton.setOnAction(e -> {
        	celebrationWindow.close(); 
            new LevelOne().display();  
        });
        menuButton.setOnAction(e -> menu(celebrationWindow));
        restartButton.setPrefWidth(150);
        menuButton.setPrefWidth(150);

        HBox buttonLayout = new HBox(10, restartButton, menuButton);
        buttonLayout.setPadding(new Insets(20, 0, 0, 0));
        buttonLayout.setAlignment(Pos.CENTER);

        // Set up the main layout of the window
        VBox mainLayout = new VBox(20, victoryMessage, achievementMessage, donationLink, buttonLayout);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);

        // Create the scene and display it
        Scene scene = new Scene(mainLayout, 20 * 40 + 30, 12 * 40 + 100);
        celebrationWindow.setScene(scene);
        celebrationWindow.show();
    }

    // Switches to the main menu
    public void menu(Stage currentStage) {
        currentStage.close();
        Menu menuWindow = new Menu();
        menuWindow.display();
    }

    // Opening the donation link in a web browser
    private void openDonationLink() {
        try {
            Desktop.getDesktop().browse(new URI("https://theoceancleanup.com/donate/"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
