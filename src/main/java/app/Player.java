package app;

import AI.HumanStrategy;
import AI.Strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Player implements PlayerI{
    Puyo[][] currentPuyo;
    Board board;
    private int playerNo;
    private Output output;
    Chain chain;
    Garbage garbage;

    Player(Board b, int playerNo, Output output){
        currentPuyo = Puyo.createInitialPuyo();
        board = b;
        this.playerNo = playerNo;
        this.output = output;
        this.output.updateCurrentPuyo(currentPuyo, playerNo);
        chain = new Chain(board);
        garbage = new Garbage(board);
    }

    private void getNextPuyo(){
        currentPuyo[0] = currentPuyo[1];
        currentPuyo[1] = currentPuyo[2];
        currentPuyo[2] = Puyo.create2Puyo();
    }

    public Move turn(Strategy s){
        Move move = s.makeMove(board, currentPuyo);
        getNextPuyo();
        return move;
    }

    public List<int[]> findRecentlyDropped(Move m){
        int[][] coords = m.getCoord();
        ArrayList<int[]> result = new ArrayList<>();
        if (coords[0][0] == coords[1][0]){
            result.add(new int[]{coords[0][0], board.peekCol(coords[0][0])});
            result.add(new int[]{coords[1][0], board.peekCol(coords[1][0]) - 1});
        }
        else{
            result.add(new int[]{coords[0][0], board.peekCol(coords[0][0])});
            result.add(new int[]{coords[1][0], board.peekCol(coords[1][0])});
        }
        return result;
    }
}
