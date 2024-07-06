import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * the Main class serves as the entry point for the Gym Meal Machine program
 * it initializes the system, processes product and purchase files, and updates the output file.
 */
public class Main {
    /**
     * the main method that initializes and write the output file the GMM
     *
     * @param args command line arguments containing paths for product file, purchase file, and output file
     */
    public static void main(String[] args) {
        String productFile = args[0];
        String purchaseFile = args[1];
        String outputFile = args[2];
        GMMachine machine = new GMMachine();

        FileOutput.writeToFile(outputFile, "", false, false);

        // Process product and purchase inputs, then write the output file
        machine.inputProduct(productFile, outputFile);
        machine.printCurrentState(outputFile);
        machine.inputPurchase(purchaseFile, outputFile);
        machine.printCurrentState(outputFile);
    }

    public static class FileInput {
        /**
         * Reads the file at the given path and returns contents of it in a string array.
         *
         * @param path              Path to the file that is going to be read.
         * @param discardEmptyLines If true, discards empty lines with respect to trim; else, it takes all the lines from the file.
         * @param trim              Trim status; if true, trims (strip in Python) each line; else, it leaves each line as-is.
         * @return Contents of the file as a string array, returns null if there is not such a file or this program does not have sufficient permissions to read that file.
         */
        public static String[] readFile(String path, boolean discardEmptyLines, boolean trim) {
            try {
                List<String> lines = Files.readAllLines(Paths.get(path)); //Gets the content of file to the list.
                if (discardEmptyLines) { //Removes the lines that are empty with respect to trim.
                    lines.removeIf(line -> line.trim().equals(""));
                }
                if (trim) { //Trims each line.
                    lines.replaceAll(String::trim);
                }
                return lines.toArray(new String[0]);
            } catch (IOException e) { //Returns null if there is no such a file.
                e.printStackTrace();
                return null;
            }
        }
    }

    public static class FileOutput {
        /**
         * This method writes given content to file at given path.
         *
         * @param path    Path for the file content is going to be written.
         * @param content Content that is going to be written to file.
         * @param append  Append status, true if wanted to append to file if it exists, false if wanted to create file from zero.
         * @param newLine True if wanted to append a new line after content, false if vice versa.
         */
        public static void writeToFile(String path, String content, boolean append, boolean newLine) {
            PrintStream ps = null;
            try {
                ps = new PrintStream(new FileOutputStream(path, append));
                ps.print(content + (newLine ? "\n" : ""));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (ps != null) { //Flushes all the content and closes the stream if it has been successfully created.
                    ps.flush();
                    ps.close();
                }
            }
        }
    }

    public static class Product {
        private String _name;
        private int _price;
        private float _protein;
        private float _carb;
        private float _fat;
        private int _calorie;
        /**
         * Constructs a new {@code Product} with specified attributes.
         *
         * @param name The name of the product.
         * @param price The price of the product.
         * @param protein The protein content of the product.
         * @param carb The carbohydrate content of the product.
         * @param fat The fat content of the product.
         */
        public Product(String name, int price, float protein, float carb, float fat) {
            this._name = name;
            this._price = price;
            this._carb = carb;
            this._protein = protein;
            this._fat = fat;
            this._calorie = Math.round(4 * protein + 4 * carb + 9 * fat);
        }

        public String getName() {
            return _name;
        }

        public int getPrice() {
            return _price;
        }

        public float getCarb() {
            return _carb;
        }

        public float getProtein() {
            return _protein;
        }

        public float getFat() {
            return _fat;
        }

        public double getCalorie() {
            return _calorie;
        }
    }

    public static class GMMachine {
        private final int maxProducts = 10;
        private final int row = 6;
        private final int column = 4;
        private final int[][] productQty = new int[row][column];// Tracks the quantity of each product in the machine.
        private double[][] productCal = new double[row][column];// Tracks the calorie content of each product in the machine.
        private final Product[][] products = new Product[row][column];// Stores the products available in the machine.
        /**
         * Reads product information from a file and fills the machine with the specified products.
         *
         * @param productFile The path to the product information file.
         * @param outputFile The path to the output file where machine status will be written.
         */
        public void inputProduct(String productFile, String outputFile) {
            String[] productLines = FileInput.readFile(productFile, true, true);
            // dosyanın boş olup olmadığını kontrol etmece
            if (productLines == null) {
                return;
            }
            //
            for (String line : productLines){
                String[] parts = line.split("\t");

                if (parts.length >= 3) {
                    // ismi alma
                    String name = parts[0];
                    // parasını alma
                    int price = Integer.parseInt(parts[1]);
                    // besin değerlerini alma
                    String[] nutritionValues = parts[2].split(" ");

                    if (nutritionValues.length == 3) {
                        float protein = Float.parseFloat((nutritionValues[0]));
                        float carb = Float.parseFloat((nutritionValues[1]));
                        float fat = Float.parseFloat((nutritionValues[2]));
                        Product product = new Product(name, price, protein, carb, fat);

                        // fill methodundan -1 gelirse slotların hepsi dolmuş demektir ve de bu yüzden hata mesajı yazdırılacaktır
                        if (fill(outputFile, product, 1) == -1) {
                            return;
                        }
                    }
                }
            }
        }

