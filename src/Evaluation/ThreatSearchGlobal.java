package Evaluation;

import Contract.BoardCell;
import Contract.Color;
import Contract.ReadableBoard;

import java.util.ArrayList;

public class ThreatSearchGlobal {

    ArrayList<Threat> threatsWhite;
    ArrayList<Threat> threatsBlack;
    //TODO CHECK THE END OF COLUMN AND ROWS

    //TODO CHECK OPEN SPOTS IN BETWEEN

    private void evaluateRows(ReadableBoard board) {

        for(int i = 0; i<board.getSize(); i++){
            int steps = 1;
            boolean openLeft = false;
            boolean openRight;
            for(int j = 0; j<board.getSize(); j++) {
                if (j + 1 < board.getSize()) {
                    {
                        if (board.getCell(i, j).equals(board.getCell(i, j + 1)) && !board.getCell(i, j).equals(BoardCell.Empty)) {
                            steps++;
                        }
                        else if (board.getCell(i, j).equals(BoardCell.Empty)) {
                            openRight = true;
                            evalThreat(steps, openLeft, openRight, board.getCell(i, j));
                            steps = 1;
                            openLeft = true;
                        }
                        else if (!board.getCell(i, j).equals(board.getCell(i, j + 1)) && !board.getCell(i, j + 1).equals(BoardCell.Empty)) {
                            openRight = false;
                            evalThreat(steps, openLeft, openRight, board.getCell(i, j));
                            steps = 1;
                        }
                        else if (board.getCell(i, j).equals(BoardCell.Empty)) {

                        }
                    }
                }
            }
        }
    }
    private void evaluateColumns(ReadableBoard board){
        for(int i = 0; i<board.getSize(); i++){
            int steps = 1;
            boolean openTop = false;
            BoardCell color = null;
            boolean openBottom;
            for(int j = 0; j<board.getSize(); j++) {
                if (j + 1 < board.getSize()) {
                    {
                        System.out.println("");
                        System.out.println(board.getCell(j, i));
                        System.out.println(board.getCell(j+1, i));

                        if (board.getCell(j, i).equals(board.getCell(j+1, i)) && !board.getCell(j, i).equals(BoardCell.Empty)) {
                            steps++;
                            color = board.getCell(j, i);
                            System.out.println("steps");
                            System.out.println(steps);

                        }
                        else if (board.getCell(j, i).equals(BoardCell.Empty)) {
                            openBottom = true;
                            evalThreat(steps, openTop, openBottom, color);
                            steps = 1;
                            openTop = true;
                        }
                        else if (!board.getCell(j, i).equals(board.getCell(j+1, i)) && !board.getCell(j+1, i).equals(BoardCell.Empty)) {
                            openBottom = false;
                            evalThreat(steps, openTop, openBottom, board.getCell(j, i));
                            steps = 1;
                        }
                        else if (board.getCell(j, i).equals(BoardCell.Empty)) {

                        }
                    }
                }
            }
        }
    }

    private void evaluateBackDiagonal(ReadableBoard board) {
       for (int i = board.getSize() - 5; i >= 0; i--) {
           int steps = 1;
           boolean openTop = false;
           boolean openBottom;
           int k = i;
           while (i > 0) {
               if (k < board.getSize() - 1) {
                   for (int j = 0; j < board.getSize() - 1; j++) {
                       if (board.getCell(k, j).getColor().equals(board.getCell(k + 1, j + 1)) && !board.getCell(k, j).equals(null)) {
                           steps++;

                       } else if (board.getCell(k, j).getColor().equals(null)) {
                           openBottom = true;
                           evalThreat(steps, openTop, openBottom, board.getCell(k, j));
                           steps = 1;
                           openTop = true;

                       } else if (!board.getCell(k, j).getColor().equals(board.getCell(k + 1, j + 1)) && !board.getCell(k + 1, j).equals(null)) {
                           openBottom = false;
                           evalThreat(steps, openTop, openBottom, board.getCell(j, i));
                           steps = 1;


                       }
                       k++;}

               }
           }
           if (i == 0) {
               for (int j = 0; j < board.getSize() - 5; j++) {
                   if (board.getCell(k, j).getColor().equals(board.getCell(k + 1, j + 1)) && !board.getCell(k, j).equals(BoardCell.Empty)) {
                       steps++;

                   } else if (board.getCell(k, j).equals(BoardCell.Empty)) {
                       openBottom = true;
                       evalThreat(steps, openTop, openBottom, board.getCell(k, j));
                       steps = 1;
                       openTop = true;
                   }
                   else if (!board.getCell(k, j).getColor().equals(board.getCell(k + 1, j + 1)) && !board.getCell(k + 1, j).equals(BoardCell.Empty)) {
                       openBottom = false;
                       evalThreat(steps, openTop, openBottom, board.getCell(j, i));
                       steps = 1;
                   }
                   k++;  }
           }
       }
   }

