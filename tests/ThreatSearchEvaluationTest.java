import Board.SimpleBoard;
import Contract.BoardCell;
import Contract.Color;
import Contract.Move;
import Evaluation.ThreatSearchGlobal;

public class ThreatSearchEvaluationTest extends SimpleUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nEvaluation Threat Search Test\n");

        it("gives 0 score to board with no winner and loser", () -> {
            BoardCell[][] boardState = {
                    {W, E, E, E, E},
                    {W, B, B, B, E},
                    {W, E, E, E, E},
                    {E, E, E, E, E},
                    {E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(new ThreatSearchGlobal().evaluate(board) == 0
            );
        });
    }

}