        /**
         * Processes purchase information from a file and performs transactions based on the provided data.
         *
         * @param purchaseFile The path to the purchase information file.
         * @param outputFile The path to the output file where transaction details will be written.
         */
        public void inputPurchase(String purchaseFile, String outputFile) {
            String[] purchaseLines = FileInput.readFile(purchaseFile, true, true);

            // check if the file is empty
            if (purchaseLines == null){
                return;
            }
            // import purchase.txt file import line by line
            for (String line : purchaseLines) {
                FileOutput.writeToFile(outputFile, "INPUT: " + line, true, true);
                String[] parts = line.split(("\t"));
                String[] moneyParts = parts[1].split(" ");
                int[] cashAmounts = new int[moneyParts.length];
                // take cashAmounts and convert them to integer
                for (int i = 0; i < cashAmounts.length; i++) {
                    cashAmounts[i] = Integer.parseInt(moneyParts[i]);
                }
                // error if the currency is invalid
                if (!cashLira(cashAmounts)) {
                    FileOutput.writeToFile(outputFile, "INFO: Invalid money, your money will be returned.",
                            true, true);
                    continue;
                }
                // to calculate the total money
                int totalMoney = 0;
                for (int cash : cashAmounts) {
                    totalMoney += cash;
                }
                // choosing which type of product to buy
                if (parts.length == 4) {
                    String buyingType = parts[2];
                    // how much will it take
                    int buyingQty = Integer.parseInt(parts[3]);
                    switch (buyingType) {
                        case "NUMBER":
                            // method to get it according to the number
                            selectByNumber(buyingQty, totalMoney, outputFile);
                            break;
                        case "PROTEIN":
                        case "CARB":
                        case "FAT":
                        case "CALORIE":
                            // method made to get it according to its nutritional value
                            NutritionalValue(buyingType, buyingQty, totalMoney, outputFile);
                            break;
                    }
                }
            }
        }

