import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class Board {

    private Puyo[][] board;
    Chain chain;

    Board(){
        board = new Puyo[6][13];
        chain = new Chain(this);
    }

    Board(Puyo[][] board){
        this.board = board;
        chain = new Chain(this);
    }

    // Returns true if successfully added, otherwise false
    private boolean dropPuyoHelper(Puyo p, int col){
        for (int row = 0; row < board[0].length; row ++){
            if (board[col][row] == null){
                board[col][row] = p;
                return true;
            }
        }
        return checkEnd();
    }

    private void removePuyo(int col){
        for (int i = board[col].length - 1; i >= 0; i --){
            if (board[col][i] != null){
                board[col][i] = null;
                return;
            }
        }
    }

    boolean checkEnd(){
        for (Puyo[] col : board) {
            if (col[col.length - 1] == null) {
                return false;
            }
        }
        return true;
    }

    boolean dropPuyo(Puyo[] puyos, Move m){
        // Only can ever have 2 puyos here, and can fail
        // if either Puyo drops into a full column
        // If the first one drops into a full column, then
        // just return false.
        // If the second one drops into a full column, then
        // remove the first one and then return false
        int dropped = -1; // This records the index of the first dropped Puyo
        int[][] coords = m.getCoord();
        chain.resetChain(); // If we're dropping Puyo, then we can't be in the middle of a chain
        for (int i = 1; i >= 0; i --){
            for (int j = 0; j < 2; j ++){
                if (coords[j][1] == i){
                    if (dropPuyoHelper(puyos[j], coords[j][0])){
                        dropped = j;
                        continue;
                    }
                    else if (dropped != -1){
                        removePuyo(coords[j][0]);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    ArrayList<int[]> cascadePuyo(){
        ArrayList<int[]> dropped = new ArrayList<>();
        for (int col = 0; col < getNoCols(); col ++){
            int noOfSpaces = 0;
            for (int row = 0; row < getNoRows(); row ++){
                if (board[col][row] == null){
                    noOfSpaces ++;
                }
                else if (noOfSpaces > 0){
                    for (int i = row; i < getNoRows(); i ++){
                        board[col][i - noOfSpaces] = board[col][i];
                        board[col][i] = null;
                        if (board[col][i - noOfSpaces] != null) {
                            dropped.add(new int[]{col, i - noOfSpaces});
                        }
                    }
                    noOfSpaces = 0;
                    row -= noOfSpaces - 1;
                }
            }
        }
        return dropped;
    }
    // TODO Need to write a unit test to test for if multiple gaps of pops along the same column cascade right

    Puyo getPuyo(int col, int row){
        if (board[col][row] == null)
            return null;
        return board[col][row].copyPuyo();
    }
    Puyo getPuyo(int[] pos){
        return getPuyo(pos[0], pos[1]);
    }

    int getNoCols(){
        return board.length;
    }
    int getNoRows(){
        return board[0].length;
    }

    void removePuyo(int col, int row){
        board[col][row] = null;
    }
    void removePuyo(int[] pos){
        board[pos[0]][pos[1]] = null;
    }

    int peekCol(int col){
        for (int row = getNoRows() - 1; row >= 0; row --){
            if (board[col][row] != null){
                return row;
            }
        }
        // Should only be called to find the highest row with a Puyo in it
        return -1;
    }
}