/**
 * Represents a route, defined as a distance between two points along a specific road.
 */
public class Route {
    private int distance;
    private  Road road;
    private String point1;
    private  String point2;
    private int priority;

    /**
     * Constructs a Route object with specified distance, road, and endpoints.
     *
     * @param distance The total distance of the route.
     * @param road The road object this route is associated with.
     * @param point1 The starting point of the route.
     * @param point2 The ending point of the route.
     */
    public Route(int distance, Road road, String point1, String point2, int priority) {
        this.distance = distance;
        this.road = road;
        this.point1 = point1;
        this.point2 = point2;
        this.priority = priority;
    }

    public int getDistance() {
        return distance;
    }

    public Road getRoad() {
        return road;
    }

    public String getPoint1() {
        return point1;
    }

    public String getPoint2() {
        return point2;
    }
    public int getPriority() {
        return priority;
    }
}
