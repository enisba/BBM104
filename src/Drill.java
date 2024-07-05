import javafx.scene.image.Image;

/**
 * Represents the drilling machine in the game. Manages its movement, fuel consumption, and gem collection.
 */
public class Drill {
    private int x, y; // Current position of the drill
    private double fuel; // Current level of fuel
    private int collectedGems;
    private boolean isAlive; // Indicates whether the drill is still operational
    private boolean hitLava = false; // Indicates if the drill has hit lava
    private Image currentImage;
    private Game game;
    private boolean isMovingUp = false; // Indicates if the drill is moving up
    private double gravityCounter = 0; // Counter for applying gravity
    private final double gravityInterval = 0.4; // Time interval for gravity effects
    private final double gravityDelay = 0.2; // Delay before gravity starts affecting the drill
    private double gravityDelayCounter = 0; // Counter for the delay before gravity effects
    private double currentLoad = 0;   // Current load of minerals being carried
    
    public boolean getHitLava() {
        return hitLava;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public double getFuel() {
        return fuel;
    }
    public int getCollectedGems() {
        return collectedGems;
    }
    public double getCurrentLoad() {
        return currentLoad;
    }
    public void setAlive(boolean alive) {
        isAlive = alive;
    }
    public boolean getAlive() {
        return isAlive;
    }
    public Image getCurrentImage() {
        return currentImage;
    }

    /**
     * Constructor for Drill.
     * Initializes the drill's position, fuel, and sets the initial image.
     * @param game The game object that this drill belongs to.
     * @param startX The initial x-coordinate of the drill.
     * @param startY The initial y-coordinate of the drill.
     */
    public Drill(Game game, int startX, int startY) {
        this.game = game;
        this.fuel = 10000;
        this.x = startX;
        this.y = startY;
        this.collectedGems = 0;
        this.isAlive = true;
        this.currentImage = game.getImage("drill_down");

    }

    /**
     * Consumes fuel. Amount of fuel consumed depends on whether the drill is moving.
     * @param isMoving True if the drill is moving, otherwise false.
     */
    public void consumeFuel(boolean isMoving) {
        if (isMoving) {
            fuel = fuel - 100;
        } else {
            fuel -= 0.007;
        }

        if (fuel <= 0) {
            fuel = 0;
            isAlive = false;
        }
    }

    /**
     * Collects gems from the current Tail.
     * Adds the gem's value to the total and increases the load of the drill.
     * @param gemType The type of gem to collect.
     */
    public void collectGem(Tail.Type gemType) {
        double weight = game.getGemWeights().getOrDefault(gemType, 0.0);
        int value = game.getGemValues().getOrDefault(gemType, 0);
        currentLoad += weight;
        collectedGems += value;
    }

    /**
     * Applies gravity to the drill, moving it downwards if the space below is empty.
     * @param counterTime The time elapsed since the last update.
     */
    public void gravityRules(double counterTime) {
        if (!isMovingUp) {
            gravityDelayCounter += counterTime;
            if (gravityDelayCounter >= gravityDelay) {
                gravityCounter += counterTime;
                if (gravityCounter >= gravityInterval) {
                    Tail tailBelow = game.getTailAt(x, y + 1);
                    if (tailBelow != null && tailBelow.getType() == Tail.Type.EMPTY) {
                        y++;
                        gravityCounter = 0;
                    }
                }
            }
        } else {
            gravityDelayCounter = 0;
        }
        isMovingUp = false;
    }

    /**
     * Handles the interaction of the drill with the current Tail, such as collecting gems or hitting obstacles.
     */
    public void mine() {
        Tail currentTail = game.getTailAt(x, y);
        if (currentTail.getType() == Tail.Type.BOULDER || currentTail.getType() == Tail.Type.LAVA) {
            if (currentTail.getType() == Tail.Type.LAVA) {
                setAlive(false);
                hitLava = true;
            }
            return;
        }
        game.removeTailAt(x, y);
        if (currentTail.getType() != Tail.Type.SOIL && currentTail.getType() != Tail.Type.EMPTY) {
            collectGem(currentTail.getType());
        }
    }

    /**
     * Determines if the Tail type can be passed through by the drill.
     * @param type The type of Tail to check.
     * @return True if the Tail is passable, otherwise false.
     */
    private boolean isPassable(Tail.Type type) {
        return type != Tail.Type.BOULDER && type != Tail.Type.LAVA && type != Tail.Type.SOIL && type != Tail.Type.GEM_AMAZONITE &&
                type != Tail.Type.GEM_EMERALD && type != Tail.Type.GEM_PLATINUM && type != Tail.Type.GEM_RUBY;
    }

    /**
     * Checks if the drill is standing on solid ground.
     * @return True if there is a non-empty Tail below the drill, otherwise false.
     */
    private boolean isOnGround() {
        Tail TailBelow = game.getTailAt(x, y + 1);
        return TailBelow != null && TailBelow.getType() != Tail.Type.EMPTY;
    }

    /**
     * Checks if the Tail to the right of the drill is empty.
     * @return True if the right Tail is empty and within game boundaries, otherwise false.
     */
    private boolean isEmptyOnRight() {
        if (x < game.getMap()[0].length - 1) {
            Tail TailRight = game.getTailAt(x + 1, y);
            return TailRight != null && TailRight.getType() == Tail.Type.EMPTY;
        }
        return false;
    }

    /**
     * Checks if the Tail to the left of the drill is empty.
     * @return True if the left Tail is empty and within game boundaries, otherwise false.
     */
    private boolean isEmptyOnLeft() {
        if (x > 0) {
            Tail TailLeft = game.getTailAt(x - 1, y);
            return TailLeft != null && TailLeft.getType() == Tail.Type.EMPTY;
        }
        return false;
    }

    /**
     * Moves the drill left if the movement is possible.
     * Checks for ground presence and obstacles before moving.
     */
    public void moveLeft() {
        if ((isOnGround() || isEmptyOnLeft()) && x > 0) {
            Tail nextTail = game.getTailAt(x - 1, y);
            if (nextTail.getType() != Tail.Type.BOULDER) {
                x--;
                currentImage = game.getImage("drill_left");
                mine();
                consumeFuel(true);
            }
        }
    }

    /**
     * Moves the drill right if the movement is possible.
     * Checks for ground presence and obstacles before moving.
     */
    public void moveRight() {
        if ((isOnGround() || isEmptyOnRight()) && x < 15) {
            Tail nextTail = game.getTailAt(x + 1, y);
            if (nextTail.getType() != Tail.Type.BOULDER) {
                x++;
                currentImage = game.getImage("drill_right");
                mine();
                consumeFuel(true);
            }
        }
    }

    /**
     * Moves the drill upwards if the movement is possible.
     * The drill can move up if the above Tail is empty or passable.
     */
    public void moveUp() {
        isMovingUp = true;
        Tail aboveTail = game.getTailAt(x, y - 1);
        if (aboveTail != null && (aboveTail.getType() == Tail.Type.EMPTY || isPassable(aboveTail.getType())) && y > 0) {
            y--;
            currentImage = game.getImage("drill_up");
            consumeFuel(true);
        }
    }

    /**
     * Moves the drill downwards if the movement is possible.
     * Automatically expands the map if necessary.
     */
    public void moveDown() {
        if (y >= game.getMap()[0].length - 3) {
            game.expandMapDown();
        }
        if (game.getTailAt(x, y + 1).getType() != Tail.Type.BOULDER) {
            y++;
            currentImage = game.getImage("drill_down");
            mine();
            consumeFuel(true);
        }
    }


}
