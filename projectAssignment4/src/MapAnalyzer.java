import java.util.Locale;
/**
 * The MapAnalyzer class serves as the main entry point for the route analysis application.
 * It sets the application's locale settings and initiates the processing of road data from input to output.
 */
public class MapAnalyzer {
    /**
     * The main method sets the default locale to US, reads the input and output file paths from command-line arguments,
     * and triggers the route processing through the RouteManager class.
     *
     * @param args Command-line arguments containing the paths to the input and output files.
     *             args[0] should be the input file path.
     *             args[1] should be the output file path.
     */
    public static void main(String[] args) {

        Locale.setDefault(Locale.US);

        String inputFile = args[0];
        String outputFile = args[1];

        RouteManager routeManager = new RouteManager();
        routeManager.processRoutes(inputFile, outputFile);
    }
}
