class Wallpaper extends Decoration {
    public Wallpaper(String name, double price) {
        super(name, price);
    }

    @Override
    public String toString() {
        return "Wallpaper" ;
    }

    @Override
    public double calculateCost(double area) {
        return area * getPrice();
    }
}
