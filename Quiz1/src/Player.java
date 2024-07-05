
public class Player {
    private final String _name;
    private int _score = 0;
    private boolean _inGame = true;

    public Player(String name) {
        this._name = name;
    }

    public void throwDice(int d1, int d2) {
        if (!(d1 == 1 || d2 == 1)) {
            _score += d1 + d2;
        }
    }

    public String getName() {
        return _name;
    }

    public boolean inGame() {
        return _inGame;
    }

    public int getScore() {
        return _score;
    }
}