public class Stationery extends Item {
    private String kind;

    /**
     * Constructs a new Stationery.
     *
     * @param name the name of the stationery
     * @param kind the kind of the stationery
     * @param barcode the barcode of the stationery
     * @param price the price of the stationery
     */
    public Stationery(String name, String kind, int barcode, double price) {
        super(name, barcode, price);
        this.kind = kind;
    }

    public String getKind() {
        return kind;
    }

    @Override
    public String toString() {
        return String.format("Kind of the %s is %s. Its barcode is %d and its price is %s", getName(), kind, getBarcode(), getPrice());
    }
}
