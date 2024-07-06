import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Random;

/**
 * The Game class manages the game environment including the drill, map Tails,
 * images, and game mechanics such as camera movement and gameplay updates.
 */
public class Game {
    private Drill drill;
    private Tail[][] map;
    private GraphicsContext gc;
    private HashMap<String, Image> images;
    private static final int Tail_SIZE = 50;
    private Random rand = new Random();
    private HashMap<Tail.Type, Integer> gemValues;
    private HashMap<Tail.Type, Double> gemWeights;
    private long lastTime = 0;
    private int cameraY = 0;


    public Tail[][] getMap() {
        return map;
    }
    public Drill getDrill() {
        return drill;
    }

    public Image getImage(String key) {
        return images.get(key);
    }

    public Tail getTailAt(int x, int y) {
        return map[x][y];
    }
    public HashMap<Tail.Type, Integer> getGemValues() {
        return gemValues;
    }

    public HashMap<Tail.Type, Double> getGemWeights() {
        return gemWeights;
    }

    public void removeTailAt(int x, int y) {
        map[x][y] = new Tail(Tail.Type.EMPTY, x, y, null);
    }

    /**
     * Constructor for Game initializes graphics, loads images, and prepares game values.
     * @param gc The GraphicsContext from the canvas.
     */
    public Game(GraphicsContext gc) {
        this.gc = gc;
        images = new HashMap<>();
        loadImages();
        initializeGemValues();
        initializeGemWeights();
        initialize();
    }

    /**
     * Loads images for different Tails and the drill.
     */
    private void loadImages() {
        // Drill
        images.put("drill_right", new Image("file:src/assets/drill/drill_55.png"));
        images.put("drill_left", new Image("file:src/assets/drill/drill_53.png"));
        images.put("drill_up", new Image("file:src/assets/drill/drill_27.png"));
        images.put("drill_down", new Image("file:src/assets/drill/drill_43.png"));
        // Tail
        images.put("soıl", new Image("file:src/assets/underground/soil_01.png"));
        images.put("grass_soil", new Image("file:src/assets/underground/top_01.png")); // Çimli toprak görseli
        images.put("gem_amazonıte", new Image("file:src/assets/underground/valuable_amazonite.png"));
        images.put("gem_emerald", new Image("file:src/assets/underground/valuable_emerald.png"));
        images.put("gem_ruby", new Image("file:src/assets/underground/valuable_ruby.png"));
        images.put("gem_platınum", new Image("file:src/assets/underground/valuable_platinum.png"));
        images.put("boulder", new Image("file:src/assets/underground/obstacle_02.png"));
        images.put("lava", new Image("file:src/assets/underground/lava_02.png"));

    }

