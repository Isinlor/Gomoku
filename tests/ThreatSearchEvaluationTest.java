import Board.SimpleBoard;
import Contract.BoardCell;
import Contract.Color;
import Contract.Move;
import Evaluation.ThreatSearchGlobal;

public class ThreatSearchEvaluationTest extends SimpleUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nEvaluation Threat Search Test\n");

        it("gives evaluation 0", () -> {
            BoardCell[][] boardState = {
                    {W, E, E, E, E},
                    {W, W, W, E, E},
                    {B, E, W, E, E},
                    {B, E, E, E, E},
                    {E, B, E, E, B},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(new ThreatSearchGlobal().evaluate(board) == 0
            );
        });

        it("gives 0  score to board ", () -> {
            BoardCell[][] boardState = {
                    {W, W, B, E, E},
                    {E, E, E, E, E},
                    {E, E, E, E, E},
                    {E, E, E, E, E},
                    {E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(new ThreatSearchGlobal().evaluate(board) == 0
            );
        });
    }

}
