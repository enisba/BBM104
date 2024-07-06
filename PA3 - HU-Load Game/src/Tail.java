import javafx.scene.image.Image;

/**
 * Represents a single Tail in the game map with a specific type, position, and optionally an image.
 * Tails can be of various types, including different types of gems, obstacles like boulders and lava, or simple soil.
 */
public class Tail {
    private Type type;
    private int x, y;
    private Image image;

    /**
     * Enumerates the different possible types of Tails in the game.
     * Each type represents a different element that can appear in the game map.
     */
    public enum Type {
        SOIL, BOULDER, LAVA, GEM_AMAZONITE, GEM_EMERALD, GEM_RUBY, GEM_PLATINUM, EMPTY
    }

    /**
     * Constructs a new Tail with a specified type, position, and image.
     * @param type  The type of the Tail.
     * @param x     The x-coordinate of the Tail in the game map.
     * @param y     The y-coordinate of the Tail in the game map.
     * @param image The image used to visually represent the Tail, can be null for types like EMPTY.
     */
    public Tail(Type type, int x, int y, Image image) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.image = image;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Image getImage() {
        return image;
    }

    public Type getType() {
        return type;
    }

    }
