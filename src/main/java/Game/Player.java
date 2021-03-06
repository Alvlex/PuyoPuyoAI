package Game;

import Strategies.Strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player implements PlayerI{
    public Puyo[][] currentPuyo;
    public Board board;
    public Chain chain;
    public Garbage garbage;
    private int noOfColours;
    public Strategy s;
    private Random x;

    public Player(Board b, int noOfColours, Strategy s, int randomSeed){
        x = new Random(randomSeed);
        this.noOfColours = noOfColours;
        currentPuyo = PuyoI.createInitialPuyo(noOfColours, x);
        board = b;
        chain = new Chain(board);
        garbage = new Garbage(board);
        this.s = s;
    }

    private void getNextPuyo(){
        currentPuyo[0] = currentPuyo[1];
        currentPuyo[1] = currentPuyo[2];
        currentPuyo[2] = PuyoI.create2Puyo(noOfColours, x);
    }

    public Move turn(){
        return turn(new Board());
    }

    @Override
    public Move turn(Board oppBoard){
        Move move;
        move = s.makeMove(board, currentPuyo, oppBoard);
        getNextPuyo();
        return move;
    }

    @Override
    public List<Coordinate> findRecentlyDropped(Move m){
        Coordinate[] coords = m.getCoord();
        ArrayList<Coordinate> result = new ArrayList<>();
        result.add(new Coordinate(coords[0].getX(), board.peekCol(coords[0].getX()) - coords[0].getY()));
        result.add(new Coordinate(coords[1].getX(), board.peekCol(coords[1].getX()) - coords[1].getY()));
        return result;
    }
}
