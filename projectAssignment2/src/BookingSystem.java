import java.io.RandomAccessFile;
import java.util.Locale;

public class BookingSystem {

    /**
     * The main method that runs the booking system.
     * It reads commands from the specified file, processes them, and then cleans up the output file.
     *
     * @param args Command line arguments where args[0] is the input file path and args[1] is the output file path.
     */
    public static void main(String[] args) {
        // Set default locale to US for "," to ".".
        Locale.setDefault(Locale.US);

        // Read commands from the input file.
        String[] commands = FileInput.readFile(args[0], true, true);

        String outputFile = args[1];
        // outputfile writing process
        FileOutput.writeToFile(outputFile, "", false, false);

        // Process each command using the VoyageManagementSystem.
        for (String commandLine : commands) {
            VoyageManagementSystem.processCommand(commandLine, outputFile);
        }

        // Check if the last command is not "Z_REPORT" and call generateZReport
        if (commands.length == 0 || !commands[commands.length - 1].trim().startsWith("Z_REPORT")) {
            String[] defaultParts = {"Z_REPORT"};
            VoyageManagementSystem.generateZReport(defaultParts, outputFile);
        }


        // Remove the last empty line from the output file to clean up after processing.
        try {
            RandomAccessFile file = new RandomAccessFile(args[1], "rw");
            long length = file.length();
            file.seek(length - 1);
            file.setLength(length - 1);
            file.close();
        } catch (Exception ignored) {
        }
    }
}
