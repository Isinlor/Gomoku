package Contract;

import java.util.Objects;

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

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return x == move.x && y == move.y;
    }

    public int hashCode() {
        return Objects.hash(x, y);
    }

    public String toString() {
        return "Move(x: " + x + ", y: " + y + ")";
    }

}
