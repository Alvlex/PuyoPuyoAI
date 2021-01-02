package app;

import AI.HumanStrategy;
import AI.RandomStrategy;
import AI.Strategy;
import AI.pms.PMS;

import java.util.ArrayList;
import java.util.List;

public class Player implements PlayerI{
    public Puyo[][] currentPuyo;
    Board board;
    Chain chain;
    Garbage garbage;
    private int noOfColours;
    public Strategy s;

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
        return turn(new Board());
    }

    @Override
    public Move turn(Board oppBoard){
        Move move;
        if (s instanceof PMS)
            move = ((PMS) s).makeMove(board, currentPuyo, oppBoard);
        else
            move = s.makeMove(board, currentPuyo);
        getNextPuyo();
        return move;
    }

    @Override
    public List<Coordinate> findRecentlyDropped(Move m){
        int[][] coords = m.getCoord();
        ArrayList<Coordinate> result = new ArrayList<>();
        result.add(new Coordinate(coords[0][0], board.peekCol(coords[0][0]) - coords[0][1]));
        result.add(new Coordinate(coords[1][0], board.peekCol(coords[1][0]) - coords[1][1]));
        return result;
    }
}
