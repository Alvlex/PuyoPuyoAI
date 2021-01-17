package AI;

import app.Board;
import app.Move;
import app.Puyo;

public interface Strategy {
    Move makeMove(Board b, Puyo[][] currentPuyo, Board oppBoard);
}
