import Board.SimpleBoard;
import Contract.BoardCell;
import Contract.Color;
import Contract.Move;
import Evaluation.ThreatSearchGlobal;

public class ThreatSearchEvaluationTest extends SimpleUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nEvaluation Threat Search Test\n");

        it("gives score -1 to White because Black Half Closed 2 in a row", () -> {
            BoardCell[][] boardState = {
                {E, E, E, E, E},
                {E, E, E, E, E},
                {W, B, B, E, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertWhite(board.getCurrentColor());
            assertEqual(new ThreatSearchGlobal().evaluate(board), -1, 0.1);
        });

        it("gives score -4 to White because Black Open 2 in a row", () -> {
            BoardCell[][] boardState = {
                {E, E, E, E, E},
                {E, E, E, E, E},
                {E, B, B, E, W},
                {E, E, E, E, E},
                {E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertWhite(board.getCurrentColor());
            assertEqual(new ThreatSearchGlobal().evaluate(board), -4,0.1);
        });

        it("gives score -9 to White because Black Open 3 in a row", () -> {
            BoardCell[][] boardState = {
                {E, E, E, E, E},
                {E, E, E, E, E},
                {E, B, B, B, E},
                {E, E, E, E, E},
                {W, E, E, E, W},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertWhite(board.getCurrentColor());
            assertEqual(new ThreatSearchGlobal().evaluate(board), -9,0.1);
        });

        it("gives score -9 to White because Black Open 3 in a diagonal", () -> {
            BoardCell[][] boardState = {
                {W, E, E, E, E},
                {E, E, E, B, E},
                {E, E, B, E, E},
                {E, B, E, E, E},
                {E, E, E, E, W},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertWhite(board.getCurrentColor());
            assertEqual(new ThreatSearchGlobal().evaluate(board), -9,0.1);
        });

        it("gives score -16 to White because Black Open 4 in a diagonal", () -> {
            BoardCell[][] boardState = {
                {W, E, E, E, E, E, W},
                {E, E, E, E, E, E, E},
                {E, E, E, E, B, E, E},
                {E, E, E, B, E, E, E},
                {E, E, B, E, E, E, E},
                {E, B, E, E, E, E, E},
                {E, E, E, E, E, E, W},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertWhite(board.getCurrentColor());
            assertEqual(new ThreatSearchGlobal().evaluate(board), -16,0.1);
        });

        it("gives score 9 to Black because Black Open 3 in a row", () -> {
            BoardCell[][] boardState = {
                {E, E, W, E, E},
                {E, E, E, E, E},
                {E, B, B, B, E},
                {E, E, E, E, E},
                {W, E, E, E, W},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertBlack(board.getCurrentColor());
            assertEqual(new ThreatSearchGlobal().evaluate(board), 9,0.1);
        });

        it("evaluates a complex situation", () -> {
            BoardCell[][] boardState = {
                {E, E, E, E, E, E, E},
                {E, E, W, W, E, E, E},
                {E, E, W, E, B, E, E},
                {E, E, E, B, E, E, E},
                {E, E, B, E, E, E, E},
                {E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertBlack(board.getCurrentColor());
            assertEqual(new ThreatSearchGlobal().evaluate(board), 9 - (4*3), 0.1);
        });

    }

}
