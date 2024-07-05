import java.io.*;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * The main class that processes inventory commands from a file.
 */
public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        String inputFile = args[0];
        String outputFile = args[1];

        Inventory<Item> inventory = new Inventory<>();

        try (Scanner scanner = new Scanner(new File(inputFile));
             PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\t");
                switch (parts[0]) {
                    case "ADD":
                        addItem(inventory, parts);
                        break;

                    case "REMOVE":
                        int barcode = Integer.parseInt(parts[1]);

                        if (inventory.itemRemove(barcode)) {
                            writer.println("REMOVE RESULTS:");
                            writer.println("Item is removed.");
                            writer.println("------------------------------");
                        } else {
                            writer.println("REMOVE RESULTS:");
                            writer.println("Item is not found.");
                            writer.println("------------------------------");
                        }
                        break;
                    case "SEARCHBYBARCODE":
                        barcode = Integer.parseInt(parts[1]);
                        Item item = inventory.searchBarcode(barcode);
                        if (item != null) {
                            writer.println("SEARCH RESULTS:");
                            writer.println(item);
                            writer.println("------------------------------");
                        } else {
                            writer.println("SEARCH RESULTS:");
                            writer.println("Item is not found.");
                            writer.println("------------------------------");
                        }
                        break;
                    case "SEARCHBYNAME":
                        String name = parts[1];
                        List<Item> items = inventory.searchName(name);
                        if (!items.isEmpty()) {
                            writer.println("SEARCH RESULTS:");
                            for (Item i : items) {
                                writer.println(i);
                            }
                            writer.println("------------------------------");
                        } else {
                            writer.println("SEARCH RESULTS:");
                            writer.println("Item is not found.");
                            writer.println("------------------------------");
                        }
                        break;
                    case "DISPLAY":
                        writer.println("INVENTORY:");
                        for (Item i : inventory.getSortedItems()) {
                            writer.println(i);
                        }
                        writer.println("------------------------------");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds an item to the inventory based on the input parts.
     *
     * @param inventory the inventory to add the item to
     * @param parts the input parts containing item details
     */
    private static void addItem(Inventory<Item> inventory, String[] parts) {
        String type = parts[1];
        switch (type) {
            case "Book":
                Book book = new Book(parts[2], parts[3], Integer.parseInt(parts[4]), Double.parseDouble(parts[5]));
                inventory.itemAdd(book);
                break;
            case "Toy":
                Toy toy = new Toy(parts[2], parts[3], Integer.parseInt(parts[4]), Double.parseDouble(parts[5]));
                inventory.itemAdd(toy);
                break;
            case "Stationery":
                Stationery stationery = new Stationery(parts[2], parts[3], Integer.parseInt(parts[4]), Double.parseDouble(parts[5]));
                inventory.itemAdd(stationery);
                break;
        }
    }
}