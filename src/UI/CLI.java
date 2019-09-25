package UI;

import Board.Board;
import Contract.BoardCell;
import Contract.Color;

import java.util.Scanner;

/**
 * Very simple CLI interface for prototyping and debugging.
 */
public class CLI {
    public static void main(String[] args) {

        Board board = new Board(5);

        while(!board.hasWinner()) {

            System.out.println(board.toString());
            System.out.println("Please, type your move (x y):\n");

            Color currentColor = board.getCurrentColor();
            while (currentColor == board.getCurrentColor()) {
                try {

                    Scanner sc = new Scanner(System.in);
                    int x = sc.nextInt();
                    int y = sc.nextInt();

                    board.move(x, y);

                } catch (Exception e) {
                    System.out.println(e.getClass().getCanonicalName() + "\n" + e.getMessage());
                    System.out.println("\nTry again:\n");
                }
            }

        }

        System.out.println("Game won by: " + board.getWinner());

    }
}
