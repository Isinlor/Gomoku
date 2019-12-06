package Contract;

import java.util.Collection;

public interface MoveSelector {
    /**
     * Returns selection of valid moves.
     */
    Collection<Move> getMoves(ReadableBoard board);
}
