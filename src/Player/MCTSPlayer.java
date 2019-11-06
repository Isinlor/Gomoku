package Player;

import Board.Helpers.ApproximateMoveSelector;
import Contract.*;
import Tree.MCTSNode;
import Board.SimpleBoard;
import Board.SimpleGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class MCTSPlayer implements Player {
    MCTSNode root = null;
    MoveSelector moveSelector;

    public MCTSPlayer(MoveSelector moveSelector){
        this.moveSelector = moveSelector;
    }

    @Override
    public Move getMove(ReadableBoard board) {

        //Initializes the tree with the current turn as root node
        //Root has a child for each possible turn
        root = new MCTSNode(board,null);
        Color MCTSColor = root.getState().getCurrentColor();

        //
        double startTime = System.currentTimeMillis();
        double timeSinceStart = 0;
        double allowedTime = 1000*5; //10 seconds
        double c = Math.sqrt(2); //EXPLORATION PARAMETER

        while(timeSinceStart<allowedTime){
            //Traverse till hasChildren = null
            //if leaf played = 0 -> rollout
            //if leaf played > 0 -> expand and rollout from random child
            MCTSNode leaf = root.traverse(c);
            if(leaf.getGamesPlayed()==0){
                leaf.expandTree(moveSelector);
                leaf = leaf.getChildren().get(0);
            }
            rollout(leaf,MCTSColor);
            timeSinceStart = System.currentTimeMillis()-startTime;
        }
//         TESTING THE CHILDNODES!!!!!!!!!
//        for(MCTSNode child:root.getChildren()) {
//            System.out.println(child.toString());
//            System.out.println(child.getGamesWon()+" Games won");
//            System.out.println(child.getGamesPlayed()+" Games played");
//        }
        return root.getBestChild().getLastMove();
    }

    public static Game setupGame() {

        Player blackPlayer = Players.getPlayer("random");
        Player whitePlayer = Players.getPlayer("random");

        return new SimpleGame(blackPlayer, whitePlayer);
    }

    public static void rollout(MCTSNode currentNode, Color MCTSColor){
        Game game = setupGame();
        SimpleBoard tempBoard = new SimpleBoard(currentNode.getState());
        game.play(tempBoard);
        Color winner = tempBoard.getWinner();
        if(winner == MCTSColor){
            currentNode.backpropagate(true);
        }else{
            currentNode.backpropagate(false);
        }
    }

}
