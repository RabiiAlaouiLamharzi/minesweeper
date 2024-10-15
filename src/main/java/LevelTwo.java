import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.InputStream;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.input.MouseEvent;

public class LevelTwo {

    private static final int ROWS = 12;
    private static final int COLS = 20;
    private static final int NUM_MINES = 10;
    private Button[][] boardButtons = new Button[ROWS][COLS];
    private boolean[][] mineLocations = new boolean[ROWS][COLS];
    private boolean[][] revealedCells = new boolean[ROWS][COLS];
    private boolean[][] flaggedCells = new boolean[ROWS][COLS];
    private static final int CELL_SIZE = 40;
    private boolean gameOverflag = false;
    private int scoreValue = 0;
    private int flagCount = 2;
    private Map<String, String> referenceAnswers = new HashMap<>();
    private int hintCount = 1;
    private Stage hintPopup;
    private Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/Segoe UI.ttf"), 14);
    
    public void display2() {
    	// Create the main window and make it non-resizable
    	Stage mainWindow = new Stage();
    	mainWindow.setResizable(false);

    	// Create labels for score and level
    	Label score = new Label("Score : 0");
    	Label level = new Label("Level : 2");
    	score.setFont(customFont);
    	level.setFont(customFont);
    	VBox vbox1 = new VBox(5, score, level);

    	// Set up labels to display the number of mines
    	Label minesemoji = new Label("🦠");
    	Label mines = new Label("x " + NUM_MINES);
    	mines.setFont(customFont);
    	HBox hboxmine = new HBox(5, minesemoji, mines);

    	// Create square label and cell count
    	Label square = new Label("◼︎︎");
    	square.setStyle("-fx-font-weight: bold; -fx-text-fill: navy; -fx-font-size: 27px; -fx-padding: -11 -1 0 0;");
    	Label cells = new Label("x 125");
    	cells.setFont(customFont);
    	HBox hboxcell = new HBox(5, square, cells);

    	// Group mine and cell info in a vertical box
    	VBox vbox2 = new VBox(5, hboxmine, hboxcell);

    	// Create timer labels
    	Label time = new Label("Time Spent");
    	time.setFont(customFont);
    	Label spent = new Label("0 s");
    	spent.setFont(customFont);
    	VBox vbox3 = new VBox(5, time, spent); 

    	// Set up a timer to update every second
    	long startTime = System.currentTimeMillis();
    	Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
    	    long elapsedMillis = System.currentTimeMillis() - startTime;
    	    long elapsedSeconds = elapsedMillis / 1000;
    	    spent.setText(elapsedSeconds + " s");
    	}));
    	timeline.setCycleCount(Timeline.INDEFINITE);
    	timeline.play();

    	// Create animal-saving labels
    	Label animals = new Label("Animals To Save");
    	animals.setFont(customFont);
    	Label saved = new Label("🐬🦑🦀🐟🐠🐙");
    	VBox vbox4 = new VBox(5, animals, saved); 

    	// Set up buttons for reload, menu, flag, and help
    	Button restart = new Button("Reload");
    	restart.setFont(customFont);
    	restart.setStyle("-fx-font-size: 15px; -fx-padding: 5px 20px; -fx-background-radius: 50px; -fx-border-radius: 100%;");
    	restart.setOnAction(e -> restart(mainWindow));

    	Button menu = new Button("Menu");
    	menu.setFont(customFont);
    	menu.setStyle("-fx-font-size: 15px; -fx-padding: 5px 20px; -fx-background-radius: 50px; -fx-border-radius: 100%;");
    	menu.setOnAction(e -> menu(mainWindow));

    	Button flag = new Button("Flag");
    	flag.setFont(customFont);
    	flag.setStyle("-fx-font-size: 15px; -fx-padding: 5px 20px; -fx-background-radius: 50px; -fx-border-radius: 100%;");
    	flag.setOnAction(e -> setupFlagControl(mainWindow));

    	Button chat = new Button("Help");
    	chat.setFont(customFont);
    	chat.setStyle("-fx-font-size: 15px; -fx-padding: 5px 20px; -fx-background-radius: 50px; -fx-border-radius: 100%;");
    	chat.setOnAction(e -> openChat());

    	setupFlagDragAndDrop(flag);

    	// Group the buttons horizontally
    	HBox hbox1 = new HBox(5, restart, menu, flag, chat);
    	hbox1.setAlignment(Pos.CENTER);

    	// Group everything into a main layout
    	HBox hbox = new HBox(35, vbox1, vbox2, vbox3, vbox4, hbox1);
    	hbox.setAlignment(Pos.CENTER);

    	// Set up the main layout and game board
    	BorderPane layout = new BorderPane();
    	GridPane gameBoard = new GridPane();

    	layout.setTop(hbox);
    	BorderPane.setMargin(hbox, new Insets(10, 10, 10, 10));
    	layout.setCenter(gameBoard);
    	layout.setPadding(new Insets(15));

    	// Create the game scene
    	Scene gameScene = new Scene(layout, COLS * CELL_SIZE + 30, ROWS * CELL_SIZE + 100);
    	mainWindow.setTitle("Ocean Rescue - Level 2");
    	mainWindow.setScene(gameScene);
    	mainWindow.centerOnScreen();
    	mainWindow.show();

    	// Set up game board and gameplay mechanics
    	setupGameBoard(gameBoard, mainWindow, score);
    	scatterMines();
    	preRevealCells();

    	// Handle key press events
    	gameScene.setOnKeyPressed(event -> {
    	    if (event.getCode() == KeyCode.SPACE) {
    	        revealOneMine(mainWindow);
    	    }
    	});

    	// Focus the game board on mouse click
    	gameScene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> gameBoard.requestFocus());
    	gameBoard.requestFocus();
        
        referenceAnswers.put("ocean rescue", "OceanRescue is a game that aims to raise awareness about the importance of protecting the ocean and its endangered species, inspired by the classic Minesweeper gameplay.");
        referenceAnswers.put("levels", "The game consists of three levels: easy, medium, and hard. As you progress, the game features larger ocean grids and more toxic waste.");
        referenceAnswers.put("mines", "Instead of regular mines, the game features toxic waste, which represents pollution and hazards to marine life. If a player encounters toxic waste, all marine life in that area is affected, leading to a game over.");
        referenceAnswers.put("cell", "Clicking on a cell reveals its status. If it contains toxic waste, a mine icon is displayed, triggering a game over. If it is safe, the cell displays the number of adjacent mines or a random animal emoji if there are no adjacent mines.");
        referenceAnswers.put("control buttons", "The game includes several control buttons: Reload Button (restarts the game), Menu Button (opens a main menu), Add Flag Button (initiates the process to flag a cell), and Chat Button (opens a chat window for user interaction).");
        referenceAnswers.put("flag", "Users can flag cells by dragging the flag icon from the flag button and dropping it onto the desired cell. If a flagged cell is clicked again, the flag is removed.");
        referenceAnswers.put("game over", "If a player reveals a mine or places a flag incorrectly, a game-over screen is displayed with a message indicating the loss and an option to close the game.");
        referenceAnswers.put("time", "A timer counts the elapsed time in seconds from when the game starts until it ends, providing players with a sense of urgency and achievement.");
        referenceAnswers.put("chat function", "The Chat Button opens a chat window that allows players to interact with an AI for guidance and information regarding game rules and other inquiries.");
        referenceAnswers.put("popup messages", "There are several popup messages designed to inform players, including: No More Flags Popup (alerts players when they have no flags left) and Flag Information Popup (provides rules and the current number of flags available).");
        referenceAnswers.put("restart", "Players can click the Reload Button to restart the game, allowing them to try again with the same level settings.");
        referenceAnswers.put("eclipse", "1. Install Eclipse IDE. 2. Clone the repository from GitHub. 3. Import the project as a Maven project. 4. Download Java (22.0.2 recommended) and JavaFX SDK (21.0.4 recommended). 5. Add JavaFX libraries to the project. 6. Configure JavaFX in the Run Configuration.");
        referenceAnswers.put("libraries", "The project primarily utilizes JavaFX for UI, along with OkHttp for API calls, and Gson for parsing JSON responses.");
        referenceAnswers.put("difficulty", "As players progress through the levels, the game features larger grids and increased amounts of toxic waste, challenging players to navigate safely.");
        referenceAnswers.put("programming languages", "The game is developed using Java and JavaFX for the graphical user interface and interactions.");
        referenceAnswers.put("hint", "To activate the hint, press the space bar on your keyboard. This will reveal and flag one of the mines for you. Please note that you only have one hint available per level");
    }
    
    // Reveals one hidden mine on the board if hints are available
    private void revealOneMine(Stage mainWindow) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (mineLocations[row][col] && !revealedCells[row][col] && hintCount > 0) {
                    revealedCells[row][col] = true;
                    
                    Image flagImage = new Image(getClass().getResourceAsStream("/assets/skull.gif"));

                    ImageView flagImageView = new ImageView(flagImage);
                    flagImageView.setFitWidth(30);
                    flagImageView.setFitHeight(30);

                    boardButtons[row][col].setGraphic(flagImageView);

                    boardButtons[row][col].setStyle("-fx-background-color: white; -fx-border-color: white; -fx-border-width: 0.5;");
                    hintCount--;
                    return;
                }
                else if (hintCount == 0) {
                	setupHintControl(mainWindow);
                }
            }
        }
    }
    
    // Displays a popup when no more hints are available for the current level
    private void setupHintControl(Stage mainWindow) {
        if (hintPopup != null && hintPopup.isShowing()) {
            hintPopup.toFront();
            return;
        }

        hintPopup = new Stage();
        hintPopup.initModality(Modality.APPLICATION_MODAL);
        hintPopup.setTitle("Hints Information");

        VBox popupLayout = new VBox(15);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.setPadding(new Insets(10));

        Label message = new Label("No more hints available for this level!");
        message.setFont(customFont);
        message.setStyle("-fx-text-alignment: center;");
        message.setWrapText(true);

        Image lightGif = new Image(getClass().getResourceAsStream("/assets/cry.gif"));
        ImageView gifView = new ImageView(lightGif);
        gifView.setFitWidth(100);
        gifView.setFitHeight(100);

        Button closeButton = new Button("Got it!");
        closeButton.setFont(customFont);
        closeButton.setStyle(
        	    "-fx-font-size: 15px; " +
        	    "-fx-padding: 5px 20px; " +  
        	    "-fx-background-radius: 50px; " +  
        	    "-fx-border-radius: 100%;"
        	);
        closeButton.setOnAction(ev -> hintPopup.close());

        popupLayout.getChildren().addAll(gifView, message, closeButton);

        Scene popupScene = new Scene(popupLayout, 300, 250);
        popupLayout.setPadding(new Insets(5, 35, 5, 35));
        hintPopup.setScene(popupScene);

        hintPopup.show();
    }
    
    // Displays a popup with instructions on how to use the flag control
    private void setupFlagControl(Stage mainWindow) {
        if (hintPopup != null && hintPopup.isShowing()) {
            hintPopup.toFront();
            return;
        }

        hintPopup = new Stage();
        hintPopup.initModality(Modality.APPLICATION_MODAL);
        hintPopup.setTitle("Flag Information");

        VBox popupLayout = new VBox(15);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.setPadding(new Insets(10));

        Label message = new Label("You must drag the flag button to the cell you wish to designate as a flag."
                                  + "You have only " + flagCount + " flags available for this level.");
        message.setFont(customFont);
        message.setStyle("-fx-text-alignment: center;");
        message.setWrapText(true);

        Image lightGif = new Image(getClass().getResourceAsStream("/assets/light.gif"));
        ImageView gifView = new ImageView(lightGif);
        gifView.setFitWidth(100);
        gifView.setFitHeight(100);

        Button closeButton = new Button("Got it!");
        closeButton.setFont(customFont);
        closeButton.setStyle(
        	    "-fx-font-size: 15px; " +
        	    "-fx-padding: 5px 20px; " +  
        	    "-fx-background-radius: 50px; " +  
        	    "-fx-border-radius: 100%;"
        	);
        closeButton.setOnAction(ev -> hintPopup.close());

        popupLayout.getChildren().addAll(gifView, message, closeButton);

        Scene popupScene = new Scene(popupLayout, 300, 350);
        popupLayout.setPadding(new Insets(5, 35, 15, 35));
        hintPopup.setScene(popupScene);

        hintPopup.show();
    }
    
    // Opens a chat window where the user can type messages and receive responses
    private void openChat() {
        Stage chatStage = new Stage();
        chatStage.setTitle("Chatbot");

        VBox chatLayout = new VBox(15);
        chatLayout.setPadding(new Insets(30));

        TextArea chatHistory = new TextArea();
        chatHistory.setEditable(false);
        chatHistory.setWrapText(true);

        TextArea userInput = new TextArea();
        userInput.setPromptText("Type your message here...");
        userInput.setFont(customFont);
        
        userInput.setStyle(
        	    "-fx-pref-height: 20px;"
        	);

        Button sendButton = new Button("Send");
        sendButton.setFont(customFont);
        Button closeButton = new Button("Close");
        closeButton.setFont(customFont);
        sendButton.setStyle(
                "-fx-font-size: 15px; " +
                "-fx-padding: 5px 20px; " +
                "-fx-background-radius: 50px; " +
                "-fx-border-radius: 100%;"
            );
        closeButton.setStyle(
                "-fx-font-size: 15px; " +
                "-fx-padding: 5px 20px; " +
                "-fx-background-radius: 50px; " +
                "-fx-border-radius: 100%;"
            );
        closeButton.setOnAction(e -> chatStage.close());
        HBox chatbot = new HBox(5, sendButton, closeButton);
        sendButton.setOnAction(event -> {
            String userMessage = userInput.getText();
            chatHistory.appendText("You : " + userMessage + "\n");
            chatHistory.setFont(customFont);

            generateResponse(userMessage, chatHistory);

            userInput.clear();
        });

        chatLayout.getChildren().addAll(chatHistory, userInput, chatbot);

        Scene chatScene = new Scene(chatLayout, 400, 300);
        chatStage.setScene(chatScene);
        chatStage.show();
    }

    private void generateResponse(String userMessage, TextArea chatHistory) {
        String response = getResponseBasedOnQuestion(userMessage);
        if (response != null) {
            chatHistory.appendText("Bot : " + response + "\n");
        } else {
            chatHistory.appendText("Bot : I'm sorry, I can only answer questions related to the Ocean Rescue game.\n");
        }
    }

    private String getResponseBasedOnQuestion(String userMessage) {
        String lowerCaseMessage = userMessage.toLowerCase();

        for (Map.Entry<String, String> entry : referenceAnswers.entrySet()) {
            if (lowerCaseMessage.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

     // Sets up drag-and-drop functionality for the flag button to allow players to mark cells as flagged
    private void setupFlagDragAndDrop(Button flagButton) {
        flagButton.setOnDragDetected(event -> {
            Dragboard db = flagButton.startDragAndDrop(TransferMode.COPY);

            ClipboardContent content = new ClipboardContent();
            content.putString("☠️");
            db.setContent(content);

            Image dragView = new Image(getClass().getResourceAsStream("/assets/skull.gif"));

            ImageView flagImageView = new ImageView(dragView);
            flagImageView.setFitWidth(35);
            flagImageView.setFitHeight(35);

            SnapshotParameters sp = new SnapshotParameters();
            sp.setFill(Color.TRANSPARENT);
            WritableImage image = flagImageView.snapshot(sp, null);

            db.setDragView(image);

            event.consume();
        });
    }

    // Initializes the game board with buttons representing each cell and sets up click and drag-and-drop actions for each
    private void setupGameBoard(GridPane gameBoard, final Stage mainWindow, Label score) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Button cellButton = new Button();
                cellButton.setMinSize(30, 30);
                final int currentRow = row;
                final int currentCol = col;
                cellButton.setMinSize(CELL_SIZE, CELL_SIZE);
                cellButton.setMaxSize(CELL_SIZE, CELL_SIZE);

                cellButton.setStyle("-fx-background-color: navy;" +
                                     "-fx-text-fill: white; -fx-font-weight: bold; -fx-border-color: white; -fx-border-width: 0.5;");

                cellButton.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        handleCellClick(currentRow, currentCol, mainWindow, score);
                    }
                });

                setupCellDragAndDrop(cellButton, currentRow, currentCol, mainWindow);

                boardButtons[row][col] = cellButton;
                gameBoard.add(cellButton, col, row);
            }
        }
    }

    
    private void setupCellDragAndDrop(Button cellButton, int row, int col, Stage mainWindow) {
        cellButton.setOnDragOver(event -> {
            if (event.getGestureSource() != cellButton && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        cellButton.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString() && !revealedCells[row][col]) {
                if (flaggedCells[row][col]) {
                    removeFlag(row, col);
                } else {
                    placeFlag(row, col, mainWindow);
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    // Handles click actions on a cell, revealing mines, updating the score, etc.
    private void handleCellClick(int row, int col, Stage mainWindow, Label score) {
        boardButtons[row][col].setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                handleRightClick(row, col, mainWindow);
            } else if (event.getButton() == MouseButton.PRIMARY) {
                if (gameOverflag || flaggedCells[row][col]) {
                    return;
                }

                if (revealedCells[row][col]) {
                    return;
                }

                revealedCells[row][col] = true;
                if (mineLocations[row][col]) {
                    boardButtons[row][col].setText("️🦠️");
                    boardButtons[row][col].setStyle("-fx-background-color: linear-gradient(from 20% 20% to 100% 100%, black, green); -fx-text-fill: white; -fx-font-weight: bold; -fx-border-width: 0; -fx-alignment: center; -fx-padding: 0;");
                    revealAllMines(mainWindow);
                } else {
                    scoreValue += 100;
                    score.setText("Score: " + scoreValue);
                    
                    int nearbyMines = countAdjacentMines(row, col);
                    if (nearbyMines > 0) {
                        boardButtons[row][col].setText(String.valueOf(nearbyMines));
                    } else {
                        String[] emojis = {"🐬", "🦑", "🦀", "🐟", "🐠", "🐙"};
                        Random random = new Random();
                        String randomEmoji = emojis[random.nextInt(emojis.length)];
                        boardButtons[row][col].setText(randomEmoji);
                        boardButtons[row][col].setStyle("-fx-background-color: blue;" +
                                "-fx-text-fill: white; -fx-font-weight: bold; -fx-border-color: white; -fx-border-width: 0.5; -fx-alignment: center; -fx-padding: 0;");
                        revealSurroundingCells(row, col);
                    }
                    checkIfGameWon(mainWindow);
                }
            }
        });
    }

    private void handleRightClick(int row, int col, Stage mainWindow) {
        if (revealedCells[row][col]) {
            String cellContent = boardButtons[row][col].getText();
            showAnimalInfoPopup(cellContent, mainWindow);
        }
    }

    // Displays a popup with information about an animal represented by the given emoji
    private void showAnimalInfoPopup(String emoji, Stage mainWindow) {
        String animalInfo = getAnimalInfo(emoji);

        Stage hintPopup = new Stage();
        hintPopup.initModality(Modality.APPLICATION_MODAL);
        hintPopup.setTitle(emoji + " Information");

        VBox popupLayout = new VBox(15);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.setPadding(new Insets(10));

        Label message = new Label(animalInfo);
        message.setFont(customFont);
        message.setStyle("-fx-text-alignment: center;");
        message.setWrapText(true);

        Image lightGif;
        switch (emoji) {
            case "🐬":
                lightGif = new Image(getClass().getResourceAsStream("/assets/dolphin.gif"));
                break;
            case "🦑":
                lightGif = new Image(getClass().getResourceAsStream("/assets/squid.gif"));
                break;
            case "🦀":
                lightGif = new Image(getClass().getResourceAsStream("/assets/crab.gif"));
                break;
            case "🐟":
                lightGif = new Image(getClass().getResourceAsStream("/assets/fishblue.gif"));
                break;
            case "🐠":
                lightGif = new Image(getClass().getResourceAsStream("/assets/fishorange.gif"));
                break;
            case "🐙":
                lightGif = new Image(getClass().getResourceAsStream("/assets/octopus.gif"));
                break;
            default:
                lightGif = new Image(getClass().getResourceAsStream("/assets/default.gif"));
                break;
        }
        
        ImageView gifView = new ImageView(lightGif);
        gifView.setFitWidth(100);
        gifView.setFitHeight(100);

        Button closeButton = new Button("Got it!");
        closeButton.setFont(customFont);
        closeButton.setStyle(
            "-fx-font-size: 15px; " +
            "-fx-padding: 5px 20px; " +  
            "-fx-background-radius: 50px; " +  
            "-fx-border-radius: 100%;"
        );
        closeButton.setOnAction(ev -> hintPopup.close());

        popupLayout.getChildren().addAll(gifView, message, closeButton);

        Scene popupScene = new Scene(popupLayout, 300, 300);
        popupLayout.setPadding(new Insets(5, 35, 5, 35));
        hintPopup.setScene(popupScene);

        hintPopup.show();
    }

    private String getAnimalInfo(String emoji) {
        switch (emoji) {
            case "🐬":
                return "Vaquita: A small porpoise found only in the northern part of the Gulf of California, Mexico, critically endangered due to bycatch in fishing nets.";

            case "🦑":
                return "Southern Arrowhead: Overfishing and habitat loss threaten various squid species.";

            case "🦀":
                return "Coconut Crab: The largest terrestrial arthropod, threatened by habitat destruction and overharvesting.";

            case "🐟":
                return "Atlantic Salmon: Overfishing, habitat loss, and pollution have critically endangered wild populations.";

            case "🐠":
                return "Clownfish: Threatened by habitat destruction and the aquarium trade.";

            case "🐙":
                return "Octopus vulgaris: Overfishing and habitat degradation threaten their populations.";

            default:
                return "Unknown animal: No information available.";
        }
    }

    // Places a flag at the specified cell if conditions allow
    private void placeFlag(int row, int col, Stage mainWindow) {
        if (!revealedCells[row][col]) {
            if (flagCount > 0) {
                flaggedCells[row][col] = true;

                if (!mineLocations[row][col]) {
                    showGameOverScreen(mainWindow);
                    return;
                }

                Image flagImage = new Image(getClass().getResourceAsStream("/assets/skull.gif"));

                ImageView flagImageView = new ImageView(flagImage);
                flagImageView.setFitWidth(30);
                flagImageView.setFitHeight(30);

                boardButtons[row][col].setGraphic(flagImageView);

                boardButtons[row][col].setStyle("-fx-background-color: white; -fx-border-color: white; -fx-border-width: 0.5;");
                
                flagCount--;
            } else {
                showNoMoreFlagsPopup();
            }
        }
    }

    // Displays the game over screen with a message and options to restart or close the game
    private void showGameOverScreen(Stage mainWindow) {
        Stage gameOverStage = new Stage();
        gameOverStage.initModality(Modality.APPLICATION_MODAL);
        gameOverStage.setTitle("Game Over");

        VBox popupLayout = new VBox(15);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.setPadding(new Insets(10));

        Label message = new Label("Game Over! You placed a flag on a non-mine cell.");
        message.setWrapText(true);
        message.setStyle("-fx-text-alignment: center;");

        InputStream is = getClass().getResourceAsStream("/assets/angry.gif");
        
        Image lightGif = new Image(is);
        ImageView gifView = new ImageView(lightGif);
        gifView.setFitWidth(100);
        gifView.setFitHeight(100);

        Button closeButton = new Button("Got it!");
        closeButton.setStyle(
        	    "-fx-font-size: 15px; " +
        	    "-fx-padding: 5px 20px; " +  
        	    "-fx-background-radius: 50px; " +  
        	    "-fx-border-radius: 100%;"
        	);
        closeButton.setOnAction(e -> {
        	gameOverStage.close(); 
        	mainWindow.close();
            new LevelOne().display();  
        });
        
        gameOverStage.setOnCloseRequest(event -> {
        	mainWindow.close();
        	new LevelOne().display();
        });

        popupLayout.getChildren().addAll(gifView, message, closeButton);

        Scene popupScene = new Scene(popupLayout, 300, 250);
        popupLayout.setPadding(new Insets(5, 35, 5, 35));
        gameOverStage.setScene(popupScene);
        gameOverStage.showAndWait();
    }

    // Displays a popup when there are no more flags available for the level
    private void showNoMoreFlagsPopup() {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("No More Flags");

        VBox popupLayout = new VBox(15);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.setPadding(new Insets(10));

        Label message = new Label("No more flags available for this level.");
        message.setWrapText(true);
        message.setStyle("-fx-text-alignment: center;");

        InputStream is = getClass().getResourceAsStream("/assets/cry.gif");
        
        Image lightGif = new Image(is);
        ImageView gifView = new ImageView(lightGif);
        gifView.setFitWidth(100);
        gifView.setFitHeight(100);

        Button closeButton = new Button("Got it!");
        closeButton.setStyle(
        	    "-fx-font-size: 15px; " +
        	    "-fx-padding: 5px 20px; " +  
        	    "-fx-background-radius: 50px; " +  
        	    "-fx-border-radius: 100%;"
        	);
        closeButton.setOnAction(ev -> popup.close());

        popupLayout.getChildren().addAll(gifView, message, closeButton);

        Scene popupScene = new Scene(popupLayout, 300, 250);
        popupLayout.setPadding(new Insets(5, 35, 5, 35));
        popup.setScene(popupScene);
        popup.showAndWait();
    }


    private void removeFlag(int row, int col) {
        flaggedCells[row][col] = false;
        boardButtons[row][col].setText("");
        boardButtons[row][col].setStyle("-fx-background-color: navy;" +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-border-color: white; -fx-border-width: 0.5;");
    }
    
    // Pre-reveals specific cells on the game board (land cells used for decoration purposes)
    private void preRevealCells() {
    	int[][] cellsToReveal = {
            	{0,0}, {0,1}, {0,2}, {0,3}, {0,4}, {0,5}, {0,6},
            	{1,0}, {1,1}, {1,2}, {1,3}, {1,4}, {1,5},
            	{2,0}, {2,1}, {2,2}, {2,3}, {2,4}, {2,5},
            	{3,0}, {3,1}, {3,2}, {3,3}, {3,4},
            	{4,0}, {4,1}, {4,2}, {4,3},
            	{5,0}, {5,1}, {5,2},
            	{6,0},
            	{7,0}, {7,1}, {7,2}, {7,3},
            	{8,0}, {8,1}, {8,2}, {8,3},
            	{9,0}, {9,1}, {9,2}, {9,3},
            	{10,0}, {10,1}, {10,2}, {10,3}, {10,4},
            	{11,0}, {11,1}, {11,2}, {11,3}, {11,4}, {11,5},
            	
            	        {0,19}, {0,18}, {0,17}, {0,16}, {0,15},
            	{1,19}, {1,18}, {1,17}, {1,16}, {1,15}, {1,14},
            	        {2,19}, {2,18}, {2,17}, {2,16}, {2,15},
            	                {3,18}, {3,17}, {3,16},
            	{4,18}, {4,17}, {4,16}, {4,15}, {4,14},
            	{5,18}, {5,17}, {5,16}, {5,15}, {5,14},
            	{6,18}, {6,17}, {6,16}, {6,15}, {6,14},
            	{7,17}, {7,16}, {7,15}, {7,14},
            	        {8,15},
            	{9,19}, {9,18}, {9,17}, {9,16}, {9,15}, {9,14}, 
            	{10,19}, {10,18}, {10,17}, {10,16}, {10,15}, {10,14}, {10,13},
            	{11,19}, {11,18}, {11,17}, {11,16}, {11,15}, {11,14}, {11,13}, {11,12},
            };

        for (int[] cell : cellsToReveal) {
            int row = cell[0];
            int col = cell[1];
            revealedCells[row][col] = true;
            boardButtons[row][col].setStyle("-fx-background-color: #3B2A1A; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-weight: bold; " +
                    "-fx-border-width: 0; " +
                    "-fx-background-radius: 0px; " +
                    "-fx-border-radius: 0;");
            boardButtons[row][col].setText("");
        }
    }

    // Counts the number of adjacent mines around the specified cell
    private int countAdjacentMines(int row, int col) {
        int mineCount = 0;
        for (int rOffset = -1; rOffset <= 1; rOffset++) {
            for (int cOffset = -1; cOffset <= 1; cOffset++) {
                if (isWithinBounds(row + rOffset, col + cOffset) && mineLocations[row + rOffset][col + cOffset]) {
                    mineCount++;
                }
            }
        }
        return mineCount;
    }

    // Reveals surrounding cells, showing adjacent mine counts or random emojis.
    private void revealSurroundingCells(int row, int col) {
        for (int rOffset = -1; rOffset <= 1; rOffset++) {
            for (int cOffset = -1; cOffset <= 1; cOffset++) {
                int newRow = row + rOffset;
                int newCol = col + cOffset;
                if (isWithinBounds(newRow, newCol) && !revealedCells[newRow][newCol]) {
                    revealedCells[newRow][newCol] = true;
                    int adjacentMines = countAdjacentMines(newRow, newCol);
                    if (adjacentMines > 0) {
                        boardButtons[newRow][newCol].setText(String.valueOf(adjacentMines));
                    } else {
                        String[] emojis = {"🐬", "🦑", "🦀", "🐟", "🐠", "🐙"};
                        Random random = new Random();
                        String randomEmoji = emojis[random.nextInt(emojis.length)];
                        boardButtons[newRow][newCol].setText(randomEmoji);
                        boardButtons[newRow][newCol].setStyle("-fx-background-color: blue;" +
                                "-fx-text-fill: white; -fx-font-weight: bold; -fx-border-color: white; -fx-border-width: 0.5; -fx-alignment: center; -fx-padding: 0;");
                        revealSurroundingCells(newRow, newCol);
                    }
                }
            }
        }
    }

    private boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }

    // Randomly places mines on the board, ensuring no adjacent mines and not on pre-revealed cells.
    private void scatterMines() {
        Random rand = new Random();
        int minesPlaced = 0;

        while (minesPlaced < NUM_MINES) {
            int row = rand.nextInt(ROWS);
            int col = rand.nextInt(COLS);

            if (!mineLocations[row][col] && !isCellPreRevealed(row, col)) {
                boolean canPlaceMine = true;

                for (int rOffset = -1; rOffset <= 1; rOffset++) {
                    for (int cOffset = -1; cOffset <= 1; cOffset++) {
                        int newRow = row + rOffset;
                        int newCol = col + cOffset;
                        if (isWithinBounds(newRow, newCol) && mineLocations[newRow][newCol]) {
                            canPlaceMine = false;
                            break;
                        }
                    }
                    if (!canPlaceMine) break;
                }

                if (canPlaceMine) {
                    mineLocations[row][col] = true;
                    minesPlaced++;
                }
            }
        }
    }

    // Checks if a cell is in the predefined list of cells to be revealed at the start.
    private boolean isCellPreRevealed(int row, int col) {
    	int[][] cellsToReveal = {
            	{0,0}, {0,1}, {0,2}, {0,3}, {0,4}, {0,5}, {0,6},
            	{1,0}, {1,1}, {1,2}, {1,3}, {1,4}, {1,5},
            	{2,0}, {2,1}, {2,2}, {2,3}, {2,4}, {2,5},
            	{3,0}, {3,1}, {3,2}, {3,3}, {3,4},
            	{4,0}, {4,1}, {4,2}, {4,3},
            	{5,0}, {5,1}, {5,2},
            	{6,0},
            	{7,0}, {7,1}, {7,2}, {7,3},
            	{8,0}, {8,1}, {8,2}, {8,3},
            	{9,0}, {9,1}, {9,2}, {9,3},
            	{10,0}, {10,1}, {10,2}, {10,3}, {10,4},
            	{11,0}, {11,1}, {11,2}, {11,3}, {11,4}, {11,5},
            	
            	        {0,19}, {0,18}, {0,17}, {0,16}, {0,15},
            	{1,19}, {1,18}, {1,17}, {1,16}, {1,15}, {1,14},
            	        {2,19}, {2,18}, {2,17}, {2,16}, {2,15},
            	                {3,18}, {3,17}, {3,16},
            	{4,18}, {4,17}, {4,16}, {4,15}, {4,14},
            	{5,18}, {5,17}, {5,16}, {5,15}, {5,14},
            	{6,18}, {6,17}, {6,16}, {6,15}, {6,14},
            	{7,17}, {7,16}, {7,15}, {7,14},
            	        {8,15},
            	{9,19}, {9,18}, {9,17}, {9,16}, {9,15}, {9,14}, 
            	{10,19}, {10,18}, {10,17}, {10,16}, {10,15}, {10,14}, {10,13},
            	{11,19}, {11,18}, {11,17}, {11,16}, {11,15}, {11,14}, {11,13}, {11,12},
            };

        for (int[] cell : cellsToReveal) {
            if (cell[0] == row && cell[1] == col) {
                return true;
            }
        }
        return false;
    }

    // Reveals all mines on the board and transitions to the game over screen after a pause.
    private void revealAllMines(final Stage primaryStage) {
        gameOverflag = true;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (mineLocations[row][col]) {
                    boardButtons[row][col].setText("🦠");
                    boardButtons[row][col].setStyle("-fx-background-color: linear-gradient(from 20% 20% to 100% 100%, black, green); -fx-text-fill: white; -fx-font-weight: bold; -fx-border-width: 0; -fx-alignment: center; -fx-padding: 0;");
                }
            }
        }

        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
			    primaryStage.close();
			    GameOver newWindow = new GameOver();
			    newWindow.display();
			}
		});
        pause.play();
    }

    // Checks if the game is won by verifying if all non-mine cells are revealed
    private void checkIfGameWon(final Stage primaryStage) {
        boolean hasWon = true;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (!mineLocations[row][col] && !revealedCells[row][col]) {
                    hasWon = false;
                    break;
                }
            }
        }

        if (hasWon) {
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(new EventHandler<ActionEvent>() {
    			public void handle(ActionEvent event) {
    			    primaryStage.close();
    			    LevelThree newWindow = new LevelThree();
    			    newWindow.display3();
    			}
    		});
            pause.play();
        }
    }
    
    public void menu(Stage primaryStage) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Confirmation");

        VBox popupLayout = new VBox(15);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.setPadding(new Insets(10));

        Label message = new Label("Are you sure you want to return to Menu?");
        message.setWrapText(true);
        message.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");

        Button confirmButton = new Button("Yes");
        confirmButton.setStyle(
            "-fx-font-size: 15px; " +
            "-fx-padding: 5px 20px; " +
            "-fx-background-radius: 50px; " +
            "-fx-border-radius: 100%;"
        );

        Button cancelButton = new Button("No");
        cancelButton.setStyle(
            "-fx-font-size: 15px; " +
            "-fx-padding: 5px 20px; " +
            "-fx-background-radius: 50px; " +
            "-fx-border-radius: 100%;"
        );

        confirmButton.setOnAction(ev -> {
            primaryStage.close();
            Menu newWindow = new Menu();
            newWindow.display();
            popup.close();
        });

        cancelButton.setOnAction(ev -> popup.close());
        
        HBox hbox = new HBox(5, confirmButton, cancelButton);
        hbox.setAlignment(Pos.CENTER);

        popupLayout.getChildren().addAll(message, hbox);

        Scene popupScene = new Scene(popupLayout, 320, 150);
        popup.setScene(popupScene);
        popup.showAndWait();
    }

    public void restart(Stage primaryStage) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Confirmation");

        VBox popupLayout = new VBox(15);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.setPadding(new Insets(10));

        Label message = new Label("Are you sure you want to restart the game?");
        message.setWrapText(true);
        message.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");

        Button confirmButton = new Button("Yes");
        confirmButton.setStyle(
            "-fx-font-size: 15px; " +
            "-fx-padding: 5px 20px; " +
            "-fx-background-radius: 50px; " +
            "-fx-border-radius: 100%;"
        );

        Button cancelButton = new Button("No");
        cancelButton.setStyle(
            "-fx-font-size: 15px; " +
            "-fx-padding: 5px 20px; " +
            "-fx-background-radius: 50px; " +
            "-fx-border-radius: 100%;"
        );

        confirmButton.setOnAction(ev -> {
            primaryStage.close();
            LevelOne newWindow = new LevelOne();
            newWindow.display1();
            popup.close();
        });

        cancelButton.setOnAction(ev -> popup.close());
        
        HBox hbox = new HBox(5, confirmButton, cancelButton);
        hbox.setAlignment(Pos.CENTER);

        popupLayout.getChildren().addAll(message, hbox);

        Scene popupScene = new Scene(popupLayout, 320, 150);
        popup.setScene(popupScene);
        popup.showAndWait();
    }
    
    public void display() {
        LevelOne newWindow = new LevelOne();
        newWindow.display1();
    }
}