    /**
     * Initializes the game map with soil, boulders, and empty spaces.
     */
    public void initialize() {
        drill = new Drill(this, 7, 0);
        map = new Tail[16][15];
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                Image image;
                Tail.Type type;
                if (y < 3) {
                    if (y == 2) {
                        type = Tail.Type.SOIL;
                        image = images.get("grass_soil");
                    } else {
                        type = Tail.Type.EMPTY;
                        image = null;
                    }
                } else if ((x == 0 || x == map.length - 1)) {
                    type = Tail.Type.BOULDER;
                    image = images.get("boulder");
                } else {
                    type = generateRandomTailType(rand);
                    image = images.get(type.toString().toLowerCase());
                }
                map[x][y] = new Tail(type, x, y, image);
            }
        }
    }

    /**
     * Expands the map downwards by adding a new row of Tails at the bottom.
     */
    public void expandMapDown() {
        int currentHeight = map[0].length;
        Tail[][] newMap = new Tail[16][currentHeight + 1];

        for (int x = 0; x < 16; x++) {
            System.arraycopy(map[x], 0, newMap[x], 0, currentHeight);
        }

        for (int x = 0; x < 16; x++) {
            Tail.Type type;
            Image image;
            if (x == 0 || x == 15) {
                type = Tail.Type.BOULDER;
                image = images.get("boulder");
            } else {
                type = generateRandomTailType(rand);
                image = images.get(type.toString().toLowerCase());
            }
            newMap[x][currentHeight] = new Tail(type, x, currentHeight, image);
        }

        map = newMap;
    }

    /**
     * Generates a random Tail type based on predefined probabilities.
     * @param rand The Random instance used for generating random numbers.
     * @return The type of the Tail generated.
     */
    private Tail.Type generateRandomTailType(Random rand) {
        double chance = rand.nextDouble();
        if (chance < 0.82) {
            return Tail.Type.SOIL;
        } else if (chance < 0.86) {
            return Tail.Type.GEM_PLATINUM;
        } else if (chance < 0.89) {
            return Tail.Type.GEM_EMERALD;
        } else if (chance < 0.91) {
            return Tail.Type.GEM_RUBY;
        } else if (chance < 0.93) {
            return Tail.Type.GEM_AMAZONITE;
        } else if (chance < 0.96) {
            return Tail.Type.BOULDER;
        } else {
            return Tail.Type.LAVA;
        }
    }

    /**
     * Initializes the gem values with their corresponding monetary value.
     */
    private void initializeGemValues() {
        gemValues = new HashMap<>();
        gemValues.put(Tail.Type.GEM_AMAZONITE, 500000);  // Amazonite için değer
        gemValues.put(Tail.Type.GEM_RUBY, 20000);       // Ruby için değer
        gemValues.put(Tail.Type.GEM_EMERALD, 5000);    // Emerald için değer
        gemValues.put(Tail.Type.GEM_PLATINUM, 750);   // Platinum için değer
    }

    /**
     * Initializes the weights for different types of gems.
     */
    private void initializeGemWeights() {
        gemWeights = new HashMap<>();
        gemWeights.put(Tail.Type.GEM_PLATINUM, 30.0);
        gemWeights.put(Tail.Type.GEM_EMERALD, 60.0);
        gemWeights.put(Tail.Type.GEM_RUBY, 80.0);
        gemWeights.put(Tail.Type.GEM_AMAZONITE, 120.0);
    }

    /**
     * Updates the camera's vertical position based on the drill's position.
     */
    private void updateCamera() {
        int threshold = 600;
        if (drill.getY() * Tail_SIZE > cameraY + threshold || cameraY > 0) {
            cameraY = drill.getY() * Tail_SIZE - threshold;
        }
    }

    /**
     * Draws all game elements to the canvas.
     * @param gc The GraphicsContext used to draw the game elements.
     */
    private void draw(GraphicsContext gc) {
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.clearRect(0, 0, canvasWidth, canvasHeight);

        gc.setFill(Color.DARKBLUE);
        gc.fillRect(0, 0 - cameraY, canvasWidth, Tail_SIZE * 3);
        gc.setFill(Color.SADDLEBROWN);
        gc.fillRect(0, 105 - cameraY, canvasWidth, Tail_SIZE * 320);

        for (Tail[] Tails : map) {
            for (Tail Tail : Tails) {
                if (Tail != null) {
                    gc.drawImage(Tail.getImage(), Tail.getX() * Tail_SIZE, Tail.getY() * Tail_SIZE - cameraY);
                }
            }
        }

        Image drillImage = drill.getCurrentImage();
        double imageWidth = drillImage.getWidth();
        double imageHeight = drillImage.getHeight();

        double drawX = drill.getX() * Tail_SIZE + (Tail_SIZE - imageWidth) / 2 - 10;
        double drawY = drill.getY() * Tail_SIZE + (Tail_SIZE - imageHeight) / 2 + 10;

        if (drill.getCurrentImage() == images.get("drill_right")) {
            drawX += 15;
            drawY -= 5;
        }
        gc.drawImage(drillImage, drawX, drawY - cameraY);

        gc.setFill(Color.rgb(100, 100, 100, 0.5));
        gc.fillRect(2, 2, 200, 88);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font(18));
        gc.fillText("MONEY: " + drill.getCollectedGems() + " $", 10, 20);  // Puan bilgisini sol üst köşeye yaz
        gc.fillText("FUEL: " + String.format("%.3f", drill.getFuel()) + " L", 10, 50);  // Yakıt bilgisini altına yaz
        gc.fillText("HAUL: " + String.format("%.2f", drill.getCurrentLoad()) + " kg", 10, 80);

        if (drill.getFuel() < 1000) {
            gc.setFill(Color.WHITESMOKE);
            gc.fillText(" ATTENTION: FUEL LOW !!! ", 203, 50);  // Yakıt bilgisini altına yaz
        }

        if (!drill.getAlive()) {
            if (drill.getHitLava()) {
                gc.setFill(Color.DARKRED);
                gc.fillRect(0, 0, canvasWidth, canvasHeight);
                gc.setFill(Color.WHITE);
                gc.setFont(new Font(48));
                gc.fillText("GAME OVER", canvasWidth / 2 - 150, canvasHeight / 2);
                gc.setFont(new Font(16));
                gc.fillText("(YOU TOUCHED THE LAVA)", canvasWidth / 2 - 115, canvasHeight / 2 + 30);
            } else if (drill.getFuel() == 0) {
                gc.setFill(Color.GREEN);
                gc.fillRect(0, 0, canvasWidth, canvasHeight);
                gc.setFill(Color.WHITE);
                gc.setFont(new Font(32));
                String gameOverMsg = "GAME OVER";
                String gemsCollected = "Total Money Collected: " + drill.getCollectedGems();
                gc.fillText(gameOverMsg, canvasWidth / 2 - gameOverMsg.length() * 10, canvasHeight / 2 - 20);
                gc.fillText(gemsCollected, canvasWidth / 2 - gemsCollected.length() * 8, canvasHeight / 2 + 20);
            }
        }
    }

    /**
     * Updates the game state including drill operations and Tail interactions.
     * @param counterTime The time difference from the last update.
     */
    private void update(double counterTime) {
        drill.gravityRules(counterTime);
        drill.consumeFuel(false);
        if (!drill.getAlive()) {
            return;
        }

        if (drill.getFuel() <= 0) {
            drill.setAlive(false);
            return;
        }

        Tail currentTail = map[drill.getX()][drill.getY()];
        switch (currentTail.getType()) {
            case LAVA:
                drill.setAlive(false);
                break;
            case BOULDER:
                break;
            default:
                if (currentTail.getType() != Tail.Type.SOIL && currentTail.getType() != Tail.Type.EMPTY) {
                    drill.collectGem(currentTail.getType());
                }
                break;
        }
    }

    /**
     * Starts the game loop using an AnimationTimer to handle game updates and rendering.
     */
    public void start() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                double counterTime = (now - lastTime) / 900_000_000.0;
                lastTime = now;
                update(counterTime);
                updateCamera();
                draw(gc);
            }
        }.start();
    }
}