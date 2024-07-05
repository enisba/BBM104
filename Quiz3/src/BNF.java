import java.util.*;

/**
 * This class represents a parser for Backus-Naur Form (BNF) grammars.
 * It reads BNF rules from a file, processes them.
 */
public class BNF {

    public static void main(String[] args) {

        BNF processor = new BNF(args[0]); // get input file
        String result = processor.generate("S");
        String outputFile = args[1]; // get output file

        FileOutput.writeToFile(outputFile, result, false, false); // write output file

    }

    private final Map<String, Collection<String>> rules = new HashMap<>();

    /**
     * Constructs a BNF parser.
     *
     * @param inputFile The path to the file containing BNF rules.
     */
    public BNF(String inputFile){
        String[] lines = FileInput.readFile(inputFile, true, true);
        assert lines != null;
        loadRules(lines);
    }

    /**
     * Loads BNF rules from an array according to given rules.
     *
     * @param lines An array of strings.
     */
    private void loadRules(String[] lines) {
        for (String line : lines) {
            String[] parts = line.split("->");
            String left = parts[0].trim();
            String[] productions = parts[1].split("\\|");
            rules.put(left, new ArrayList<>(Arrays.asList(productions)));
        }
    }

    /**
     * Generates all possible strings that can be derived from a given non-terminal.
     *
     * @param nonTerminal symbol to expand.
     * @return A string representing all derivations from the non-terminal.
     */
    public String generate(String nonTerminal) {
        if (!rules.containsKey(nonTerminal)) return nonTerminal;

        Collection<String> productions = rules.get(nonTerminal);
        StringBuilder result = new StringBuilder("(");
        for (String production : productions) {
            for (int i = 0; i < production.length(); i++) {
                result.append(generate(String.valueOf(production.charAt(i))));
            }
            result.append("|");
        }
        result.setLength(result.length() - 1);
        result.append(")");
        return result.toString();
    }


}
