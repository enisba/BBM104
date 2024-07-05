import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages a collection of items.
 *
 * @param <T> the type of items in the inventory
 */
public class Inventory<T extends Item> {
    private List<T> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    /**
     * Adds an item to the inventory.
     *
     * @param item the item to add
     */
    public void itemAdd(T item) {
        items.add(item);
    }

    /**
     * Removes an item from the inventory by barcode.
     *
     * @param barcode the barcode of the item to remove
     * @return true if the item was removed, false otherwise
     */
    public boolean itemRemove(int barcode) {
        return items.removeIf(item -> item.getBarcode() == barcode);
    }

    /**
     * Searches for an item by barcode.
     *
     * @param barcode the barcode of the item to search for
     * @return the item with the given barcode, or null if not found
     */
    public T searchBarcode(int barcode) {
        return items.stream()
                .filter(item -> item.getBarcode() == barcode)
                .findFirst()
                .orElse(null);
    }

    /**
     * Searches for items by name.
     *
     * @param name the name of the items to search for
     * @return a list of items with the given name
     */
    public List<T> searchName(String name) {
        return items.stream()
                .filter(item -> item.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    public List<T> getItems() {
        return items;
    }

    /**
     * Returns the items in the inventory, sorted by type and then by insertion order.
     *
     * @return the sorted items in the inventory
     */
    public List<T> getSortedItems() {
        return items.stream()
                .sorted(Comparator.comparingInt((Item item) -> {
                    if (item instanceof Book) return 1;
                    if (item instanceof Toy) return 2;
                    return 3;
                }).thenComparing(items::indexOf))
                .collect(Collectors.toList());
    }
}
