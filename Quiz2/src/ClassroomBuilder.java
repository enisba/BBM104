public interface ClassroomBuilder {
    ClassroomBuilder setId(String id);
    ClassroomBuilder setWallDecoration(Decoration wallDecoration);
    void setFloorDecoration(Decoration floorDecoration);
    ClassroomBuilder setWidth(double width);
    ClassroomBuilder setLength(double length);
    ClassroomBuilder setHeight(double height);
    ClassroomBuilder setRadius(double radius);
    Classroom buildRectangle();
    Classroom buildCircular();
}
