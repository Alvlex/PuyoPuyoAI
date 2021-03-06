package tmsPrep;

import Game.Board;
import Game.Chain;
import Game.Coordinate;
import Game.Puyo;
import appTest.Template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class RelMatrix {

    char[][] relMatrix;
    char[] sortedLetters;

    RelMatrix(LabelledCells lc, Template chain){
        Board board = chain.b.copyBoard();
        Puyo[][] shallowCopy = new Puyo[6][13];
        for (int i = 0; i < 6; i ++){
            for (int j = 0; j < 13; j ++){
                shallowCopy[i][j] = board.getPuyoObj(i,j);
            }
        }
        Chain ch = new Chain(board);
        char[][] cells = lc.cells;
        HashSet<Character> letters = new HashSet<>();
        for (int i = 0; i < cells.length; i ++){
            for (int j = 0; j < cells[i].length; j ++){
                letters.add(cells[i][j]);
            }
        }
        char[] sortedLetters = new char[letters.size()];
        int index = 0;
        for (char c: letters){
            sortedLetters[index] = c;
            index ++;
        }
        Arrays.sort(sortedLetters);
        this.sortedLetters = sortedLetters;
        relMatrix = new char[sortedLetters.length][sortedLetters.length];
        for (char c: this.sortedLetters){
            if (Character.isUpperCase(c))
                setCell(c, c, 'S');
        }

        for (int i = 0; i < 6; i ++){
            for (int j = 0; j < 13; j ++){
                char c1 = cells[i][j];
                if (Character.isUpperCase(c1)){
                    for (Coordinate c: lc.findAdjacent(lc.cells, i,j)){
                        char c2 = cells[c.getX()][c.getY()];
                        if (c1 != c2){
                            setCell(c1, c2, 'X');
                            setCell(c2, c1, 'X');
                            // May need to also go through chain and check extra spaces.
                        }
                    }
                }
            }
        }
        ArrayList<Coordinate> recDropped;
        while(ch.isPopping()){
            recDropped = ch.chainTurn();
            for (Coordinate c: recDropped){
                Coordinate c1 = lc.findPuyoInBoard(shallowCopy, board.getPuyoObj(c.getX(), c.getY()));
                char label1 = lc.cells[c1.getX()][c1.getY()];
                for (Coordinate c2: ch.findAdjacent(c)) {
                    Coordinate c2Linked = lc.findPuyoInBoard(shallowCopy, board.getPuyoObj(c2.getX(), c2.getY()));
                    char label2 = lc.cells[c2Linked.getX()][c2Linked.getY()];
                    if (label1 != label2 && label2 != '0'){
                        setCell(label1, label2, 'X');
                        setCell(label2, label1, 'X');
                    }
                }
            }
        }

        print();
    }

    private void print(){
        System.out.print("  ");
        for (int i = 0; i < sortedLetters.length; i ++){
            System.out.print(sortedLetters[i] + " ");
        }
        System.out.println();
        for (int i = 0; i < relMatrix.length; i ++){
            System.out.print(sortedLetters[i] + " ");
            for (int j = 0; j < relMatrix[i].length; j ++){
                if (relMatrix[i][j] == '\u0000')
                    System.out.print(" ");
                System.out.print(relMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    private int findIndex(char c){
        for (int i = 0; i < sortedLetters.length; i ++){
            if (sortedLetters[i] == c)
                return i;
        }
        return -1;
    }

    void setCell(char col, char row, char symbol){
        relMatrix[findIndex(col)][findIndex(row)] = symbol;
    }

    char getCell(char col, char row){
        return relMatrix[findIndex(col)][findIndex(row)];
    }
}
