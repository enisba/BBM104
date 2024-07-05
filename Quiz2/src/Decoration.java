abstract class Decoration implements DecorationCost {
    protected String name;
    protected double price;
    protected double area;

    public Decoration(String name, double price) {
        this.name = name;
        this.price = price;

    }

    public double getPrice() {
        return price;
    }

    public double getArea() {
        return area;
    }
}
