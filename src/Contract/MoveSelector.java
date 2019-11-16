package Contract;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface MoveSelector {
    /**
     * Returns selection of valid moves.
     */
    List<Move> getMoves(ReadableBoard board);
}
