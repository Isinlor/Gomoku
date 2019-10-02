package Player;

import Contract.Move;
import Contract.Player;
import Contract.ReadableBoard;

import java.util.Scanner;

public class CliPlayer implements Player {
    public Move getMove(ReadableBoard board) {

        System.out.println(board.toString());

        Move move = null;
        while(move == null || !board.isValidMove(move)) {
            System.out.println("Please, type your move (x y):\n");

            Scanner sc = new Scanner(System.in);
            int x = sc.nextInt();
            int y = sc.nextInt();

            move = new Move(x, y);
        }

        return move;

    }
}
