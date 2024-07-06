class Tile extends Decoration{
    public Tile(String name, double price, double area) {
        super(name, price);
        this.area = area;
    }

    @Override
    public double calculateCost(double area) {
        double numberOfTiles = Math.ceil(area / getArea());
        return numberOfTiles * getPrice();
    }
}