    private void evaluateForDiagonal (ReadableBoard board){
       for (int i = 4; i < board.getSize();  i++) {
           int steps = 1;
           boolean openTop = false;
           boolean openBottom;
           int k = i;
           while (i < 14) {
               if (k < board.getSize() - 1) {
                   for (int j = 0; j < board.getSize() - 1; j++) {
                       if (board.getCell(k, j).equals(board.getCell(k - 1, j + 1)) && !board.getCell(k, j).equals(BoardCell.Empty)) {
                           steps++;

                       } else if (board.getCell(k, j).equals(BoardCell.Empty)) {
                           openBottom = true;
                           evalThreat(steps, openTop, openBottom, board.getCell(k, j));
                           steps = 1;
                           openTop = true;

                       } else if (!board.getCell(k, j).equals(board.getCell(k + 1, j + 1)) && !board.getCell(k + 1, j).equals(BoardCell.Empty)) {
                           openBottom = false;
                           evalThreat(steps, openTop, openBottom, board.getCell(k, j));
                           steps = 1;


                       }
                       k--;}

               }
           }
           if (i == 14) {
               for (int j = 0; j < board.getSize() - 5; j++) {
                   if (board.getCell(k, j).equals(board.getCell(k - 1, j + 1)) && !board.getCell(k, j).equals(BoardCell.Empty)) {
                       steps++;

                   } else if (board.getCell(k, j).getColor().equals(null)) {
                       openBottom = true;
                       evalThreat(steps, openTop, openBottom, board.getCell(k, j));
                       steps = 1;
                       openTop = true;
                   }
                   else if (!board.getCell(k, j).equals(board.getCell(k - 1, j + 1)) && !board.getCell(k + 1, j).equals(BoardCell.Empty)) {
                       openBottom = false;
                       evalThreat(steps, openTop, openBottom, board.getCell(j, i));
                       steps = 1;
                   }
                   k--; }
           }
       }
   }

    private void evalThreat(int steps, boolean openBefore, boolean openAfter, BoardCell color){
        Threat threat = null;
        if((steps==2&&openAfter&&!openBefore) || (steps==2&&!openAfter&&openBefore)){   //H2
            threat=Threat.HalfClosed2;
        }
        else if((steps==3&&openAfter&&!openBefore) || (steps==3&&!openAfter&&openBefore)){  //H3
            threat=Threat.HalfClosed3;
        }
        else if(steps==2&&openAfter&&openBefore){  //O2
            threat=Threat.Open2;
        }
        else if((steps==4&&openAfter&&!openBefore) || (steps==4&&!openAfter&&openBefore)){  //H4
            threat=Threat.HalfClosed4;
        }
        else if(steps==3&&openAfter&&openBefore){  //O3
            threat=Threat.Open3;
        }
        else if(steps==4&&openAfter&&!openBefore) {  //O4
            threat=Threat.Open4;
        }

        System.out.println("threat evaluation");
        System.out.println(threat);

        System.out.println("color");
        System.out.println(color);


        if(color.equals(BoardCell.Black)){
            threatsBlack.add(threat);
        }
        else if(color.equals(BoardCell.White)){
            threatsWhite.add(threat);
        }
    }

    private double threatValue(Threat threat) {
        switch (threat) {
            case HalfClosed2:
                return 1;
            case HalfClosed3:
                return 2.25;
            case HalfClosed4:
            case Open2:
                return 4;
            case Open3:
                return 9;
            case Open4:
                return 16;
        }
        return 0.0;
    }

    public double evaluate(ReadableBoard board){
        threatsBlack = new ArrayList<Threat>();
        threatsWhite = new ArrayList<Threat>();

        evaluateColumns(board);
        System.out.println(threatsBlack);
        System.out.println(threatsWhite);

//        evaluateRows(board);
//        evaluateForDiagonal(board);
//        evaluateBackDiagonal(board);

        double threatsBlackValue = threatsBlack.stream().map(t -> threatValue(t)).reduce(0.0, (a,b) -> a + b);
        double threatsWhiteValue = threatsWhite.stream().map(t -> threatValue(t)).reduce(0.0, (a,b) -> a + b);

        return threatsBlackValue - threatsWhiteValue;
    }
}
