package Contract;

public class Move {

    final public int x;
    final public int y;

    public Move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isEqual(Move move) {
        return x == move.x && y == move.y;
    }

    public String toString() {
        return "Move(x: " + x + ", y: " + y + ")";
    }

}
