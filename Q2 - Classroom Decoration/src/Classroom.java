abstract class Classroom  {
    private final String id;
    private final Decoration wallDecoration;
    private final Decoration floorDecoration;

    public Classroom(String id, Decoration wallDecoration, Decoration floorDecoration) {
        this.id = id;
        this.wallDecoration = wallDecoration;
        this.floorDecoration = floorDecoration;
    }

    public String getId() {
        return id;
    }

    public Decoration getWallDecoration() {
        return wallDecoration;
    }

    public Decoration getFloorDecoration() {
        return floorDecoration;
    }

    public abstract double calculateWallArea();
    public abstract double calculateFloorArea();

    public double calculateTotalCost() {
        double wallCost = getWallDecoration().calculateCost(calculateWallArea());
        double floorCost = getFloorDecoration().calculateCost(calculateFloorArea());
        return wallCost + floorCost;
    }
}
