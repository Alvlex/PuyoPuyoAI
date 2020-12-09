package appTest;

import AI.RandomStrategy;
import app.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class unitTestsPlayer {

    Template empty = new Template("empty.csv");
    Board b;
    Player p;
    Puyo[][] pastPuyo;

    @Before
    public void prepare(){
        b = empty.getBoard();
        p = new Player(b,  4, new RandomStrategy());
        pastPuyo = copyPuyoList(p.currentPuyo);
    }


    @Test
    public void turnTest(){
        p.turn();
        Assert.assertFalse(empty.equalBoards(b));
        Assert.assertTrue(listsDifferent(pastPuyo, p.currentPuyo));

    }

    @Test
    public void findRecentlyDroppedTest(){
        Move m = p.turn();
        int[][] coords = m.getCoord();
        List<int[]> test = p.findRecentlyDropped(m);
        if (coords[0][1] == 1 || coords[1][1] == 1){
            int temp = coords[0][1];
            coords[0][1] = coords[1][1];
            coords[1][1] = temp;
        }
        int[] first = {coords[0][0], coords[0][1]};
        int[] second = {coords[1][0], coords[1][1]};
        Assert.assertArrayEquals(test.get(0), first);
        Assert.assertArrayEquals(test.get(1), second);
    }

    private Puyo[][] copyPuyoList(Puyo[][] p){
        Puyo[][] result = new Puyo[p.length][p[0].length];
        for (int i = 0; i < p.length; i ++){
            for (int j = 0; j < p[i].length; j ++){
                result[i][j] = p[i][j];
            }
        }
        return result;
    }

    private boolean listsDifferent(Puyo[][] p1, Puyo[][] p2){
        for (int i = 0; i < p1.length; i ++){
            for (int j = 0; j < p1[i].length; j ++){
                if (!p1[i][j].getColour().equals(p2[i][j].getColour())){
                    return true;
                }
            }
        }
        return listSameElement(p1) && listSameElement(p2);
    }

    private boolean listSameElement(Puyo[][] p){
        for (int i = 0; i < 2; i ++){
            String colour = p[0][i].getColour();
            for (int j = 0; j < p.length; j ++){
                if (!p[j][i].getColour().equals(colour)){
                    return false;
                }
            }
        }
        return true;
    }
}
