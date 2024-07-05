public class Book extends Item {
    private String author;

    /**
     * Constructs a new Book.
     *
     * @param name the name of the book
     * @param author the author of the book
     * @param barcode the barcode of the book
     * @param price the price of the book
     */
    public Book(String name, String author, int barcode, double price) {
        super(name, barcode, price);
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return String.format("Author of the %s is %s. Its barcode is %d and its price is %s", getName(), author, getBarcode(), getPrice());
    }
}
