/**
 * Represents a road connecting two points. This class encapsulates the properties of a road
 * including its start and end points, length, and id.
 */
public class Road {
    private final String point1;
    private final String point2;
    private final int length;
    private final int id;

    /**
     * Constructs a Road object with specified endpoints, length, and identifier.
     *
     * @param point1 The starting point of the road.
     * @param point2 The ending point of the road.
     * @param length The length of the road in kilometers.
     * @param id The unique identifier.
     */
    public Road(String point1, String point2, int length, int id) {
        this.point1 = point1;
        this.point2 = point2;
        this.length = length;
        this.id = id;
    }

    public String getPoint1() {
        return point1;
    }

    public String getPoint2() {
        return point2;
    }

    public int getLength() {
        return length;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return point1 + "\t" + point2 + "\t" + length + "\t" + id;
    }
}
