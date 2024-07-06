
public class DiceGame {
    public static void main(String[] args) {
        String inputFile = args[0];
        String outputFile = args[1];

        FileOutput.writeToFile(outputFile, "", false, false);

        Game game = new Game();
        game.startGame(inputFile, outputFile);
    }
}