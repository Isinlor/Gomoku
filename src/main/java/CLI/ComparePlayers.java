package CLI;

import Board.*;
import Contract.*;
import Player.*;
import Evaluation.*;
import UI.Logger;

import java.util.Scanner;

public class ComparePlayers {
    public static void main(String[] args) {

        Logger.enabled = false;
        System.out.println("Available players: " + Players.getNames());

        String nameA = selectPlayer();
        String nameB = selectPlayer();
        TimedPlayer playerA = new TimedPlayer(Players.get(nameA));
        TimedPlayer playerB = new TimedPlayer(Players.get(nameB));

        int winA = 0;
        int winB = 0;
        int draw = 0;

        double startTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {

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
        System.out.println("Final result...");
        System.out.println(nameA + ": " + winA + "\t" + nameB + ": " + winB + "\tD: " + draw);
        System.out.println("Time: " + (System.currentTimeMillis() - startTime));

        System.out.println();
        System.out.println("Avg time per move " + nameA + ": " + Utils.formatTime(playerA.getAverageTimePerMove()));
        System.out.println("Max time per move " + nameA + ": " + Utils.formatTime(playerA.getMaxTimePerMove()));
        System.out.println("Min time per move " + nameA + ": " + Utils.formatTime(playerA.getMinTimePerMove()));

        System.out.println();
        System.out.println("Avg time per move " + nameB + ": " + Utils.formatTime(playerB.getAverageTimePerMove()));
        System.out.println("Max time per move " + nameB + ": " + Utils.formatTime(playerB.getMaxTimePerMove()));
        System.out.println("Min time per move " + nameB + ": " + Utils.formatTime(playerB.getMinTimePerMove()));

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
