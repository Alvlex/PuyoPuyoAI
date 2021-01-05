package tmsPrep;

import app.Board;
import app.Chain;
import app.Coordinate;
import app.Puyo;
import appTest.Template;
import java.util.ArrayList;

public class LabelledCells {

    char[][] cells = new char[6][13];

    public LabelledCells(Template chain, Template template){
        int character = 65;
        int lowerCase = 97;
        Board board = chain.b.copyBoard();
        Puyo[][] shallowCopy = new Puyo[6][13];
        for (int i = 0; i < 6; i ++){
            for (int j = 0; j < 13; j ++){
                shallowCopy[i][j] = board.getPuyoObj(i,j);
            }
        }
        Chain c = new Chain(board);
        while(c.isPopping()) {
            ArrayList<ArrayList<Coordinate>> groups = c.findPops(board.findAllPuyo());
            for (ArrayList<Coordinate> group : groups) {
                if (group.size() >= 4) {
                    for (Coordinate coords : group) {
                        Coordinate newCoords = findPuyoInBoard(shallowCopy, board.getPuyoObj(coords.getX(), coords.getY()));
                        cells[newCoords.getX()][newCoords.getY()] = (char) character;
                    }
                    character++;
                }
            }
            c.chainTurn();
        }

        for (int i = 0; i < 6; i ++){
            for (int j = 0; j < 13; j ++){
                if (template.b.getPuyo(i,j) == null)
                    cells[i][j] = '\u0000';
            }
        }

        // Need to get the lowercase letters and 0s
        for (int i = 5; i >= 0; i --){
            for (int j = 0; j < 13; j ++){
                for (Coordinate coords: findAdjacent(cells, i, j)){
                    if (!Character.isUpperCase(cells[i][j]) && Character.isUpperCase(cells[coords.getX()][coords.getY()])){
                        cells[i][j] = (char) lowerCase ++;
                        break;
                    }
                }
                if (cells[i][j] == '\u0000')
                    cells[i][j] = '0';
            }
        }

        for (int i = 12; i >= 0; i --){
            for (int j = 0; j < 6; j ++){
                System.out.print(cells[j][i]);
            }
            System.out.println();
        }

    }

    ArrayList<Coordinate> findAdjacent(char[][] board, int col, int row){
        ArrayList<Coordinate> result = new ArrayList<>();
        if (col > 0){
            if (board[col - 1][row] != '\u0000'){
                result.add(new Coordinate(col - 1, row));
            }
        }
        if (col < 5){
            if (board[col + 1][row] != '\u0000'){
                result.add(new Coordinate(col + 1, row));
            }
        }
        if (row > 0){
            if (board[col][row - 1] != '\u0000'){
                result.add(new Coordinate(col, row - 1));
            }
        }
        if (row < 12){
            if (board[col][row + 1] != '\u0000'){
                result.add(new Coordinate(col, row + 1));
            }
        }
        return result;
    }

    Coordinate findPuyoInBoard(Puyo[][] board, Puyo p){
        for (int i = 0; i < board.length; i ++){
            for (int j = 0; j < board[i].length; j ++){
                if (p == board[i][j])
                    return new Coordinate(i,j);
            }
        }
        System.out.println(p.getColour());
        return null;
    }
}
