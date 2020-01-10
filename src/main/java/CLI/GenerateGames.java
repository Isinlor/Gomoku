package CLI;

import Board.Helpers.MoveSelectors;
import Board.SimpleBoard;import Board.TrainingGame;
import Contract.Color;
import Contract.Player;
import Player.Players;
import Player.MCTSPlayer;

import java.io.*;
import java.util.Scanner;

public class GenerateGames {


    public static void main(String[] args) {
        try {

            String filePath = "src/main/resources/games.ser";
            if (args.length > 0) {
                filePath = args[0];
            }
            File file = new File(filePath);
            FileOutputStream fileOut = new FileOutputStream(file, false);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            MCTSPlayer player = new MCTSPlayer(MoveSelectors.get("forced3"), 0.01);

            for (int i = 0; i < 1000; i++) {
                TrainingGame game = new TrainingGame(player, player);
                SimpleBoard board = new SimpleBoard(9);
                game.play(board);
                System.out.println("Game " + i + " won by: " + game.getWinner());
                System.out.println("Number of turns: " + game.getHistory().size());
                out.writeObject(game);
            }
            out.close();
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
