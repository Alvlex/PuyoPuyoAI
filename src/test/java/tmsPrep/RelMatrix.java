package tmsPrep;

import app.Coordinate;

import java.util.Arrays;
import java.util.HashSet;

public class RelMatrix {

    char[][] relMatrix;
    char[] sortedLetters;

    RelMatrix(LabelledCells lc){
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
