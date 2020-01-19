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
        Player player1 = Players.get(nameA);
        Player player2 = Players.get(nameB);

        compare(9, 100, nameA, player1, nameB, player2);

    }

    public static Results compare(int boardSize, int games, String name1, Player player1, String name2, Player player2) {
        return compare(boardSize, games, name1, player1, name2, player2, true);
    }

    public static Results compare(
        int boardSize, int games,
        String name1, Player player1,
        String name2, Player player2,
        boolean print
    ) {

        TimedPlayer playerA = new TimedPlayer(player1);
        TimedPlayer playerB = new TimedPlayer(player2);

        int winA = 0;
        int winB = 0;
        int draw = 0;

        double startTime = System.currentTimeMillis();
        for (int i = 0; i < games; i++) {

            // there is a difference between playing black and white
            // this flips color every iteration
            boolean flip = i % 2 == 0;

            SimpleBoard board = new SimpleBoard(boardSize);

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

            if(print) System.out.print(
                "[" + (i + 1) + "/" + games + "]\t" +
                    name1 + ": " + winA + "\t\t" + name2 + ": " + winB + "\tD: " + draw + "\r"
            );

        }

        if(print) {
            System.out.println();
            System.out.println(String.format("%1$-50s", "Time per move " + name1)
                + "\t\tavg: " + Utils.formatTime(playerA.getAverageTimePerMove())
                + "\t\tmax: " + Utils.formatTime(playerA.getMaxTimePerMove())
                + "\t\tmin: " + Utils.formatTime(playerA.getMinTimePerMove())
            );
            System.out.println(String.format("%1$-50s", "Time per move " + name2)
                + "\t\tavg: " + Utils.formatTime(playerB.getAverageTimePerMove())
                + "\t\tmax: " + Utils.formatTime(playerB.getMaxTimePerMove())
                + "\t\tmin: " + Utils.formatTime(playerB.getMinTimePerMove())
            );
            System.out.println();
        }

        return new Results(winA, winB, draw, playerA, playerB);

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

class Results {
    public int winA;
    public int winB;
    public int draws;
    public TimedPlayer playerA;
    public TimedPlayer playerB;
    public Results(int winA, int winB, int draws, TimedPlayer playerA, TimedPlayer playerB) {
        this.winA = winA;
        this.winB = winB;
        this.draws = draws;
        this.playerA = playerA;
        this.playerB = playerB;
    }
}