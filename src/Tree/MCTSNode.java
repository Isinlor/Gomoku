package Tree;

import Contract.Board;
import Contract.Move;
import Contract.MoveSelector;
import Contract.ReadableBoard;
import Player.MCTSPlayer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public class MCTSNode {
    int depth = 0;
    ReadableBoard state = null;
    ArrayList<MCTSNode> children = new ArrayList<>();
    MCTSNode parent = null;
    double gamesWon = 0;
    double gamesPlayed = 0;
    double score = 0;
    Move lastMove;

    public MCTSNode(ReadableBoard board,Move move){
        state = board;
        lastMove = move;
    }

    public void addChild(MCTSNode child){
        children.add(child);
    }

    public void addParent(MCTSNode parent){
        depth = parent.getDepth()+1;
        this.parent = parent;
    }

    public int getDepth(){return depth;}

    public void update(boolean win){
        gamesPlayed++;
        if(win){
            gamesWon++;
        }
    }

    public double getGamesWon(){return gamesWon;}
    public double getGamesPlayed(){return gamesPlayed;}
    public ArrayList<MCTSNode> getChildren(){return children;}
    public boolean hasChild(){
        return !children.isEmpty();
    }
    public boolean hasParent(){
        return parent != null;
    }

    public void backpropagate(boolean win){
        update(win);
        if(this.hasParent()){
            parent.backpropagate(win);
        }
    }

    public void expandTree(MoveSelector moveSelector){
        Collection<Move> validMoves = moveSelector.getMoves(state);
        for(Move move:validMoves){
            ReadableBoard childBoard = state.getWithMove(move);
            MCTSNode child = new MCTSNode(childBoard,move);
            addChild(child);
            child.addParent(this);
        }
    }

    public MCTSNode traverse(double c){
        if(hasChild()){
            MCTSNode bestChild = children.get(0);
            double highscore = 0;
            for(int i=0;i<children.size();i++){
                MCTSNode child = children.get(i);
                if(child.getGamesPlayed()==0){
                    return child;
                }
                double score = (child.getGamesWon()/child.getGamesPlayed())+c * Math.sqrt(Math.log(gamesPlayed)/child.getGamesPlayed());
                if(score>highscore){
                    highscore=score;
                    bestChild=child;
                }
            }
            return bestChild.traverse(c);
        }else{
            return this;
        }
    }
    public MCTSNode getBestChild(){
        MCTSNode bestChild = children.get(0);
        for(MCTSNode child:children){
            if((child.getGamesWon()/child.getGamesPlayed()) > (bestChild.getGamesWon()/bestChild.getGamesPlayed())){
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
