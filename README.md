# OceanRescue (Figma, JavaFX)

By leveraging the popularity and nostalgia behind Minesweeper, the game "OceanRescue" aims to raise awareness about the importance of protecting the ocean and its endangered species. Instead of having a regular square grid with cells and mines, why not have an irregular grid that will represent for example an ocean. You might often click on a cell and stumble upon a "mine", which is in this case toxic waste from factories dumped into the ocean. If a player stumbles upon toxic waste, all marine life in that area dies, and it's game over. This improved version of the Minesweeper consists of 3 levels: easy, medium, and hard. As you progress in the game, you will get bigger oceans (bigger grids), and more toxic waste to make the game harder.

## Demo

https://github.com/user-attachments/assets/b55165ad-018e-415a-9e5d-0163a0aff316

## User Interface Sketches
<img width="1000" alt="Screenshot 2024-09-23 at 12 05 48" src="https://github.com/user-attachments/assets/c0d2106a-f5de-45bf-a6e8-22e1cd737457">

## Storyboard

- Cell Reveal Interaction
<img width="1000" alt="Screenshot 2024-09-23 at 12 09 55" src="https://github.com/user-attachments/assets/18a63433-12d8-4faf-a8a8-963081d1736c">
- Revealing a cell containing toxic waste (mines)
<img width="1000" alt="Screenshot 2024-09-23 at 12 12 27" src="https://github.com/user-attachments/assets/259b622f-8e5d-4a1e-9e34-a723776aece7">
- Drag to select a 3x3 cell area to scan and reveal all hidden toxic waste
<img width="1000" alt="Screenshot 2024-09-23 at 12 10 56" src="https://github.com/user-attachments/assets/8bf69b3c-ce7d-4023-8443-60e894a52c68">
<img width="1000" alt="Screenshot 2024-09-23 at 12 11 17" src="https://github.com/user-attachments/assets/c27c4ba4-38d2-4d28-b52e-b3ec7a6e3190">
- Double click on a marine animal to reveal information about its endangered status
<img width="1000" alt="Screenshot 2024-09-23 at 12 11 45" src="https://github.com/user-attachments/assets/947c7790-5e94-486a-a0ed-fdcc2fd3a748">

## Instructions

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
  - Click Add External JARs and navigate to the lib folder of the extracted JavaFX SDK.
  - Select all the JAR files in the lib folder and click Open.
  - Click Apply and Close.
- Configure JavaFX in the Run Configuration
  - Go to Run > Run Configurations
  - Select the run configuration for your JavaFX project (Menu.java is the main class).
  - In the Arguments tab, in the VM arguments section, add the following:
    ```
    --module-path /path-to-javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
  - Apply the changes and close the configuration window.
