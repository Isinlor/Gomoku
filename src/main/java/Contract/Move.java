package Contract;

import java.io.Serializable;

public class Move implements Serializable {

    private static final long serialVersionUID = 1;

    final public int x;
    final public int y;

    public Move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isEqual(Move move) {
        return x == move.x && y == move.y;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return x == move.x && y == move.y;
    }

    public int hashCode() {
        return 100000 * x + y; // allows boards up to 999 in size
    }

    public String toString() {
        return "Move(x: " + x + ", y: " + y + ")";
    }

}
