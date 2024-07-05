import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The RouteManager class handles the process of reading road data from an input file,
 * computing the shortest and minimal routes, and writing the results to an output file.
 */
public class RouteManager {
    /**
     * Processes the routes by reading an input file, calculating the necessary routes,
     * and writing the analysis results to an output file.
     *
     * @param inputFile the path to the input file containing road data and start/end points
     * @param outputFile the path to the output file where the results will be written
     */
    public void processRoutes(String inputFile, String outputFile) {
        String[] lines = FileInput.readFile(inputFile, true, true);

        String[] points = lines[0].split("\t");
        String start = points[0];
        String end = points[1];

        List<Road> roads = parseRoads(lines);

        RoutePlanner planner = new RoutePlanner(roads);

        List<Road> shortestRoute = getShortestRoute(planner, start, end, roads);
        List<Road> barelyConnectedMap = planner.calculateBarelyConnectedMap();
        List<Road> shortestRouteBarelyConnected = getShortestRoute(planner, start, end, barelyConnectedMap);

        Map<String, Double> analysisResults = planner.calculateMapsRatio(start, end, shortestRoute, barelyConnectedMap);

        writeOutputFile(outputFile, start, end, shortestRoute, barelyConnectedMap, shortestRouteBarelyConnected, analysisResults, planner);
    }

    /**
     * Parses road data from the provided lines of text into a list of Road objects.
     *
     * @param lines an array of strings, each representing a line in the input file
     * @return a list of Road objects parsed from the input lines
     */
    private List<Road> parseRoads(String[] lines) {
        List<Road> roads = new ArrayList<>();
        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].split("\t");
            if (parts.length >= 4) {
                roads.add(new Road(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
            }
        }
        return roads;
    }

    /**
     * Retrieves the shortest route between a start and end point using a specified list of roads.
     *
     * @param planner the RoutePlanner instance used for calculating routes
     * @param start the starting point of the route
     * @param end the end point of the route
     * @param roads the list of roads to be used for calculating the shortest route
     * @return a list of Roads that forms the shortest route from start to end
     */
    private List<Road> getShortestRoute(RoutePlanner planner, String start, String end, List<Road> roads) {
        planner.calculateShortestRoute(start, roads);
        return planner.getShortestRoute(start, end);
    }

    /**
     * Writes the results of the route analysis to an output file.
     *
     * @param outputFile the file path where the results will be written
     * @param start the starting point of the route
     * @param end the end point of the route
     * @param shortestRoute the shortest route from start to end
     * @param barelyConnectedMap the list of roads that form the barely connected map
     * @param shortestRouteBarelyConnected the shortest route on the barely connected map
     * @param analysisResults the results of the route analysis
     * @param planner the RoutePlanner instance used for route calculations
     */
    private void writeOutputFile(String outputFile, String start, String end, List<Road> shortestRoute, List<Road> barelyConnectedMap, List<Road> shortestRouteBarelyConnected, Map<String, Double> analysisResults, RoutePlanner planner) {
        FileOutput.writeToFile(outputFile, "Fastest Route from " + start + " to " + end + " (" + planner.calculateRouteDistance(shortestRoute) + " KM):\n", false, false);
        for (Road road : shortestRoute) {
            FileOutput.writeToFile(outputFile, road.toString() + "\n", true, false);
        }

        FileOutput.writeToFile(outputFile, "Roads of Barely Connected Map is:\n", true, false);
        for (Road road : barelyConnectedMap) {
            FileOutput.writeToFile(outputFile, road.toString() + "\n", true, false);
        }

        FileOutput.writeToFile(outputFile, "Fastest Route from " + start + " to " + end + " on Barely Connected Map (" + planner.calculateRouteDistance(shortestRouteBarelyConnected) + " KM):\n", true, false);
        for (Road road : shortestRouteBarelyConnected) {
            FileOutput.writeToFile(outputFile, road.toString() + "\n", true, false);
        }

        FileOutput.writeToFile(outputFile, "Analysis:\n", true, false);
        double materialRatio = analysisResults.get("materialRatio");
        double routeRatio = analysisResults.get("routeRatio");
        FileOutput.writeToFile(outputFile, "Ratio of Construction Material Usage Between Barely Connected and Original Map: " + String.format("%.2f", materialRatio) + "\n", true, false);
        FileOutput.writeToFile(outputFile, "Ratio of Fastest Route Between Barely Connected and Original Map: " + String.format("%.2f", routeRatio), true, false);

    }
}
