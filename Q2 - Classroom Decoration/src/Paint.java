class Paint extends Decoration {
    public Paint(String name, double price) {
        super(name, price);
    }

    @Override
    public String toString() {
        return "Paint";
    }

    @Override
    public double calculateCost(double area) {
        return area * getPrice();
    }
}
