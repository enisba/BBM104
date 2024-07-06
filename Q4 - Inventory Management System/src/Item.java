/**
 * Represents an item with a name, barcode, and price.
 */
public abstract class Item {
    private String name;
    private int barcode;
    private double price;

    /**
     * Constructs a new Item.
     *
     * @param name the name of the item
     * @param barcode the barcode of the item
     * @param price the price of the item
     */
    public Item(String name, int barcode, double price) {
        this.name = name;
        this.barcode = barcode;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getBarcode() {
        return barcode;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s - Barcode: %d, Price: %s", name, barcode, price);
    }
}
