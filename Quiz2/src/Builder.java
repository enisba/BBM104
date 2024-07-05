public class Builder implements ClassroomBuilder {
    private String id;
    private Decoration wallDecoration;
    private Decoration floorDecoration;
    private double width;
    private double length;
    private double height;
    private double radius;

    @Override
    public ClassroomBuilder setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public ClassroomBuilder setWallDecoration(Decoration wallDecoration) {
        this.wallDecoration = wallDecoration;
        return this;
    }

    @Override
    public void setFloorDecoration(Decoration floorDecoration) {
        this.floorDecoration = floorDecoration;
    }

    @Override
    public ClassroomBuilder setWidth(double width) {
        this.width = width;
        return this;
    }

    @Override
    public ClassroomBuilder setLength(double length) {
        this.length = length;
        return this;
    }

    @Override
    public ClassroomBuilder setHeight(double height) {
        this.height = height;
        return this;
    }

    @Override
    public ClassroomBuilder setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    @Override
    public Classroom buildRectangle() {
        return new Rectangle(id, wallDecoration, floorDecoration, width, length, height);
    }

    @Override
    public Classroom buildCircular() {
        return new Circular(id, wallDecoration, floorDecoration, radius, height);
    }
}
