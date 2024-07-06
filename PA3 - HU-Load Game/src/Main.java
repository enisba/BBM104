import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The main class for the HU-Load game.
 */
public class Main extends Application {

    /**
     * The main method that launches the application.
     * @param args command line arguments (not used).
     */
    public static void main(String[] args) {
        launch(args);
    }

    private Game game;

    /**
     * this method initializes all necessary components for the game to run
     * this method including the main class because this is independent of the Game.java
     * Starts the JavaFX application, setting up the primary stage, scene, and the game loop.
     * This method initializes all necessary components for the game to run.
     * @param primaryStage the primary stage for this application, onto which the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("HU-Load Game");

        Canvas canvas = new Canvas(800, 750);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);

        game = new Game(gc);
        game.initialize();

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    game.getDrill().moveUp();
                    break;
                case DOWN:
                    game.getDrill().moveDown();
                    break;
                case LEFT:
                    game.getDrill().moveLeft();
                    break;
                case RIGHT:
                    game.getDrill().moveRight();
                    break;
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
        game.start();
    }
}
