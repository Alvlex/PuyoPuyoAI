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
    private Strategy s;

    public Player(Board b, int noOfColours, int s, int playerNo, Output output){
        this.noOfColours = noOfColours;
        currentPuyo = PuyoI.createInitialPuyo(noOfColours);
        board = b;
        chain = new Chain(board);
        garbage = new Garbage(board);
        switch(s){
            case 0:
                this.s = new HumanStrategy(output, playerNo);
                break;
            case 1:
                this.s = new RandomStrategy();
                break;
            case 2:
                this.s = new PMS(true);
                break;
        }
    }

    private void getNextPuyo(){
        currentPuyo[0] = currentPuyo[1];
        currentPuyo[1] = currentPuyo[2];
        currentPuyo[2] = PuyoI.create2Puyo(noOfColours);
    }

    public Move turn(){
        return turn(new Board());
    }

    public Move turn(Board oppBoard){
        Move move;
        if (s instanceof PMS)
            move = ((PMS) s).makeMove(board, currentPuyo, oppBoard);
        else
            move = s.makeMove(board, currentPuyo);
        getNextPuyo();
        return move;
    }

    public List<int[]> findRecentlyDropped(Move m){
        int[][] coords = m.getCoord();
        ArrayList<int[]> result = new ArrayList<>();
        result.add(new int[]{coords[0][0], board.peekCol(coords[0][0]) - coords[0][1]});
        result.add(new int[]{coords[1][0], board.peekCol(coords[1][0]) - coords[1][1]});
        return result;
    }
}
