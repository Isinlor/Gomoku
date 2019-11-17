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
    double parameter;

    public MCTSPlayer(MoveSelector moveSelector, double parameter){
        this.moveSelector = moveSelector;
        this.parameter = parameter;
    }

    @Override
    public Move getMove(ReadableBoard board) {

        System.out.println(board);

        //Initializes the tree with the current turn as root node
        //Root has a child for each possible turn
        MCTSNode root = new MCTSNode(board,null,moveSelector);
        Color MCTSColor = root.getState().getCurrentColor();

        //
        double startTime = System.currentTimeMillis();
        int maxDepth = 100;
        double timeSinceStart = 0;
        double allowedTime = 1000*parameter; //20 seconds
        double c = Math.sqrt(2); //EXPLORATION PARAMETER sqrt(2)

        while(timeSinceStart<allowedTime){
            /*
            Traverse till hasUntriedMoves() is false, UntriedMoves are the possible moves from the current state
            Pick one of those moves and add it as a child node of the leaf.
            From this child (the new leaf) do a rollout
            */

            MCTSNode leaf = root.traverse(c);
//            if(leaf.getGamesPlayed()==0&&leaf.getDepth()<maxDepth){
//                leaf.expandTree(moveSelector);
//                if(!leaf.getChildren().isEmpty())
//                    leaf = leaf.getChildren().get(0);
//            }
            if(leaf.getState().hasWinner()){
                if(leaf.getState().getWinner()==MCTSColor) {
                    leaf.backpropagate(true);
                }else{
                    leaf.backpropagate(false);
                }
            }else if(!leaf.getUntriedMoves().isEmpty()){
                leaf = leaf.expandTree(moveSelector);
                rollout(leaf, MCTSColor);
            }



            timeSinceStart = System.currentTimeMillis()-startTime;
        }

        System.out.println(root.getGamesPlayed()+" Simulations run");

        //If there's a next move that wins, pick that
        for(MCTSNode node: root.getChildren()) {
            //Print Scores for each child node of Root for testing
            System.out.println(node.getGamesWon()+" Win");
            System.out.println(node.getGamesPlayed()+" Played");
            System.out.println(node.getWinRatio()+" Win ratio");
            System.out.println(node.getLastMove());
            System.out.println("---------");
            if(node.getState().getWinner() == MCTSColor) {
                System.out.println("MCTS win move: " + node.getLastMove());
                return node.getLastMove();
            }
        }

        Move selectedMove = root.getBestChild().getLastMove();

        System.out.println("MCTS selected move: " + selectedMove);

        return selectedMove;
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
