package CLI;

import Board.*;
import Contract.*;
import Player.*;
import Evaluation.*;

import java.util.Scanner;

public class ComparePlayers {
    public static void main(String[] args) {

        System.out.println("Available players: " + Players.getNames());

        String nameA = selectPlayer();
        String nameB = selectPlayer();
        Player playerA = Players.get(nameA);
        Player playerB = Players.get(nameB);

        int winA = 0;
        int winB = 0;
        int draw = 0;

        double startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {

            // there is a difference between playing black and white
            // this flips color every iteration
            boolean flip = i % 2 == 0;

            SimpleBoard board = new SimpleBoard(7);

            new SimpleGame(
                flip ? playerA : playerB,
                flip ? playerB : playerA
            ).play(board);

            if(board.getWinner() == null) {
                draw++;
            } else {
                switch (board.getWinner()) {
                    case Black:
                        if(flip) {
                            winA++;
                        } else {
                            winB++;
                        }
                        break;
                    case White:
                        if(flip) {
                            winB++;
                        } else {
                            winA++;
                        }
                        break;
                }
            }

            System.out.println(nameA + ": " + winA + "\t" + nameB + ": " + winB + "\tD: " + draw);

        }

        System.out.println();
        System.out.println(nameA + ": " + winA + "\t" + nameB + ": " + winB + "\tD: " + draw);
        System.out.println("Time: " + (System.currentTimeMillis() - startTime));

    }

    private static String selectPlayer() {
        String playerName = null;
        while(!Players.getNames().contains(playerName)) {
            System.out.print("Select player: ");
            Scanner sc = new Scanner(System.in);
            playerName = sc.nextLine();
        }
        System.out.println();
        return playerName;
    }

}
