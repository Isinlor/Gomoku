package Contract;

/**
 * Interface representing a player.
 */
public interface Player {
    /**
     * Allows the player to indicate the move to make.
     */
    Move getMove(ReadableBoard board);

}
