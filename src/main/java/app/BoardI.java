package app;

import java.util.ArrayList;

public interface BoardI {
    void dropGarbage(int col);
    boolean checkPossibilities();
    boolean dropPuyo(Puyo[] puyos, Move m);
    ArrayList<int[]> cascadePuyo();
    Board copyBoard();
    Puyo getPuyo(int col, int row);
    Puyo getPuyo(int[] pos);
    int getNoCols(); // Don't need tests
    int getNoRows(); // Don't need tests
    void removePuyo(int col, int row); // Don't need tests
    void removePuyo(int[] pos); // Don't need tests
    int peekCol(int col);
    ArrayList<int[]> findAllPuyo();
    boolean equalBoards(Board b); // Still needs tests
}
