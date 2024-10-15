import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Menu extends Application {
	

    @Override
    public void start(Stage mainStage) {
        StackPane menuLayout = createMenuLayout(mainStage);
        mainStage.setResizable(false);
        mainStage.setTitle("Ocean Rescue");
        mainStage.setScene(new Scene(menuLayout, 20 * 40 + 30, 12 * 40 + 100));
        mainStage.show();
    }

    private StackPane createMenuLayout(Stage mainStage) {
        
        // Set the background image
        Image backgroundImage = new Image("https://iili.io/dsfTku9.jpg");
        ImageView background = new ImageView(backgroundImage);
        background.setFitWidth(20 * 40 + 30);
        background.setFitHeight(12 * 40 + 100);
        background.setPreserveRatio(false);

        // Create the title text
        Label titleText = new Label("Ocean Rescue");
        titleText.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 64px;");

        // Create buttons for each level
        Button level1Button = createButton1("Level 1", mainStage);
        Button level2Button = createButton2("Level 2", mainStage);
        Button level3Button = createButton3("Level 3", mainStage);

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

    private Button createButton1(String label, Stage targetStage) {
    	
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/Segoe UI Bold.ttf"), 20);
    	
        Button button = new Button(label);
        button.setFont(customFont);
        
        button.setStyle(
        	    "-fx-padding: 10px 50px; " +  
        	    "-fx-background-radius: 50px; " +  
        	    "-fx-border-radius: 100%;"
        	);

        // Disable the button if it has no associated action
        if (targetStage == null) {
            button.setDisable(true);
        } else {
            button.setOnAction(event -> startLevel1(targetStage));
        }

        return button;
    }

    // Switches from the menu to LevelOne (game window)
    private void startLevel1(Stage mainStage) {
        mainStage.close();
        LevelOne newLevel = new LevelOne();
        newLevel.display1();
    }
    
    private Button createButton2(String label, Stage targetStage) {
    	
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/Segoe UI Bold.ttf"), 20);
    	
        Button button = new Button(label);
        button.setFont(customFont);
        
        button.setStyle(
        	    "-fx-padding: 10px 50px; " +  
        	    "-fx-background-radius: 50px; " +  
        	    "-fx-border-radius: 100%;"
        	);

        // Disable the button if it has no associated action
        if (targetStage == null) {
            button.setDisable(true);
        } else {
            button.setOnAction(event -> startLevel2(targetStage));
        }

        return button;
    }
    
    // Switches from the menu to LevelOne (game window)
    private void startLevel2(Stage mainStage) {
        mainStage.close();
        LevelTwo newLevel = new LevelTwo();
        newLevel.display2();
    }
    
    private Button createButton3(String label, Stage targetStage) {
    	
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/Segoe UI Bold.ttf"), 20);
    	
        Button button = new Button(label);
        button.setFont(customFont);
        
        button.setStyle(
        	    "-fx-padding: 10px 50px; " +  
        	    "-fx-background-radius: 50px; " +  
        	    "-fx-border-radius: 100%;"
        	);

        // Disable the button if it has no associated action
        if (targetStage == null) {
            button.setDisable(true);
        } else {
            button.setOnAction(event -> startLevel3(targetStage));
        }

        return button;
    }
    
    // Switches from the menu to LevelOne (game window)
    private void startLevel3(Stage mainStage) {
        mainStage.close();
        LevelThree newLevel = new LevelThree();
        newLevel.display3();
    }
    
    public void display() {
        Stage newStage = new Stage();
        start(newStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
