package appTest;

import Strategies.TestingStrategy;
import Game.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class unitTestsPlayer {

    private Template empty = new Template("empty.csv");
    private Board b;
    private Player p;
    private Puyo[][] pastPuyo;

    @Before
    public void prepare(){
        b = empty.getBoard();
        p = new Player(b,  4, new TestingStrategy(), 0);
        pastPuyo = copyPuyoList(p.currentPuyo);
    }


    @Test
    public void turnTest(){
        p.turn(new Board());
        Assert.assertFalse(empty.equalBoards(b));
        Assert.assertTrue(listsDifferent(pastPuyo, p.currentPuyo));

    }

    @Test
    public void findRecentlyDroppedTest(){
        Move m = p.turn();
        List<Coordinate> test = p.findRecentlyDropped(m);
        Assert.assertEquals(test.get(0).getX(), 0);
        Assert.assertEquals(test.get(0).getY(), 1);
        Assert.assertEquals(test.get(0).getX(), 0);
        Assert.assertEquals(test.get(1).getY(), 0);
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
