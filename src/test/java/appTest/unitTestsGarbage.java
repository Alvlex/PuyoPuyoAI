package appTest;

import app.Board;
import app.Garbage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class unitTestsGarbage {

    Garbage g;
    Board b;

    @Before
    public void prepare(){
        b = new Board();
        g = new Garbage(b);
    }

    @Test
    public void dropGarbageTest(){
        Board b2 = b.copyBoard();
        Garbage g2 = new Garbage(b2);
        g2.makeGarbage(6);
        g2.dropGarbage();
        Assert.assertEquals(10, g2.removeGarbage(10));
        Board test = new Board();
        for (int i = 0; i < 6; i ++) {
            test.dropGarbage(i);
        }
        unitTestsBoard utb = new unitTestsBoard();
        utb.assertEqualBoards(b2, test);
        g2.makeGarbage(72);
        g2.dropGarbage();
        Assert.assertFalse(b2.checkPossibilities());
    }

    @Test
    public void removeGarbageTest(){
        Board b2 = b.copyBoard();
        Garbage g2 = new Garbage(b2);
        Random x = new Random();
        int garbage = x.nextInt(100);
        Assert.assertEquals(garbage, g2.removeGarbage(garbage));
        g2.makeGarbage(50);
        int score = g2.removeGarbage(garbage);
        Assert.assertTrue(score == garbage - 50 || score == 0);
    }
}
