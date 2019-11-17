package Tree;

import Contract.Move;
import Contract.MoveSelector;
import Contract.ReadableBoard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class MCTSNode {
    
    private int depth = 0;
    private ReadableBoard state = null;
    private ArrayList<MCTSNode> children = new ArrayList<>();
    private ArrayList<Move> untriedMoves = new ArrayList<>();
    private MCTSNode parent = null;
    private double gamesWon = 0;
    private double gamesPlayed = 0;
    private double score = 0;
    private Move lastMove;

    public MCTSNode(ReadableBoard board,Move move,MoveSelector moveSelector){
        state = board;
        lastMove = move;
        untriedMoves = new ArrayList<>(moveSelector.getMoves(board));
    }

    private void addChild(MCTSNode child){
        children.add(child);
    }

    private void addParent(MCTSNode parent){
        depth = parent.getDepth()+1;
        this.parent = parent;
    }

    private int getDepth(){
        return depth;
    }

    private void update(boolean win){
        gamesPlayed++;
        if(win){
            gamesWon++;
        }
    }

    public double getGamesWon(){
        return gamesWon;
    }

    public double getGamesPlayed(){
        return gamesPlayed;
    }

    public double getWinRatio() {
        return gamesWon / gamesPlayed;
    }

    public ArrayList<MCTSNode> getChildren() {
        return children;
    }

    private boolean hasParent() {
        return parent != null;
    }

    public void backpropagate(boolean win){
        update(win);
        if(this.hasParent()){
            parent.backpropagate(win);
        }
    }

    public MCTSNode expandTree(MoveSelector moveSelector){
        Move childMove = untriedMoves.remove((int)(Math.random()*untriedMoves.size()));
        ReadableBoard childBoard = state.getWithMove(childMove);
        MCTSNode child = new MCTSNode(childBoard, childMove,moveSelector);
        addChild(child);
        child.addParent(this);
        return child;
    }

    public MCTSNode traverse(double c){
        if(!hasUntriedMoves()){
            MCTSNode bestChild = children.get(0);
            double highscore = 0;
            for (MCTSNode child: children) {
                if (child.getGamesPlayed() == 0) {
                    return child;
                }
                double score = getWinRatio() + c * Math.sqrt(Math.log(gamesPlayed) / child.getGamesPlayed());
                if (score > highscore) {
                    highscore = score;
                    bestChild = child;
                }
            }
            return bestChild.traverse(c);
        }else{
            return this;
        }
    }

    private boolean hasUntriedMoves(){
        return !untriedMoves.isEmpty();
    }

    public MCTSNode getBestChild(){
        MCTSNode bestChild = children.get(0);
        for(MCTSNode child:children){
            if((child.getWinRatio()) > (bestChild.getWinRatio())){
                bestChild = child;
            }
        }
        return bestChild;
    }

    public ReadableBoard getState(){return state;}

    public Move getLastMove(){return lastMove;}

    public String toString(){
        return this.state.toString();
    }

}