        /**
         * Fills a slot in the machine with the specified product and quantity
         *
         * @param outputFile Path to the output file where information about the operation
         * @param product The product to be added to the machine
         * @param quantity The quantity of the product to be added
         * @return Returns 0 if the operation was successful or -1 if the machine is full
         */
        public int fill(String outputFile, Product product, int quantity) {
            boolean productPlaced = false;
            boolean anySpaceSlot = false;
            // algorithm that fills the slots and knows when they are full and when they are empty.
            for (int i = 0; i < row && !productPlaced; i++) {
                for (int j = 0; j < column && !productPlaced; j++) {
                    if (products[i][j] == null || products[i][j].getName().equals(product.getName())) {
                        if (products[i][j] == null) {
                            products[i][j] = product;
                            productQty[i][j] = 0;
                            productCal[i][j] = 0;
                        }
                        int remainSlot = maxProducts - productQty[i][j];
                        int loadSlot = Math.min(remainSlot, quantity);
                        productQty[i][j] += loadSlot;
                        productCal[i][j] += product.getCalorie() * loadSlot;
                        quantity -= loadSlot;
                        if (quantity == 0) productPlaced = true;
                    }
                }
            }
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    if (productQty[i][j] < maxProducts) {
                        anySpaceSlot = true;
                        break;
                    }
                }
                if (anySpaceSlot) {
                    break;
                }
            }
            if (!anySpaceSlot && !productPlaced) {
                FileOutput.writeToFile(outputFile, "INFO: There is no available place to put "
                        + product.getName(), true, true);
                FileOutput.writeToFile(outputFile, "INFO: The machine is full!", true, true);
                return -1;
            }
            if (!productPlaced && quantity > 0) {
                FileOutput.writeToFile(outputFile, "INFO: There is no available place to put "
                        + product.getName(), true, true);
                return 0;
            }
            return 0;
        }
        /**
         * Prints the current state of the machine to the specified output file.
         *
         * @param outputFile The path to the output file where the machine state will be written.
         */
        public void printCurrentState(String outputFile) {
                // algorithm that prints the current state of the machine
                FileOutput.writeToFile(outputFile, "-----Gym Meal Machine-----", true, true);
                for (int i = 0; i < row; i++) {
                    String line = "";
                    for (int j = 0; j < column; j++) {
                        if (products[i][j] != null && productQty[i][j] > 0) {
                            String productInfo = String.format("%s(%d, %d)___", products[i][j].getName(),
                                    (int) products[i][j].getCalorie(), productQty[i][j]);
                            line += productInfo;
                        } else {
                            line += "___(0, 0)___";
                        }
                    }
                    FileOutput.writeToFile(outputFile, line, true, true);
                }
                FileOutput.writeToFile(outputFile, "----------", true, true);
        }
        /**
         * Validates if the provided cash amounts are valid denominations.
         * @param cash An array of integers representing the cash amounts.
         * @return True if all cash amounts are valid denominations; otherwise, false.
         */
        public boolean cashLira(int[] cash) {
            for (int liras : cash) {
                if (liras != 1 && liras != 5 && liras != 10 && liras != 20
                        && liras != 50 && liras != 100 && liras != 200) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Selects a product based on its nutritional value and performs a transaction if possible
         * @param type The type of nutritional value to consider
         * @param requestedValue The requested value of the specified nutritional type
         * @param cash The total amount of cash provided for the purchase
         */
        public void NutritionalValue(String type, int requestedValue, int cash, String outputFile) {
            // algorithm used to deliver products according to their nutritional value
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    if (productQty[i][j] > 0) {
                        float value = 0;
                        switch (type) {
                            case "PROTEIN":
                                value = products[i][j].getProtein();
                                break;
                            case "CARB":
                                value = products[i][j].getCarb();
                                break;
                            case "FAT":
                                value = products[i][j].getFat();
                                break;
                            case "CALORIE":
                                value = (float) products[i][j].getCalorie();
                                break;
                        }
                        if (Math.abs(value - requestedValue) <= 5) {
                            if (products[i][j].getPrice() <= cash) {
                                productQty[i][j]--;
                                int change = cash - products[i][j].getPrice();
                                FileOutput.writeToFile(outputFile, "PURCHASE: You have bought one " +
                                        products[i][j].getName(), true, true);
                                FileOutput.writeToFile(outputFile, "RETURN: Returning your change: " + change +
                                        " TL", true, true);
                                return;
                            } else {
                                FileOutput.writeToFile(outputFile, "INFO: Insufficient money, try " +
                                        "again with more money.", true, true);
                                FileOutput.writeToFile(outputFile, "RETURN: Returning your change: " +
                                        cash + " TL", true, true);
                                return;
                            }
                        }
                    }
                }
            }
            FileOutput.writeToFile(outputFile, "INFO: Product not found, your money will be returned.",
                    true, true);
            FileOutput.writeToFile(outputFile, "RETURN: Returning your change: " + cash + " TL",
                    true, true);
        }
        /**
         * Selects a product by its slot number and performs a transaction if possible
         *
         * @param slotNumber the slot number from which the product is to be purchased
         * @param cash the total amount of cash provided for the purchase
         * @param outputFile The path to the output file where transaction details will be written
         */
        public void selectByNumber(int slotNumber, int cash, String outputFile) {
            // algorithm used to deliver products according to their numbers
            int indexRow = (slotNumber) / column;
            int indexColumn = (slotNumber) % column;

            if (indexRow >= row || slotNumber < 0) {
                FileOutput.writeToFile(outputFile, "INFO: Number cannot be accepted. Please try again " +
                        "with another number.", true, true);
                FileOutput.writeToFile(outputFile, "RETURN: Returning your change: " + cash + " TL",
                        true, true);
                return;
            }
            if (products[indexRow][indexColumn] == null || productQty[indexRow][indexColumn] <= 0) {
                FileOutput.writeToFile(outputFile, "INFO: This slot is empty, your money will be returned.",
                        true, true);
                FileOutput.writeToFile(outputFile, "RETURN: Returning your change: " + cash + " TL",
                        true, true);
                return;
            }

            if (cash < products[indexRow][indexColumn].getPrice()) {
                FileOutput.writeToFile(outputFile, "INFO: Insufficient money, try again with more money.",
                        true, true);
                FileOutput.writeToFile(outputFile, "RETURN: Returning your change: " + cash + " TL",
                        true, true);
                return;
            }
            productQty[indexRow][indexColumn]--;
            int change = cash - products[indexRow][indexColumn].getPrice();
            FileOutput.writeToFile(outputFile, "PURCHASE: You have bought one " +
                    products[indexRow][indexColumn].getName(), true, true);
            FileOutput.writeToFile(outputFile, "RETURN: Returning your change: " + change +
                    " TL", true, true);
        }
    }
}
