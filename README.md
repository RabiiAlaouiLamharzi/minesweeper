# Minesweeper (Java FX)

### Introduction

By leveraging the popularity and nostalgia behind Minesweeper, the game "OceanRescue" aims to raise awareness about the importance of protecting the ocean and its endangered species. Instead of having a regular square grid with cells and mines, why not have an irregular grid that will represent for example an ocean. You might often click on a cell and stumble upon a "mine", which is in this case toxic waste from factories dumped into the ocean. If a player stumbles upon toxic waste, all marine life in that area dies, and it's game over. This improved version of the Minesweeper consists of 3 levels: easy, medium, and hard. As you progress in the game, you will get bigger oceans (bigger grids), and more toxic waste to make the game harder.

### Demo Video

(Watch the demo video with sound on)

https://github.com/user-attachments/assets/3d9167e3-8b4d-49ba-85e2-3ee4a9f3c858

### What was Implemented in V1

- Main Menu with basic styling (with a button to start playing level one)
- “Game Over” Screen with basic styling (and two working buttons to restart level or go to menu)
- “You Win” Screen with basic styling (and two working buttons to restart the level or go to the menu plus a donate link if you want to help save the ocean)
- The layout of the minesweeper grid with basic styling (textures and advanced styles to be added in V2)
- Implemented a board to record scores, time spent in the game, etc (to be improved with a better algorithm to record the score and animals saved)
- Implemented the two basic functionalities I mentioned in HWK 1
  - Implemented the cell reveal interaction which uncovers safe areas of the grid (saving marine life) and displays numbers that indicate adjacent mines.
  - Implemented the case of revealing a mine (toxic waste) which triggers the “Game Over” screen by uncovering all the mines at once.

### What was Implemented in V2

- Added two more levels to the game with varying difficulty (new grids, more mines, etc.).
- Introduced custom fonts and beautiful GIFs for enhanced aesthetics.
- Implemented custom popups to improve user experience.
- Added advanced functionalities for a more engaging gameplay experience.
  - Implemented drag-and-drop functionality to place a flag in the desired cell. Players can place only 2 flags per level; attempting to place more will trigger a popup informing them of the limit.
  - Enabled right-clicking on an animal that the player has just rescued to provide more information about its endangered status.
  - Integrated a basic chatbot to assist users in understanding the game better if they encounter any problems or issues.
  - Developed a hint system where users can press the space bar to reveal one mine, with the restriction of having only 1 hint per level.
 
### Instructions to Set up the Project

- Install Eclipse IDE
- Clone the Repository from GitHub
  - Go to Eclipse IDE
  - File > Import > Git > Projects from Git.
  - Choose Clone URI and click Next.
  - Paste the URL of the GitHub repository into the URI field and press Next.
  - Select a local directory to clone the project to, and click Next.
  - Choose Import as Maven Project then click Finish.
- Download Java (I use Java 22.0.2)
- Download the JavaFX SDK (I use javafx-sdk-21.0.4)
- Add JavaFX Libraries to the Project in Eclipse
  - Right-click the imported project in Eclipse
  - Choose Properties.
  - Java Build Path > Libraries tab.
  - Click Add External JARs and navigate to the lib folder of the extracted
  - JavaFX SDK.
  - Select all the JAR files in the lib folder and click Open.
  - Click Apply and Close.
- Configure JavaFX in the Run Configuration
  - Go to Run > Run Configurations
  - Select the run configuration for your JavaFX project (Menu.java is the main class).
- In the Arguments tab, in the VM arguments section, add the following:
  ```
  --module-path /path-to-javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
- Apply the changes and close the configuration window.

