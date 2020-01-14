package Board;

import Contract.Move;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Moves implements Iterable<Move> {

    private HashSet<Move> moves;

    public Moves() {
        this(new HashSet<Move>());
    }

    public Moves(HashSet<Move> moves) {
        this.moves = moves;
    }

    public boolean isEmpty() {
        return moves.isEmpty();
    }

    /**
     * The copy is necessary to avoid concurrent access error.
     * The error occurs because without clone the same instance can be modified outside board.
     */
    public Set<Move> getCopy() {
        return (HashSet<Move>)moves.clone();
    }

    public Iterator<Move> iterator() {
        return moves.iterator();
    }

    public void forEach(Consumer<? super Move> consumer) {
        moves.forEach(consumer);
    }

    public Spliterator<Move> spliterator() {
        return moves.spliterator();
    }

}
