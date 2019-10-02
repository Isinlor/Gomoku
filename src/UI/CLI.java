package UI;

import Board.*;
import Contract.*;
import Player.RandomPlayer;

import java.util.Scanner;

/**
 * Very simple CLI interface for prototyping and debugging.
 */
public class CLI {
    public static void main(String[] args) {

        Player cliPlayer = (ReadableBoard currentBoard) -> {

            System.out.println(currentBoard.toString());

            Move move = null;
            while(move == null || !currentBoard.isValidMove(move)) {
                System.out.println("Please, type your move (x y):\n");

                Scanner sc = new Scanner(System.in);
                int x = sc.nextInt();
                int y = sc.nextInt();

                move = new Move(x, y);
            }

            return move;

        };

        Player randomPlayer = new RandomPlayer();

        Game game = new SimpleGame(randomPlayer, cliPlayer);
        SimpleBoard board = new SimpleBoard(5);

        game.play(board);

        System.out.println("Game won by: " + board.getWinner());

    }
}
