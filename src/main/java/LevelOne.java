import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Random;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

public class LevelOne {

    private static final int ROWS = 12;
    private static final int COLS = 20;
    private static final int NUM_MINES = 2;
    private Button[][] boardButtons = new Button[ROWS][COLS];
    private boolean[][] mineLocations = new boolean[ROWS][COLS];
    private boolean[][] revealedCells = new boolean[ROWS][COLS];
    private boolean gameOverflag = false;
    private int scoreValue = 0;
    
    public void Display() {
    	
    	Stage mainWindow = new Stage();
    	
    	// First, we will show the player's score and current level
    	Label score = new Label("Score : 0");
    	Label level = new Label("Level : 1");
    	VBox vbox1 = new VBox(5, score, level);

    	// Now, let's display info about mines and cells
    	Label mines = new Label("🦠 x " + NUM_MINES);
    	Label square = new Label("◼︎︎");
    	square.setStyle("-fx-text-fill: navy; -fx-font-size: 26px; -fx-padding: -11 -1 0 0;");
    	Label cells = new Label("x 125");
    	HBox hboxcell = new HBox(5, square, cells);
    	VBox vbox2 = new VBox(5, mines, hboxcell);

    	// Next, let's add a timer to show how long the player has been playing
    	Label time = new Label("Time Spent");
    	Label spent = new Label("0 sec");
    	VBox vbox3 = new VBox(5, time, spent);
    	
        // This is the timer logic
        long startTime = System.currentTimeMillis();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            long elapsedMillis = System.currentTimeMillis() - startTime;
            long elapsedSeconds = elapsedMillis / 1000;
            spent.setText(elapsedSeconds + " sec");
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    	// Now, let's show which sea animals the player is trying to save
    	Label animals = new Label("Animals To Save");
    	Label saved = new Label("🐬🦑🦀🐟🐠🐙");
    	VBox vbox4 = new VBox(5, animals, saved);

    	// Here we are adding some control buttons
    	Button restart = new Button("↻");
    	restart.setOnAction(e -> restart(mainWindow));
    	Button menu = new Button("☰");
    	menu.setOnAction(e -> menu(mainWindow));
    	HBox hbox1 = new HBox(5, restart, menu);

    	HBox hbox = new HBox(55, vbox1, vbox2, vbox3, vbox4, hbox1);
    	hbox.setAlignment(Pos.CENTER);
        
    	// Set up the main game layout
    	BorderPane layout = new BorderPane();
    	GridPane gameBoard = new GridPane();
        
        // Setup game UI
        layout.setTop(hbox);
        BorderPane.setMargin(hbox, new Insets(0, 0, 10, 0));
        layout.setCenter(gameBoard);
        layout.setPadding(new Insets(16));

        Scene gameScene = new Scene(layout, COLS * 31.5, ROWS * 37);
        mainWindow.setTitle("Ocean Rescue");
        mainWindow.setScene(gameScene);
        mainWindow.centerOnScreen();
        mainWindow.show();

        // Initialize the board with buttons
        setupGameBoard(gameBoard, mainWindow, score);

        // Place mines randomly on the grid
        scatterMines();

        // Reveal specified cells at the start
        preRevealCells();
    }

    private void setupGameBoard(GridPane gameBoard, final Stage mainWindow, Label score) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Button cellButton = new Button();
                cellButton.setMinSize(30, 30);
                final int currentRow = row;
                final int currentCol = col;

                // The cells of the grid that represent the ocean are colored navy
                cellButton.setStyle("-fx-background-color: navy;" +
                                     "-fx-text-fill: white; -fx-font-weight: bold; -fx-border-color: white; -fx-border-width: 0.5;");

                cellButton.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        handleCellClick(currentRow, currentCol, mainWindow, score);
                    }
                });

                boardButtons[row][col] = cellButton;
                gameBoard.add(cellButton, col, row);
            }
        }
    }

    private void handleCellClick(int row, int col, Stage mainWindow, Label score) {
        // The player cannot click on the cells representing land, and if they lose the game, 
    	// they will no longer be able to interact with any cells. 
    	if (gameOverflag || revealedCells[row][col]) {
            return; 
        }

        revealedCells[row][col] = true;

        if (mineLocations[row][col]) {
            boardButtons[row][col].setText("️️️🦠︎️");
            boardButtons[row][col].setStyle("-fx-background-color: linear-gradient(from 20% 20% to 100% 100%, black, green); -fx-text-fill: white; -fx-font-weight: bold; -fx-border-width: 0; -fx-alignment: center; -fx-padding: 0;");
            revealAllMines(mainWindow);
        } else {
        	if (!mineLocations[row][col]) {
        	    // Increment score by 100 for successful moves
        	    scoreValue += 100;
        	    score.setText("Score: " + scoreValue);
        	}
            int nearbyMines = countAdjacentMines(row, col);
            if (nearbyMines > 0) {
                boardButtons[row][col].setText(String.valueOf(nearbyMines));
            } else {
                // Always show an emoji when there's no mine
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

    // Pre-reveal specified cells (the pre-revealed cells represent land.)
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
            boardButtons[row][col].setStyle("-fx-background-color: linear-gradient(from 20% 20% to 100% 100%, beige, darkgreen); -fx-text-fill: white; -fx-font-weight: bold; -fx-border-width: 0;");
            boardButtons[row][col].setText("");
        }
    }

    // Count how many mines are around a given cell
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

    // Recursively reveal empty adjacent cells
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
                        // Always show an emoji when revealing surrounding cells
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

    // Check if row and column are within the board limits
    private boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }

 // Scatter mines randomly across the grid, avoiding pre-revealed cells
    private void scatterMines() {
        Random rand = new Random();
        int minesPlaced = 0;

        while (minesPlaced < NUM_MINES) {
            int row = rand.nextInt(ROWS);
            int col = rand.nextInt(COLS);

            if (!mineLocations[row][col] && !isCellPreRevealed(row, col)) {
                boolean canPlaceMine = true;

                // Check neighboring cells to ensure no adjacent mines
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

    
 // Check if a cell is in the pre-reveal list
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


 // Reveal all mines if the game is over
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

        // Delay for 1.5 second before redirecting to the "Game Over" window
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

    // Check if all non-mine cells are revealed
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
            // Delay for 1.5 second before redirecting to the "You Win" window
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(new EventHandler<ActionEvent>() {
    			public void handle(ActionEvent event) {
    			    primaryStage.close();
    			    GameWon newWindow = new GameWon();
    			    newWindow.display();
    			}
    		});
            pause.play();
        }
    }
    
    // Switches from the current game window to the main menu
    public void menu(Stage primaryStage) {
	    primaryStage.close();
	    Menu newWindow = new Menu();
	    newWindow.display();
    }
    
    // Restarts the game by closing the current window and re-launching it again
    public void restart(Stage primaryStage) {
	    primaryStage.close();
	    LevelOne newWindow = new LevelOne();
	    newWindow.Display();
    }
    
    public void display() {
        Stage newStage = new Stage();
        restart(newStage);
    }
}
