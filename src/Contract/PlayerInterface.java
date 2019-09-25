package Contract;

/**
 * Interface representing a player.
 */
public interface PlayerInterface {
    /**
     * Allows the player to indicate the move to make.
     */
    Move getMove(BoardInterface board);
}
