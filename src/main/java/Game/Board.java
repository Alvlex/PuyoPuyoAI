package Game;

import java.util.ArrayList;

public class Board implements BoardI{

    private Puyo[][] board;

    public Board(){
        board = new Puyo[6][13];
    }

    public Board(Puyo[][] board){
        this.board = board;
    }

    // Returns true if successfully added, otherwise false
    private boolean dropPuyoHelper(Puyo p, int col){
        for (int row = 0; row < board[0].length; row ++){
            if (board[col][row] == null){
                board[col][row] = p;
                return true;
            }
        }
        return false;
    }

    @Override
    public void dropGarbage(int col){
        dropPuyoHelper(PuyoI.createGarbage(), col);
    }

    private void removePuyo(int col){
        for (int i = board[col].length - 1; i >= 0; i --){
            if (board[col][i] != null){
                board[col][i] = null;
                return;
            }
        }
    }

    @Override
    public boolean dropPuyo(Puyo[] puyos, Move m){
        // Only can ever have 2 puyos here, and can fail
        // if either Puyo drops into a full column
        // If the first one drops into a full column, then
        // just return false.
        // If the second one drops into a full column, then
        // remove the first one and then return false
        int dropped = -1; // This records the index of the first dropped Puyo
        Coordinate[] coords = m.getCoord();
        for (int i = 1; i >= 0; i --){
            for (int j = 0; j < 2; j ++){
                if (coords[j].getY() == i){
                    if (dropPuyoHelper(puyos[j], coords[j].getX())){
                        dropped = j;
                        continue;
                    }
                    else if (dropped != -1){
                        removePuyo(coords[j].getX());
                    }
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public ArrayList<Coordinate> cascadePuyo(){
        ArrayList<Coordinate> dropped = new ArrayList<>();
        for (int col = 0; col < getNoCols(); col ++){
            int noOfSpaces = 0;
            for (int row = 0; row < getNoRows(); row ++){
                if (board[col][row] == null){
                    noOfSpaces ++;
                }
                else if (noOfSpaces > 0){
                    board[col][row - noOfSpaces] = board[col][row];
                    board[col][row] = null;
                    dropped.add(new Coordinate(col, row - noOfSpaces));
                }
            }
        }
        return dropped;
    }

    @Override
    public Board copyBoard(){
        Puyo[][] result = new Puyo[board.length][board[0].length];
        for (int i = 0; i < board.length; i ++){
            for (int j = 0; j < board[i].length; j ++){
                result[i][j] = getPuyo(i,j);
            }
        }
        return new Board(result);
    }

    @Override
    public Puyo getPuyo(int col, int row){
        if (board[col][row] == null)
            return null;
        return board[col][row].copyPuyo();
    }
    @Override
    public Puyo getPuyo(Coordinate pos){
        return getPuyo(pos.getX(), pos.getY());
    }

    public Puyo getPuyoObj(int col, int row){
        if (board[col][row] == null)
            return null;
        return board[col][row];
    }

    @Override
    public int getNoCols(){
        return board.length;
    }
    @Override
    public int getNoRows(){
        return board[0].length;
    }

    @Override
    public void removePuyo(int col, int row){
        board[col][row] = null;
    }
    @Override
    public void removePuyo(Coordinate pos){
        board[pos.getX()][pos.getY()] = null;
    }

    @Override
    public int peekCol(int col){
        for (int row = getNoRows() - 1; row >= 0; row --){
            if (board[col][row] != null){
                return row;
            }
        }
        // Should only be called to find the highest row with a Puyo in it
        return -1;
    }

    @Override
    public boolean checkPossibilities() {
        // 11 Possibilities, 5 horizontal and 6 vertical
        for (int i = 0; i < 6; i ++){
            if (dropPuyoHelper(PuyoI.createGarbage(), i)) {
                boolean flag = false;
                if (dropPuyoHelper(null, i)){
                    flag = true;
                }
                removePuyo(i, peekCol(i));
                if (flag)
                    return true;
            }
        }
        for (int i = 0; i < 5; i ++){
            if (dropPuyoHelper(null, i) && dropPuyoHelper(null, i + 1)){
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<Coordinate> findAllPuyo(){
        ArrayList<Coordinate> allPuyo = new ArrayList<>();
        for (int i = 0; i < getNoCols(); i ++){
            for (int j = 0; j < getNoRows(); j ++){
                if (getPuyo(i,j) != null)
                    allPuyo.add(new Coordinate(i,j));
            }
        }
        return allPuyo;
    }

    @Override
    public boolean equalBoards(Board b2){
        for (int i = 0; i < 6; i ++){
            for (int j = 0; j < 13; j ++){
                if (getPuyo(i,j) == null || b2.getPuyo(i,j) == null){
                    if(getPuyo(i,j) != b2.getPuyo(i,j)){
                        return false;
                    }
                }
                else if (!getPuyo(i,j).getColour().equals(b2.getPuyo(i,j).getColour())){
                    return false;
                }
            }
        }
        return true;
    }
}