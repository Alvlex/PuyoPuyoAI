package appTest;

import AI.RandomStrategy;
import app.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class unitTestsPlayer {

    @Test
    public void turnTest(){
        Board b = new Board();
        Player p = new Player(b,  4);
        RandomStrategy rs = new RandomStrategy();
        Puyo[][] pastPuyo = copyPuyoList(p.currentPuyo);
        p.turn(rs);
        Assert.assertTrue(assertBoardsDifferent(new Board(), b));
        Assert.assertTrue(assertListsDifferent(pastPuyo, p.currentPuyo));

    }

    @Test
    public void findRecentlyDroppedTest(){
        Board b = new Board();
        Player p = new Player(b, 4);
        Random x = new Random();
        RandomStrategy rs = new RandomStrategy();
        Move m = p.turn(rs);
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

    private boolean assertListsDifferent(Puyo[][] p1, Puyo[][] p2){
        for (int i = 0; i < p1.length; i ++){
            for (int j = 0; j < p1[i].length; j ++){
                if (p1[i][j] != p2[i][j]){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean assertBoardsDifferent(Board b1, Board b2){
        for (int i = 0; i < 6; i ++){
            for (int j = 0; j < 13; j ++){
                if (b1.getPuyo(i,j) == null && b2.getPuyo(i,j) != null){
                    return true;
                }
                else if (b1.getPuyo(i,j) != null && b2.getPuyo(i,j) == null){
                    return true;
                }
                else if (b1.getPuyo(i,j) == null && b2.getPuyo(i,j) == null){
                    // So we don't compare colours of null Puyo
                }
                else if (!b1.getPuyo(i,j).getColour().equals(b2.getPuyo(i,j).getColour())){
                    return true;
                }
            }
        }
        return false;
    }
}
