class Rectangle extends Classroom {
    private final double width;
    private final double length;
    private final double height;

    public Rectangle(String id, Decoration wallDecoration, Decoration floorDecoration, double width, double length, double height) {
        super(id, wallDecoration, floorDecoration);
        this.width = width;
        this.length = length;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getLength() {
        return length;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public double calculateWallArea() {
        return 2 * (getWidth() + getLength()) * getHeight();
    }

    @Override
    public double calculateFloorArea() {
        return getWidth() * getLength();
    }
}