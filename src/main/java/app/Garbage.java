package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Garbage implements GarbageI{

    private Board board;
    private int garbage = 0;

    Garbage(Board b){
        board = b;
    }

    public void makeGarbage(int score){
        garbage = score;
    }

    public void dropGarbage(){
        ArrayList<Integer> columns = new ArrayList<>(Arrays.asList(0,1,2,3,4,5));
        Random x = new Random();
        while(garbage > 0){
            int nextCol = columns.get(x.nextInt(columns.size()));
            if (columns.size() == 0){
                columns = new ArrayList<>(Arrays.asList(0,1,2,3,4,5));
            }
            board.dropGarbage(nextCol);
            garbage --;
        }
    }

    public int removeGarbage(int score){
        if (garbage == 0){
            return score;
        }
        else{
            int min = Math.min(score, garbage);
            garbage -= min;
            return score - min;
        }
    }


}
