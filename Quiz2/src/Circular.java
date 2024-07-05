public class Circular extends Classroom {
    private final double radius;
    private final double height;

    public Circular(String id, Decoration wallDecoration, Decoration floorDecoration, double radius, double height) {
        super(id, wallDecoration, floorDecoration);
        this.radius = radius;
        this.height = height;
    }

    public double getRadius() {
        return radius;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public double calculateWallArea() {
        return  Math.PI * getRadius() * getHeight();
    }

    @Override
    public double calculateFloorArea() {
        return Math.PI * Math.pow(getRadius() / 2, 2);
    }
}
