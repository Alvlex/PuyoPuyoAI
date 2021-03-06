package Strategies;

import Game.Board;
import Game.Move;
import Game.Puyo;

import java.util.Random;

public class RandomStrategy implements Strategy {
    @Override
    public Move makeMove(Board b, Puyo[][] currentPuyo, Board oppBoard) {
        Random x = new Random();
        Move m = new Move(x.nextInt(6), x.nextInt(4));
        while(!b.dropPuyo(currentPuyo[0], m)){
            m = new Move(x.nextInt(6), x.nextInt(4));
        }
        return m;
    }
}
