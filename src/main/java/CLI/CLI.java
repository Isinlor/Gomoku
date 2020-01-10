package CLI;

import Board.*;
import Contract.*;
import Player.*;

import java.util.Scanner;

/**
 * Very simple CLI interface for prototyping and debugging.
 */
public class CLI {

    public static void main(String[] args) {

        Game game = args.length == 0 ? setupGame() : new SimpleGame(Players.get(args[0]), Players.get(args[1]));

        SimpleBoard board = new SimpleBoard(8);

        game.play(board);

        System.out.println("Game won by: " + board.getWinner());

    }

    public static Game setupGame() {
        System.out.println("Available players: " + Players.getNames());

        Player blackPlayer = selectPlayer(Color.Black);
        Player whitePlayer = selectPlayer(Color.White);

        return new SimpleGame(blackPlayer, whitePlayer);
    }

    public static Player selectPlayer(Color color) {
        String playerName = null;
        while(!Players.getNames().contains(playerName)) {
            System.out.println("Select " + color.name() + " player:\n");
            Scanner sc = new Scanner(System.in);
            playerName = sc.nextLine();
        }
        return Players.get(playerName);
    }

}
