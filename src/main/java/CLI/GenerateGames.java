package CLI;

import Board.Helpers.MoveSelectors;
import Board.SimpleBoard;import Board.TrainingGame;
import Contract.Color;
import Contract.Player;
import Player.Players;
import Player.MCTSPlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class GenerateGames {


    public static void main(String[] args) {
        try {

            String filePath = "src/main/resources/games.ser";
            if(args.length>0) {
                filePath = args[0];
            }
            File file = new File(filePath);
            FileOutputStream fileOut = new FileOutputStream(file,true);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            MCTSPlayer player = new MCTSPlayer(MoveSelectors.get("approximate"),0.01);

        while (true) {
            TrainingGame game = new TrainingGame(player, player);
            SimpleBoard board = new SimpleBoard(9);
            game.play(board);
            System.out.println("Game won by: " + game.getWinner());
            System.out.println("Number of turns: " + game.getHistory().size());
            out.writeObject(game);
        }

        } catch (IOException i) {
            i.printStackTrace();
        }

    }
}
