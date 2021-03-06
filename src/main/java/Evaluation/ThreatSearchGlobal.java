package Evaluation;


import Contract.BoardCell;
import Contract.Color;
import Contract.Evaluation;
import Contract.ReadableBoard;
import UI.Logger;

import java.util.ArrayList;

public class ThreatSearchGlobal implements Evaluation {

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
                    Logger.log(color);
                    Logger.log(steps);
                } else if (board.getCell(i, j + 1).equals(BoardCell.Empty)) {
                    openRight = true;
                    Logger.log(steps);
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
                    // Logger.log();("");
                    // Logger.log();(board.getCell(j, i));
                    // Logger.log();(board.getCell(j+1, i));
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
                        //   Logger.log();("steps");
                        //   Logger.log();(steps);

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
                            Logger.log(steps + "LOOOK HERE ");

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
                    }
                }
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
                        Logger.log(steps + "LOOOK HERE ");

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
                        if(steps>1) {
                            evalThreat(steps,openTop,openBottom,board.getCell(i+1, j-1-i));
                            steps = 1;
                        }
                        openTop = true;

                    } else if (board.getCell(i, j-i).equals(board.getCell(i + 1, j -i - 1)) && !board.getCell(i, j-i).equals(BoardCell.Empty)) {
                        steps++;
                        color = board.getCell(i, j-i);
                        Logger.log(steps + "LOOOK HERE ");

                    } else if (board.getCell(i+ 1, j -i - 1).equals(BoardCell.Empty) && steps > 1) {
                        openBottom = true;
                        evalThreat(steps, openTop, openBottom, board.getCell(i , j-i));
                        steps = 1;
                        openTop = true;


                    } else if (!board.getCell(i, j-i).equals(board.getCell(i + 1, j -i - 1)) && !board.getCell(i + 1, j -i -1).equals(BoardCell.Empty)) {
                        openBottom = false;
                        evalThreat(steps, openTop, openBottom, board.getCell(i , j-i));
                        steps = 1;
                        openTop = false;
                    }
                }else{
                    if (board.getCell(i , j-i).equals(BoardCell.Empty) && steps > 1) {
                        openBottom = true;
                        evalThreat(steps, openTop, openBottom, board.getCell(i -1, j - i +1));
                        steps = 1;
                        openTop = true;
                    }else if (board.getCell(i, j-i).equals(board.getCell(i -1, j - i +1)) && !board.getCell(i , j-i ).equals(BoardCell.Empty)) {
                        evalThreat(steps, openTop, false, board.getCell(i,j-i ));

                    }
                    else if (!board.getCell(i , j- i).equals(board.getCell(i  - 1, j -i + 1)) && !board.getCell(i, j-i).equals(BoardCell.Empty)) {
                        evalThreat(steps, openTop, false, board.getCell(i  - 1, j -i + 1));
                    }
                }
            }
        }
        //TODO fix case for final diagonal
        if (board.getSize()>5) {
            for (int j = 1; j <= board.getSize() - 4; j++) {
                int steps = 1;
                boolean openTop = false;
                boolean openBottom;
                BoardCell color = BoardCell.Empty;
                for (int i = board.getSize() - 1; i > board.getSize() - 5; i--) {
                    if (i - j > 0) {
                        if (board.getCell(j, i - j).equals(BoardCell.Empty)) {
                            openBottom = true;
                            if (steps > 1) {
                                evalThreat(steps, openTop, true, board.getCell(j + 1, i - j - 1));
                                steps = 1;
                            }
                            openTop = true;
                        } else if (board.getCell(j, i - j).equals(board.getCell(j + 1, i - j - 1)) && !board.getCell(j, i - j).equals(BoardCell.Empty)) {
                            steps++;
                            color = board.getCell(j, i - j);
                            Logger.log(steps + "LOOOK HERE ");

                        } else if (board.getCell(j + 1, i - j - 1).equals(BoardCell.Empty) && steps > 1) {
                            openBottom = true;
                            evalThreat(steps, openTop, openBottom, board.getCell(j, i - j));
                            steps = 1;
                            openTop = true;


                        } else if (!board.getCell(j, i - j).equals(board.getCell(j + 1, i - j - 1)) && !board.getCell(j + 1, i - j - 1).equals(BoardCell.Empty)) {
                            openBottom = false;
                            evalThreat(steps, openTop, openBottom, board.getCell(j, i - j));
                            steps = 1;
                            openTop = false;
                        }
                    } else {
                        if (board.getCell(j, i - j).equals(BoardCell.Empty) && steps > 1) {
                            openBottom = true;
                            evalThreat(steps, openTop, openBottom, board.getCell(j - 1, i - j + 1));
                            steps = 1;
                            openTop = true;
                        } else if (board.getCell(j, i - j).equals(board.getCell(j - 1, i - j + 1)) && !board.getCell(j, i - j).equals(BoardCell.Empty)) {
                            evalThreat(steps, openTop, false, board.getCell(j, i - j));

                        } else if (!board.getCell(j, i - j).equals(board.getCell(j - 1, i - j + 1)) && !board.getCell(j, i - j).equals(BoardCell.Empty)) {
                            evalThreat(steps, openTop, false, board.getCell(j - 1, i - j + 1));
                        }
                    }
                }
            }
        }
    }


    private void evalThreat(int steps, boolean openBefore, boolean openAfter, BoardCell color) {
        Threat threat = null;
        Logger.log(openBefore);
        Logger.log(openAfter);
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
        else if (steps == 5) {
            threat = Threat.All5;
        }


        Logger.log(threat + "+" + color);
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
        if (threat == null) Logger.log("I am fucked up here!");
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
                    return 160;
                case All5:
                    return 250;
            }
        }
        return 0.0;
    }

    public double evaluate(ReadableBoard board) {
        threatsBlack = new ArrayList<Threat>();
        threatsWhite = new ArrayList<Threat>();
        double threatsBlackValue = 0;
        double threatsWhiteValue = 0;

        evaluateColumns(board);
        evaluateRows(board);
        evaluateForDiagonal(board);
        evaluateBackDiagonal(board);

        Logger.log("Threats for black" + threatsBlack);
        Logger.log("Threats for white" + threatsWhite);
        if (!threatsBlack.isEmpty())
            threatsBlackValue = threatsBlack.stream().map(t -> threatValue(t)).reduce(0.0, (a, b) -> a + b);

        if (!threatsWhite.isEmpty())
            threatsWhiteValue = threatsWhite.stream().map(t -> threatValue(t)).reduce(0.0, (a, b) -> a + b);
        Logger.log(threatsBlackValue - threatsWhiteValue);

        switch (board.getCurrentColor()) {
            case Black:
                return threatsBlackValue - threatsWhiteValue;
            case White:
                return threatsWhiteValue - threatsBlackValue;
            default:
                throw new RuntimeException("Unexpected color: " + board.getCurrentColor());
        }
    }
}
