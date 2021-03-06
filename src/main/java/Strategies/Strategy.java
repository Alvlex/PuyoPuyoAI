package Strategies;

import Game.Board;
import Game.Move;
import Game.Puyo;

public interface Strategy {
    Move makeMove(Board b, Puyo[][] currentPuyo, Board oppBoard);
}
