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

        for (int i = 0; i < board.getSize(); i++) {
            int steps = 1;
            boolean openLeft = false;
            boolean openRight = true;
            BoardCell color = BoardCell.Empty;
            for (int j = 0; j < board.getSize(); j++) {

                if (j == board.getSize() - 1) {
                    openRight = false;

                    evalThreat(steps, openLeft, openRight, board.getCell(i, j));
                    steps = 1;
                } else if (board.getCell(i, j).equals(BoardCell.Empty)) {
                    openLeft = true;
                    steps = 1;
                } else if (board.getCell(i, j).equals(board.getCell(i, j + 1)) && !board.getCell(i, j).equals(BoardCell.Empty)) {
                    steps++;
                    color = board.getCell(i, j);
                    System.out.println(color);
                    System.out.println(steps);
                } else if (board.getCell(i, j + 1).equals(BoardCell.Empty)) {
                    openRight = true;
                    System.out.println(steps);
                    evalThreat(steps, openLeft, openRight, board.getCell(i, j));
                    steps = 1;
                } else if (!board.getCell(i, j).equals(board.getCell(i, j + 1)) && !board.getCell(i, j + 1).equals(BoardCell.Empty)) {
                    openRight = false;
                    evalThreat(steps, openLeft, openRight, board.getCell(i, j));
                    steps = 1;

                }


            }
        }
    }

    private void evaluateColumns(ReadableBoard board) {
        for (int i = 0; i < board.getSize(); i++) {
            int steps = 1;
            boolean openTop = false;
            BoardCell color = BoardCell.Empty;
            boolean openBottom;
            for (int j = 0; j < board.getSize(); j++) {

                {
                    if (j == board.getSize() - 1) {
                        openBottom = false;
                        evalThreat(steps, openTop, openBottom, board.getCell(j, i));
                        steps = 1;
                    }
                    // System.out.println("");
                    // System.out.println(board.getCell(j, i));
                    // System.out.println(board.getCell(j+1, i));
                    else if (board.getCell(j + 1, i).equals(BoardCell.Empty) && steps > 1) {
                        openBottom = true;
                        evalThreat(steps, openTop, openBottom, color);
                        steps = 1;
                        openTop = true;
                    } else if (board.getCell(j, i).equals(BoardCell.Empty)) {
                        openTop = true;
                        steps = 1;
                    } else if (board.getCell(j, i).equals(board.getCell(j + 1, i)) && !board.getCell(j, i).equals(BoardCell.Empty)) {
                        steps++;
                        color = board.getCell(j, i);
                        //   System.out.println("steps");
                        //   System.out.println(steps);

                    } else if (!board.getCell(j, i).equals(board.getCell(j + 1, i)) && !board.getCell(j + 1, i).equals(BoardCell.Empty)) {
                        openBottom = false;
                        evalThreat(steps, openTop, openBottom, board.getCell(j, i));
                        steps = 1;
                        openTop =false;
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
            BoardCell color = BoardCell.Empty;
            for (int j = 0; j < board.getSize() - i; j++) {
                if (i + j  < board.getSize() - 1) {
//                    if (j == board.getSize() - 1) {
//                        openBottom = false;
//                        evalThreat(steps, openTop, openBottom, board.getCell(i + j, j));
//                        steps = 1;
//                   } else
                        if (board.getCell(i + j, j).equals(BoardCell.Empty)) {
                            openBottom=true;
                        if(steps>1) evalThreat(steps,openTop,openBottom,board.getCell(i+j-1, j-1));
                        openTop = true;

                    } else if (board.getCell(i + j, j).equals(board.getCell(i + j + 1, j + 1)) && !board.getCell(i + j, j).equals(BoardCell.Empty)) {
                        steps++;
                        color = board.getCell(i + j, j);
                        System.out.println(steps + "LOOOK HERE ");

                    } else if (board.getCell(i + j + 1, j + 1).equals(BoardCell.Empty) && steps > 1) {
                        openBottom = true;
                        evalThreat(steps, openTop, openBottom, board.getCell(i + j, j));
                        steps = 1;
                        openTop = true;


                    } else if (!board.getCell(i + j, j).equals(board.getCell(i + j + 1, j + 1)) && !board.getCell(i + j + 1, j + 1).equals(BoardCell.Empty)) {
                        openBottom = false;
                        evalThreat(steps, openTop, openBottom, board.getCell(i + j, j));
                        steps = 1;
                    }
                }else{
                    if (board.getCell(i + j, j).equals(BoardCell.Empty) && steps > 1) {
                        openBottom = true;
                        evalThreat(steps, openTop, openBottom, board.getCell(i + j, j));
                        steps = 1;
                        openTop = true;
                    }else if (board.getCell(i + j, j).equals(board.getCell(i + j - 1, j - 1)) && !board.getCell(i + j, j).equals(BoardCell.Empty)) {
                       evalThreat(steps, openTop, false, board.getCell(i+j,j));

                    }
                    else if (!board.getCell(i + j, j).equals(board.getCell(i + j - 1, j - 1)) && !board.getCell(i + j, j).equals(BoardCell.Empty)) {
                        evalThreat(steps, openTop, false, board.getCell(i + j - 1, j - 1));
                }}
            }
        }
        for (int i = board.getSize() - 5; i > 0; i--) {
            int steps = 1;
            boolean openTop = false;
            boolean openBottom;
            BoardCell color = BoardCell.Empty;
//            int k = i;

            for (int j = 0; j < board.getSize() - i; j++) {
                if (i + j  < board.getSize() - 1) {
//                    if (j == board.getSize() - 1) {
//                        openBottom = false;
//                        evalThreat(steps, openTop, openBottom, board.getCell(i + j, j));
//                        steps = 1;
//                   } else
                    if (board.getCell(j, i+j).equals(BoardCell.Empty)) {
                        openBottom=true;
                        if(steps>1) evalThreat(steps,openTop,openBottom,board.getCell(j-1, j-1+i));
                        openTop = true;

                    } else if (board.getCell(j,i+ j).equals(board.getCell( j + 1, i+j + 1)) && !board.getCell( j,i+ j).equals(BoardCell.Empty)) {
                        steps++;
                        color = board.getCell(j,i+ j);
                        System.out.println(steps + "LOOOK HERE ");

                    } else if (board.getCell( j + 1, i + j + 1).equals(BoardCell.Empty) && steps > 1) {
                        openBottom = true;
                        evalThreat(steps, openTop, openBottom, board.getCell(j,i + j));
                        steps = 1;
                        openTop = true;


                    } else if (!board.getCell(j,i+ j).equals(board.getCell( j + 1, i +j + 1)) && !board.getCell( j + 1, i+j + 1).equals(BoardCell.Empty)) {
                        openBottom = false;
                        evalThreat(steps, openTop, openBottom, board.getCell( j, i+ j));
                        steps = 1;
                    }
                }else{
                    if (board.getCell(j, i + j).equals(BoardCell.Empty) && steps > 1) {
                        openBottom = true;
                        evalThreat(steps, openTop, openBottom, board.getCell(j, i + j));
                        steps = 1;
                        openTop = true;
                    }else if (board.getCell(j, i + j).equals(board.getCell( j - 1, i +j - 1)) && !board.getCell( j, i+ j).equals(BoardCell.Empty)) {
                        evalThreat(steps, openTop, false, board.getCell(j,i + j));

                    }
                    else if (!board.getCell( j, i +j).equals(board.getCell( j - 1, i + j - 1)) && !board.getCell( j, i+ j).equals(BoardCell.Empty)) {
                        evalThreat(steps, openTop, false, board.getCell( j - 1, i +j - 1));
                    }
                }
            }
        }
    }


    private void evaluateForDiagonal(ReadableBoard board) {
        for (int j = 4; j < board.getSize(); j++) {
            int steps = 1;
            boolean openTop = false;
            boolean openBottom;
            BoardCell color = BoardCell.Empty;
            for (int i = 0; i <= j; i++) {
                if (j-i  > 0) {
                    if (board.getCell(i , j - i).equals(BoardCell.Empty)) {
                        openBottom=true;
                        if(steps>1) evalThreat(steps,openTop,openBottom,board.getCell(i+1, j-1-i));
                        openTop = true;

                    } else if (board.getCell(i, j-i).equals(board.getCell(i + 1, j -i - 1)) && !board.getCell(i, j-i).equals(BoardCell.Empty)) {
                        steps++;
                        color = board.getCell(i, j-i);
                        System.out.println(steps + "LOOOK HERE ");

                    } else if (board.getCell(i+ 1, j -i - 1).equals(BoardCell.Empty) && steps > 1) {
                        openBottom = true;
                        evalThreat(steps, openTop, openBottom, board.getCell(i , j-i));
                        steps = 1;
                        openTop = true;


                    } else if (!board.getCell(i, j-i).equals(board.getCell(i + 1, j -i - 1)) && !board.getCell(i + 1, j -i -1).equals(BoardCell.Empty)) {
                        openBottom = false;
                        evalThreat(steps, openTop, openBottom, board.getCell(i , j-i));
                        steps = 1;
                    }
                }else{
                    if (board.getCell(i , j-i).equals(BoardCell.Empty) && steps > 1) {
                        openBottom = true;
                        evalThreat(steps, openTop, openBottom, board.getCell(i , j-i));
                        steps = 1;
                        openTop = true;
                    }else if (board.getCell(i, j-i).equals(board.getCell(i -1, j - i +1)) && !board.getCell(i , j-i ).equals(BoardCell.Empty)) {
                        evalThreat(steps, openTop, false, board.getCell(i,j-i ));

                    }
                    else if (!board.getCell(i , j- i).equals(board.getCell(i  - 1, j -i + 1)) && !board.getCell(i, j-i).equals(BoardCell.Empty)) {
                        evalThreat(steps, openTop, false, board.getCell(i  - 1, j -i + 1));
                    }}
            }
        }
        for (int j = 1; j < board.getSize()-5; j++) {
            int steps = 1;
            boolean openTop = false;
            boolean openBottom;
            BoardCell color = BoardCell.Empty;
            for (int i = board.getSize()-1; i >= j; i--) {
                if (i-j  > 0) {
                    if (board.getCell(j , i-j).equals(BoardCell.Empty)) {
                        if(steps>1) evalThreat(steps,openTop,true,board.getCell(j+1, i-j-1));
                        openTop = true;
                    } else if (board.getCell(j, i-j).equals(board.getCell(j + 1, i-j - 1)) && !board.getCell(j, i-j).equals(BoardCell.Empty)) {
                        steps++;
                        color = board.getCell(j, i-j);
                        System.out.println(steps + "LOOOK HERE ");

                    } else if (board.getCell(j+ 1, i-j - 1).equals(BoardCell.Empty) && steps > 1) {
                        openBottom = true;
                        evalThreat(steps, openTop, openBottom, board.getCell(j , i-j));
                        steps = 1;
                        openTop = true;


                    } else if (!board.getCell(j, i-j).equals(board.getCell(j + 1, i-j - 1)) && !board.getCell(j + 1, i-j -1).equals(BoardCell.Empty)) {
                        openBottom = false;
                        evalThreat(steps, openTop, openBottom, board.getCell(j , i-j));
                        steps = 1;
                        openTop= false;
                    }
                }else{
                    if (board.getCell(j , i-j).equals(BoardCell.Empty) && steps > 1) {
                        openBottom = true;
                        evalThreat(steps, openTop, openBottom, board.getCell(j , i-j));
                        steps = 1;
                        openTop = true;
                    }else if (board.getCell(j, i-j).equals(board.getCell(j -1,  i- j +1)) && !board.getCell(j , i-j ).equals(BoardCell.Empty)) {
                        evalThreat(steps, openTop, false, board.getCell(j,i-j ));

                    }
                    else if (!board.getCell(j ,  i-j).equals(board.getCell(j  - 1, i-j + 1)) && !board.getCell(j, i-j).equals(BoardCell.Empty)) {
                        evalThreat(steps, openTop, false, board.getCell(j- 1, i-j + 1));
                    }}
            }
        }
    }


    private void evalThreat(int steps, boolean openBefore, boolean openAfter, BoardCell color) {
        Threat threat = null;
        System.out.println(openBefore);
        System.out.println(openAfter);
        if (!openAfter && !openBefore) {}
        else if ((steps == 2 && openAfter && !openBefore) || (steps == 2 && !openAfter && openBefore)) {   //H2
            threat = Threat.HalfClosed2;
        } else if ((steps == 3 && openAfter && !openBefore) || (steps == 3 && !openAfter && openBefore)) {  //H3
            threat = Threat.HalfClosed3;
        } else if (steps == 2 && openAfter && openBefore) {  //O2
            threat = Threat.Open2;
        } else if ((steps == 4 && openAfter && !openBefore) || (steps == 4 && !openAfter && openBefore)) {  //H4
            threat = Threat.HalfClosed4;
        } else if (steps == 3 && openAfter && openBefore) {  //O3
            threat = Threat.Open3;
        } else if (steps == 4 && openAfter && openBefore) {  //O4
            threat = Threat.Open4;
        }


        System.out.println(threat + "+" + color);
        if (threat == null) {
        } else {
            if (color.equals(BoardCell.Black)) {
                threatsBlack.add(threat);
            } else if (color.equals(BoardCell.White)) {
                threatsWhite.add(threat);
            }
        }
    }


    private double threatValue(Threat threat) {
        if (threat == null) System.out.println("I am fucked up here!");
        else {
            switch (threat) {
                case HalfClosed2:
                    return 1;
                case HalfClosed3:
                    return 2.25;
                case HalfClosed4:
                    return 4;
                case Open2:
                    return 4;
                case Open3:
                    return 9;
                case Open4:
                    return 16;
                case All5:
                    return 25;
            }
        }
        return 0.0;
    }

    public double evaluate(ReadableBoard board) {
        threatsBlack = new ArrayList<Threat>();
        threatsWhite = new ArrayList<Threat>();
        double threatsBlackValue = 0;
        double threatsWhiteValue = 0;

      //  evaluateColumns(board);
      //  evaluateRows(board);
        evaluateForDiagonal(board);
     //   evaluateBackDiagonal(board);
        System.out.println("Threats for black" + threatsBlack);
        System.out.println("Threats for white" + threatsWhite);
        if (!threatsBlack.isEmpty())
            threatsBlackValue = threatsBlack.stream().map(t -> threatValue(t)).reduce(0.0, (a, b) -> a + b);

        if (!threatsWhite.isEmpty())
            threatsWhiteValue = threatsWhite.stream().map(t -> threatValue(t)).reduce(0.0, (a, b) -> a + b);
        System.out.println(threatsBlackValue - threatsWhiteValue);
        return threatsBlackValue - threatsWhiteValue;
    }
}
