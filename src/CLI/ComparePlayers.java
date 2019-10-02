package CLI;

import Board.*;
import Contract.*;
import Player.*;
import Evaluation.*;

import java.util.Scanner;

public class ComparePlayers {
    public static void main(String[] args) {

        Game game = CLI.setupGame();

        int blackWin = 0;
        int whiteWin = 0;
        int draw = 0;

        for (int i = 0; i < 100; i++) {

            SimpleBoard board = new SimpleBoard(5);
            game.play(board);

            if(board.getWinner() == null) {
                draw++;
                continue;
            }

            switch (board.getWinner()) {
                case Black:
                    blackWin++;
                    break;
                case White:
                    whiteWin++;
                    break;
            }
            
        }

        System.out.println("Black win: " + blackWin);
        System.out.println("White win: " + whiteWin);
        System.out.println("Draw: " + draw);

    }

}
