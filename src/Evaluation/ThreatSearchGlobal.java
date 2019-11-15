package Evaluation;

import Contract.Color;
import Contract.ReadableBoard;

public class ThreatSearchGlobal {

    int threatCountW[];
    int threatCountB[];
    Threat threats[];
    //TODO CHECK THE END OF COLUMN AND ROWS

    //TODO CHECK OPEN SPOTS IN BETWEEN

    public void evaluateRows(ReadableBoard board) {

        for(int i = 0; i<board.getSize(); i++){
            int steps = 1;
            boolean openLeft = false;
            boolean openRight;
            for(int j = 0; j<board.getSize(); j++) {
                if (j + 1 < board.getSize()) {
                    {
                        if (board.getCell(i, j).getColor().equals(board.getCell(i, j + 1)) && !board.getCell(i, j).getColor().equals(null)) {
                            steps++;
                        }
                        else if (board.getCell(i, j).getColor().equals(null)) {
                            openRight = true;
                            evalThreat(steps, openLeft, openRight, board.getCell(i, j).getColor());
                            steps = 1;
                            openLeft = true;
                        }
                        else if (!board.getCell(i, j).getColor().equals(board.getCell(i, j + 1)) && !board.getCell(i, j + 1).getColor().equals(null)) {
                            openRight = false;
                            evalThreat(steps, openLeft, openRight, board.getCell(i, j).getColor());
                            steps = 1;
                        }
                        else if (board.getCell(i, j).getColor().equals(null)) {

                        }
                    }
                }
            }
        }
    }
    public void evaluateColumns(ReadableBoard board){
        for(int i = 0; i<board.getSize(); i++){
            int steps = 1;
            boolean openTop = false;
            boolean openBottom;
            for(int j = 0; j<board.getSize(); j++) {
                if (j + 1 < board.getSize()) {
                    {
                        if (board.getCell(j, i).getColor().equals(board.getCell(j+1, i)) && !board.getCell(j, i).getColor().equals(null)) {
                            steps++;
                        }
                        else if (board.getCell(j, i).getColor().equals(null)) {
                            openBottom = true;
                            evalThreat(steps, openTop, openBottom, board.getCell(j, i).getColor());
                            steps = 1;
                            openTop = true;
                        }
                        else if (!board.getCell(j, i).getColor().equals(board.getCell(j+1, i)) && !board.getCell(j+1, i).getColor().equals(null)) {
                            openBottom = false;
                            evalThreat(steps, openTop, openBottom, board.getCell(j, i).getColor());
                            steps = 1;
                        }
                        else if (board.getCell(j, i).getColor().equals(null)) {

                        }
                    }
                }
            }
        }
    }

   public void evaluateBackDiagonal(ReadableBoard board) {
       for (int i = board.getSize() - 5; i >= 0; i--) {
           int steps = 1;
           boolean openTop = false;
           boolean openBottom;
           int k = i;
           while (i > 0) {
               if (k < board.getSize() - 1) {
                   for (int j = 0; j < board.getSize() - 1; j++) {
                       if (board.getCell(k, j).getColor().equals(board.getCell(k + 1, j + 1)) && !board.getCell(k, j).getColor().equals(null)) {
                           steps++;

                       } else if (board.getCell(k, j).getColor().equals(null)) {
                           openBottom = true;
                           evalThreat(steps, openTop, openBottom, board.getCell(k, j).getColor());
                           steps = 1;
                           openTop = true;

                       } else if (!board.getCell(k, j).getColor().equals(board.getCell(k + 1, j + 1)) && !board.getCell(k + 1, j).getColor().equals(null)) {
                           openBottom = false;
                           evalThreat(steps, openTop, openBottom, board.getCell(j, i).getColor());
                           steps = 1;


                       }
                       k++;}

               }
           }
           if (i == 0) {
               for (int j = 0; j < board.getSize() - 5; j++) {
                   if (board.getCell(k, j).getColor().equals(board.getCell(k + 1, j + 1)) && !board.getCell(k, j).getColor().equals(null)) {
                       steps++;

                   } else if (board.getCell(k, j).getColor().equals(null)) {
                       openBottom = true;
                       evalThreat(steps, openTop, openBottom, board.getCell(k, j).getColor());
                       steps = 1;
                       openTop = true;
                   }
                   else if (!board.getCell(k, j).getColor().equals(board.getCell(k + 1, j + 1)) && !board.getCell(k + 1, j).getColor().equals(null)) {
                       openBottom = false;
                       evalThreat(steps, openTop, openBottom, board.getCell(j, i).getColor());
                       steps = 1;
                   }
                   k++;  }
           }
       }
   }

