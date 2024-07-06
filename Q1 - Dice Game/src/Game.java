public class Game {
    private Player[] players;

    public void startGame(String inputFile, String outputFile) {
        String[] lines = FileInput.readFile(inputFile, true, true);

        String[] playerNames = lines[1].split(",");
        players = new Player[playerNames.length];
        for (int i = 0; i < playerNames.length; i++) {
            players[i] = new Player(playerNames[i]);
        }
        for (int i = 2; i < lines.length; i++) {
            gameMoves(lines[i], i - 2, outputFile);
        }
        luckyWinner(outputFile);
    }

    private void gameMoves(String line, int turnPlayer, String outputFile) {
        String[] parts = line.split("-");
        int d1 = Integer.parseInt(parts[0]);
        int d2 = Integer.parseInt(parts[1]);
        Player currPlayer = players[turnPlayer % players.length];
        if (d1 == 0 && d2 == 0) {
            FileOutput.writeToFile(outputFile, currPlayer.getName() + " skipped the turn and " +
                    currPlayer.getName() + "’s score is " + currPlayer.getScore() + ".", true, true);
        } else {
            currPlayer.throwDice(d1, d2);
            if (d1 == 1 && d2 == 1) {
                FileOutput.writeToFile(outputFile, currPlayer.getName() + " threw 1-1. Game over " + currPlayer.getName() + "!", true, true );
                int playerIndex = -1;
                for (int i = 0; i < players.length; i++) {
                    if (players[i].getName() == currPlayer.getName()){
                        playerIndex = i;
                        break;
                    }
                }

                if (playerIndex != -1) {
                    Player[] newPlayers = new Player[players.length - 1];
                    for (int i = 0, j = 0; i < players.length; i++) {
                        if (i != playerIndex) {
                            newPlayers[j++] = players[i];
                        }
                    }
                    players = newPlayers;
                }
            } else {
                FileOutput.writeToFile(outputFile, currPlayer.getName() + " threw " + d1 + "-" + d2 + " and " + currPlayer.getName() + "’s score is " + currPlayer.getScore() + ".", true, true );
            }
        }
    }

    private void luckyWinner(String outputFile) {
        for (Player player : players) {
            if (player.inGame()) {
                FileOutput.writeToFile(outputFile, player.getName() + " is the winner of the game with the score of " + player.getScore() + ". Congratulations " + player.getName() + "!", true, false);
                break;
            }
        }
    }
}