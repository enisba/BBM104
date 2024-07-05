import java.util.HashMap;
import java.util.Map;

/**
 * Manages decorations and processes information related to the decoration of classrooms.
 * This class is responsible for parsing decoration and item files.
 * Calculates total costs for decorating classrooms.
 */
public class DecorationManager {
    private final Map<String, Decoration> decorations = new HashMap<>();

    /**
     * @param itemsFile An array of strings, where each string represents a line from the items file.
     */
    public void processItemsFile(String[] itemsFile) {
        for (String line : itemsFile) {
            String[] parts = line.split("\t");
            if ("DECORATION".equals(parts[0])) {
                Decoration decoration = createDecoration(parts);
                decorations.put(parts[1], decoration);
            }
        }
    }

    /**
     * Processes the decoration file and calculates the total cost of decorations specified
     * for each classroom. It utilizes both the decoration file and items file to achieve this.
     * @param decorationFile An array of strings, each representing a line from the decoration file.
     * @return A string summary of the decoration specifications and total cost.
     */
    public String processDecorationFile(String[] decorationFile, String[] itemsFile) {
        StringBuilder output = new StringBuilder();
        double totalCost = 0;
        for (String line : decorationFile) {
            String[] parts = line.split("\t");
            Builder builder = new Builder();

            for (String item : itemsFile) {
                String[] itemParts = item.split("\t");
                if ("CLASSROOM".equals(itemParts[0]) && itemParts[1].equals(parts[0])) {
                    Classroom classroom = configureClassroom(builder, itemParts, decorations.get(parts[1]), decorations.get(parts[2]));
                    double cost = classroom.calculateTotalCost();
                    totalCost += cost;
                    output.append(String.format("Classroom %s used %s for walls and used %s for flooring, these costed %.0fTL.\n",
                            classroom.getId(), formatDecoration(classroom.getWallDecoration(), Math.ceil(classroom.calculateWallArea())), formatDecoration(classroom.getFloorDecoration(), classroom.calculateFloorArea()), Math.ceil(cost)));
                }
            }
        }
        output.append(String.format("Total price is: %.0fTL.", Math.ceil(totalCost)));
        return output.toString();
    }
    /**
     * @param decoration The decoration object to be formatted.
     * @param area The area covered by the decoration.
     * @return A formatted string describing the decoration and its coverage area.
     */
    private String formatDecoration(Decoration decoration, double area) {
        if (decoration instanceof Tile) {
            double numberOfTiles = Math.ceil(area / decoration.area);
            return String.format("%.0f Tiles", numberOfTiles);
        } else {
            return String.format("%.0fm2 of %s", area, decoration);
        }
    }
    /**
     * @param builder An instance of Builder used to construct the Classroom object.
     * @param itemParts An array of strings containing the classroom and decoration details.
     * @param wallDecoration The decoration to be used for walls.
     * @param floorDecoration The decoration to be used for flooring.
     * @return A fully configured Classroom object.
     */
    private Classroom configureClassroom(Builder builder, String[] itemParts, Decoration wallDecoration, Decoration floorDecoration) {
        builder.setId(itemParts[1])
                .setWallDecoration(wallDecoration)
                .setFloorDecoration(floorDecoration);

        if ("Rectangle".equals(itemParts[2])) {
            return builder.setWidth(Double.parseDouble(itemParts[3]))
                    .setLength(Double.parseDouble(itemParts[4]))
                    .setHeight(Double.parseDouble(itemParts[5]))
                    .buildRectangle();
        } else {
            return builder.setRadius(Double.parseDouble(itemParts[3]))
                    .setHeight(Double.parseDouble(itemParts[5]))
                    .buildCircular();
        }
    }
    /**
     * @param parts An array of strings containing decoration details.
     * @return A Decoration object corresponding to the provided details or null if the type is unrecognized.
     */
    private Decoration createDecoration(String[] parts) {
        switch (parts[2]) {
            case "Paint":
                return new Paint(parts[1], Double.parseDouble(parts[3]));
            case "Tile":
                return new Tile(parts[1], Double.parseDouble(parts[3]), Double.parseDouble(parts[4]));
            case "Wallpaper":
                return new Wallpaper(parts[1], Double.parseDouble(parts[3]));
            default:
                return null;
        }
    }
}
