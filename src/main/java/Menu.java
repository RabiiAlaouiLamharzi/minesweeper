import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Menu extends Application {

    @Override
    public void start(Stage mainStage) {
        StackPane menuLayout = createMenuLayout(mainStage);
        mainStage.setTitle("Ocean Rescue");
        mainStage.setScene(new Scene(menuLayout, 20 * 31.5, 12 * 37));
        mainStage.show();
    }

    private StackPane createMenuLayout(Stage mainStage) {
        // Set the background image
        Image backgroundImage = new Image("https://iili.io/dsfTku9.jpg");
        ImageView background = new ImageView(backgroundImage);
        background.setFitWidth(20 * 31.5);
        background.setFitHeight(12 * 37);
        background.setPreserveRatio(false);

        // Create the title text
        Label titleText = new Label("Ocean Rescue");
        titleText.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 64px;");

        // Create buttons for each level
        Button level1Button = createButton("Level 1", mainStage);
        Button level2Button = createButton("Level 2", null);
        Button level3Button = createButton("Level 3", null);

        // Add margins for the buttons
        HBox.setMargin(level1Button, new Insets(0, 0, 30, 0));
        HBox.setMargin(level2Button, new Insets(0, 0, 30, 0));
        HBox.setMargin(level3Button, new Insets(0, 0, 30, 0));
        
        // Arrange buttons in a horizontal box
        HBox buttonContainer = new HBox(10, level1Button, level2Button, level3Button);
        buttonContainer.setTranslateY(200);
        buttonContainer.setStyle("-fx-alignment: center;");

        // Set up the layout
        StackPane rootLayout = new StackPane();
        rootLayout.getChildren().addAll(background, buttonContainer, titleText);

        return rootLayout;
    }

    private Button createButton(String label, Stage targetStage) {
        Button button = new Button(label);
        button.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
        button.setPrefWidth(150);
        button.setPrefHeight(40);

        // Disable the button if it has no associated action
        if (targetStage == null) {
            button.setDisable(true);
        } else {
            button.setOnAction(event -> startLevel(targetStage));
        }

        return button;
    }

    // Switches from the menu to LevelOne (game window)
    private void startLevel(Stage mainStage) {
        mainStage.close();
        LevelOne newLevel = new LevelOne();
        newLevel.display();
    }
    
    public void display() {
        Stage newStage = new Stage();
        start(newStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
