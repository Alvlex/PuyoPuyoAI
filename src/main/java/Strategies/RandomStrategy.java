package Strategies;

import Game.Board;
import Game.Move;
import Game.Puyo;

import java.util.ArrayList;
import java.util.Random;

public class RandomStrategy implements Strategy {
    private Random x;
    private ArrayList<Long> times = new ArrayList<>();

    public RandomStrategy(int seed){
        x = new Random(seed);
    }

    @Override
    public Move makeMove(Board b, Puyo[][] currentPuyo, Board oppBoard) {
        long start = System.nanoTime();
        Move m = new Move(x.nextInt(6), x.nextInt(4));
        while(!b.dropPuyo(currentPuyo[0], m)){
            m = new Move(x.nextInt(6), x.nextInt(4));
        }
        long roundedTime = System.nanoTime() - start;
        times.add(roundedTime);
        return m;
    }

    public ArrayList<Long> getTimes(){
        return times;
    }
}
