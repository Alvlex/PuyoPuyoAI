package AI;

import app.Board;
import app.Move;
import app.Puyo;

public class TestingStrategy implements Strategy {
    @Override
    public Move makeMove(Board b, Puyo[][] currentPuyo, Board oppBoard) {
        Move m = new Move(0,0);
        Move m2 = new Move(1, 1);
        boolean mFine = true;
        while(!b.dropPuyo(currentPuyo[0], m)){
            if (m.getCoord()[0].getX() == 5){
                mFine = false;
                break;
            }
            m.right();
        }
        if (mFine)
            return m;
        while(!b.dropPuyo(currentPuyo[0], m2)){
            if (m2.getCoord()[0].getX() == 5){
                return null;
            }
            m2.right();
        }
        return m2;
    }
}
