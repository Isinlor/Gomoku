package Player;

import Contract.*;
import Tree.MCTSNode;
import Board.SimpleBoard;
import Board.SimpleGame;

public class MCTSPlayer implements Player {

    private boolean useMaxIterations;
    private int maxIterations = -1;
    public Boolean debug = false;
    private MoveSelector smartMoveSelector;
    private MoveSelector quickMoveSelector;
    private double parameter;

    public MCTSPlayer(MoveSelector smartMoveSelector, double parameter){
        this.smartMoveSelector = smartMoveSelector;
        this.parameter = parameter;
        this.useMaxIterations = false;
    }

    public MCTSPlayer(MoveSelector smartMoveSelector, double parameter, boolean useMaxIterations){
        this.smartMoveSelector = smartMoveSelector;
        this.parameter = parameter;
        this.maxIterations = (int) parameter;
        this.useMaxIterations = useMaxIterations;
    }

    public MCTSPlayer(MoveSelector smartMoveSelector, MoveSelector quickMoveSelector, double parameter){
        this.smartMoveSelector = smartMoveSelector;
        this.quickMoveSelector = quickMoveSelector;
        this.parameter = parameter;
        this.useMaxIterations = false;
    }


    public MCTSPlayer(MoveSelector smartMoveSelector, MoveSelector quickMoveSelector, double parameter, boolean useMaxIterations){
        this.smartMoveSelector = smartMoveSelector;
        this.quickMoveSelector = quickMoveSelector;
        this.parameter = parameter;
        this.maxIterations = (int) parameter;
        this.useMaxIterations = useMaxIterations;
    }

    @Override
    public Move getMove(ReadableBoard board) {

        if(debug) {
            System.out.println(board);
        }

        //Initializes the tree with the current turn as root node
        //Root has a child for each possible turn
        MCTSNode root = new MCTSNode(board,null, smartMoveSelector);
        Color MCTSColor = root.getState().getCurrentColor();

        // return immediately if there is only one move to consider
        if(root.getUntriedMoves().size() == 1) {
            return root.getUntriedMoves().get(0);
        }

        //
        double startTime = System.currentTimeMillis();
        int maxDepth = 100;
        double timeSinceStart = 0;
        double allowedTime = 1000*parameter; //20 seconds
        double c = Math.sqrt(2); //EXPLORATION PARAMETER sqrt(2)

        MoveSelector quickMoveSelector = this.quickMoveSelector;
        if(quickMoveSelector == null) {
            quickMoveSelector = smartMoveSelector;
        }

        int iterations = 0;


        while(this.useMaxIterations ? (iterations < this.maxIterations) : (timeSinceStart < allowedTime)){
            iterations++;
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
                if(leaf.getState().getWinner() == MCTSColor) {
                    leaf.backpropagate(Result.Win);
                } else if(leaf.getState().getWinner() == null) {
                    leaf.backpropagate(Result.Draw);
                } else {
                    leaf.backpropagate(Result.Lose);
                }
            }else if(!leaf.getUntriedMoves().isEmpty()){
                leaf = leaf.expandTree(quickMoveSelector);
                rollout(leaf, MCTSColor);
            }

            timeSinceStart = System.currentTimeMillis()-startTime;
        }


        if(debug) {
            System.out.println("MCTS iterations: " + iterations);
            System.out.println(root.getGamesPlayed() + " Simulations run");

            //If there's a next move that wins, pick that
            for (MCTSNode node : root.getChildren()) {
                //Print Scores for each child node of Root for testing
                System.out.println(node.getGamesWon() + " Win");
                System.out.println(node.getGamesPlayed() + " Played");
                System.out.println(node.getWinRatio() + " Win ratio");
                System.out.println(node.getLastMove());
                System.out.println("---------");
                if (node.getStateWinner() == MCTSColor) {
                    System.out.println("MCTS win move: " + node.getLastMove());
                }
            }
        }

        Move selectedMove = root.getBestChild().getLastMove();
        if(debug) {
            System.out.println("MCTS selected move: " + selectedMove);
        }

        return selectedMove;
    }

    public static Game setupGame() {

        Player blackPlayer = Players.get("random");
        Player whitePlayer = Players.get("random");

        return new SimpleGame(blackPlayer, whitePlayer);
    }

    public static void rollout(MCTSNode currentNode, Color MCTSColor){
        Game game = setupGame();
        SimpleBoard tempBoard = new SimpleBoard(currentNode.getState());
        game.play(tempBoard);
        Color winner = tempBoard.getWinner();
        if(winner == MCTSColor) {
            currentNode.backpropagate(Result.Win);
        } else if(winner == null) {
            currentNode.backpropagate(Result.Draw);
        } else {
            currentNode.backpropagate(Result.Lose);
        }
    }

}
