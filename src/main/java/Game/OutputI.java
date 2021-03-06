package Game;

public interface OutputI {
    void updateBoard(Board b, int playerNo);
    void updateCurrentPuyo(Puyo[][] currentPuyo, int playerNo);
    void updateMoves(Move m, int playerNo);
    String printCurrentPuyo();
    String printBoards();
}