   public void evaluateForDiagonal (ReadableBoard board){
       for (int i = 4; i < board.getSize();  i++) {
           int steps = 1;
           boolean openTop = false;
           boolean openBottom;
           int k = i;
           while (i < 14) {
               if (k < board.getSize() - 1) {
                   for (int j = 0; j < board.getSize() - 1; j++) {
                       if (board.getCell(k, j).getColor().equals(board.getCell(k - 1, j + 1)) && !board.getCell(k, j).getColor().equals(null)) {
                           steps++;

                       } else if (board.getCell(k, j).getColor().equals(null)) {
                           openBottom = true;
                           evalThreat(steps, openTop, openBottom, board.getCell(k, j).getColor());
                           steps = 1;
                           openTop = true;

                       } else if (!board.getCell(k, j).getColor().equals(board.getCell(k + 1, j + 1)) && !board.getCell(k + 1, j).getColor().equals(null)) {
                           openBottom = false;
                           evalThreat(steps, openTop, openBottom, board.getCell(k, j).getColor());
                           steps = 1;


                       }
                       k--;}

               }
           }
           if (i == 14) {
               for (int j = 0; j < board.getSize() - 5; j++) {
                   if (board.getCell(k, j).getColor().equals(board.getCell(k - 1, j + 1)) && !board.getCell(k, j).getColor().equals(null)) {
                       steps++;

                   } else if (board.getCell(k, j).getColor().equals(null)) {
                       openBottom = true;
                       evalThreat(steps, openTop, openBottom, board.getCell(k, j).getColor());
                       steps = 1;
                       openTop = true;
                   }
                   else if (!board.getCell(k, j).getColor().equals(board.getCell(k - 1, j + 1)) && !board.getCell(k + 1, j).getColor().equals(null)) {
                       openBottom = false;
                       evalThreat(steps, openTop, openBottom, board.getCell(j, i).getColor());
                       steps = 1;
                   }
                   k--; }
           }
       }
   }

    public void evalThreat(int steps, boolean openBefore, boolean openAfter, Color color){
        if(color.equals(Color.Black)){
            if((steps==2&&openAfter&&!openBefore) || (steps==2&&!openAfter&&openBefore)){   //H2
                threatCountB[0]++;
            }
            else if((steps==3&&openAfter&&!openBefore) || (steps==3&&!openAfter&&openBefore)){  //H3
                threatCountB[1]++;
            }
            else if(steps==2&&openAfter&&openBefore){  //O2
                threatCountB[2]++;
            }
            else if((steps==4&&openAfter&&!openBefore) || (steps==4&&!openAfter&&openBefore)){  //H4
                threatCountB[3]++;
            }
            else if(steps==3&&openAfter&&openBefore){  //O3
                threatCountB[4]++;
            }
            else if(steps==4&&openAfter&&!openBefore) {  //O4
                threatCountB[5]++;
            }
        }if (color.equals(Color.White)){
            if((steps==2&&openAfter&&!openBefore) || (steps==2&&!openAfter&&openBefore)){   //H2
                threatCountW[0]++;
            }
            else if((steps==3&&openAfter&&!openBefore) || (steps==3&&!openAfter&&openBefore)){  //H3
                threatCountW[1]++;
            }
            else if(steps==2&&openAfter&&openBefore){  //O2
                threatCountW[2]++;
            }
            else if((steps==4&&openAfter&&!openBefore) || (steps==4&&!openAfter&&openBefore)){  //H4
                threatCountW[3]++;
            }
            else if(steps==3&&openAfter&&openBefore){  //O3
                threatCountW[4]++;
            }
            else if(steps==4&&openAfter&&!openBefore) {  //O4
                threatCountW[5]++;
            }
        }
    }
}
