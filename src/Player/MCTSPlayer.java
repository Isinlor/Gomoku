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

    MoveSelector moveSelector;

    public MCTSPlayer(MoveSelector moveSelector){
        this.moveSelector = moveSelector;
    }

    @Override
    public Move getMove(ReadableBoard board) {

        System.out.println(board.toString());

        //Initializes the tree with the current turn as root node
        //Root has a child for each possible turn
        MCTSNode root = new MCTSNode(board,null);
        Color MCTSColor = root.getState().getCurrentColor();

        //
        double startTime = System.currentTimeMillis();
        int maxDepth = 100;
        double timeSinceStart = 0;
        double allowedTime = 1000*20; //10 seconds
        double c = Math.sqrt(2); //EXPLORATION PARAMETER

        while(timeSinceStart<allowedTime){
            //Traverse till hasChildren = null
            //if leaf played = 0 -> rollout
            //if leaf played > 0 -> expand and rollout from random child
            MCTSNode leaf = root.traverse(c);
            if(leaf.getGamesPlayed()==0&&leaf.getDepth()<maxDepth){
                leaf.expandTree(moveSelector);
                if(!leaf.getChildren().isEmpty())
                    leaf = leaf.getChildren().get(0);
            }
            rollout(leaf,MCTSColor);
            timeSinceStart = System.currentTimeMillis()-startTime;
        }

        //System.out.println(root.getGamesPlayed()+" Simulations run");

        //If there's a next move that wins, pick that
        for(int i=0;i<root.getChildren().size();i++){
            if(root.getChildren().get(i).getState().getWinner()==MCTSColor) {
                return root.getChildren().get(i).getLastMove();
            }
        }

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
