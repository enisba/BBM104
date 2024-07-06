public class Main {
    public static void main(String[] args) {
        String[] decorationFile = FileInput.readFile(args[0], true, true);
        String[] itemsFile = FileInput.readFile(args[1], true, true);

        DecorationManager manager = new DecorationManager();
        manager.processItemsFile(itemsFile);
        String output = manager.processDecorationFile(decorationFile, itemsFile);

        FileOutput.writeToFile(args[2], output, false, false);
    }
}
