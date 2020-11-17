package app;

import AI.Strategy;

import java.util.ArrayList;
import java.util.List;

public class Player implements PlayerI{
    public Puyo[][] currentPuyo;
    Board board;
    Chain chain;
    Garbage garbage;
    private int noOfColours;
    private Strategy s;

    public Player(Board b, int noOfColours, Strategy s){
        this.noOfColours = noOfColours;
        currentPuyo = PuyoI.createInitialPuyo(noOfColours);
        board = b;
        chain = new Chain(board);
        garbage = new Garbage(board);
        this.s = s;
    }

    private void getNextPuyo(){
        currentPuyo[0] = currentPuyo[1];
        currentPuyo[1] = currentPuyo[2];
        currentPuyo[2] = PuyoI.create2Puyo(noOfColours);
    }

    public Move turn(){
        Move move = s.makeMove(board, currentPuyo);
        getNextPuyo();
        return move;
    }

    public List<int[]> findRecentlyDropped(Move m){
        int[][] coords = m.getCoord();
        ArrayList<int[]> result = new ArrayList<>();
        if (coords[0][0] == coords[1][0]){
            result.add(new int[]{coords[0][0], board.peekCol(coords[0][0]) - coords[0][1]});
            result.add(new int[]{coords[1][0], board.peekCol(coords[1][0]) - coords[1][1]});
        }
        else{
            result.add(new int[]{coords[0][0], board.peekCol(coords[0][0])});
            result.add(new int[]{coords[1][0], board.peekCol(coords[1][0])});
        }
        return result;
    }
}
