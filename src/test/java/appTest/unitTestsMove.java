package appTest;

import app.Move;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class unitTestsMove {

    @Test
    public void leftTest(){
        Move m = new Move(5, 0);
        for (int i = 5; i >= 0; i --){
            Assert.assertEquals(m.getCoord()[0][0], i);
            Assert.assertEquals(m.getCoord()[1][0], i);
            m.left();
        }
        Assert.assertEquals(m.getCoord()[0][0], 0);
        Assert.assertEquals(m.getCoord()[1][0], 0);
    }

    @Test
    public void rightTest(){
        Move m = new Move(0, 0);
        for (int i = 0; i < 6; i ++){
            Assert.assertEquals(m.getCoord()[0][0], i);
            Assert.assertEquals(m.getCoord()[1][0], i);
            m.right();
        }
        Assert.assertEquals(m.getCoord()[0][0], 5);
        Assert.assertEquals(m.getCoord()[1][0], 5);
    }

    @Test
    public void rotClockTest(){
        Move m = new Move(2,0);
        for (int i = 0; i < 4; i ++){
            Assert.assertEquals(m.getCoord()[0][0], 2);
            Assert.assertEquals(m.getCoord()[0][1], i == 2 ? 1: 0);
            Assert.assertEquals(m.getCoord()[1][0], i == 0 ? 2: 4 - i);
            Assert.assertEquals(m.getCoord()[1][1], i == 0 ? 1: 0);
            m.rotClock();
        }
        Move m2 = new Move(5, 0);
        Move m3 = new Move(0, 2);
        m2.rotClock();
        m3.rotClock();
        for (int i = 0; i < 2; i ++){
            for (int j = 0; j < 2; j ++){
                Assert.assertEquals(m2.getCoord()[i][j], j == 1 ? 0 : i + 4);
                Assert.assertEquals(m3.getCoord()[i][j], j == 1 ? 0 : 1 - i);
            }
        }
    }

    @Test
    public void rotAntiTest(){
        Move m = new Move(2,0);
        for (int i = 0; i < 4; i ++){
            Assert.assertEquals(m.getCoord()[0][0], 2);
            Assert.assertEquals(m.getCoord()[0][1], i == 2 ? 1: 0);
            Assert.assertEquals(m.getCoord()[1][0], i == 0 ? 2: i);
            Assert.assertEquals(m.getCoord()[1][1], i == 0 ? 1: 0);
            m.rotAnti();
        }
        Move m2 = new Move(5, 2);
        Move m3 = new Move(0, 0);
        m2.rotAnti();
        m3.rotAnti();
        for (int i = 0; i < 2; i ++){
            for (int j = 0; j < 2; j ++){
                Assert.assertEquals(m2.getCoord()[i][j], j == 1 ? 0 : i + 4);
                Assert.assertEquals(m3.getCoord()[i][j], j == 1 ? 0 : 1 - i);
            }
        }
    }

    @Test
    public void getCoordTest(){
        Random x = new Random();
        int col = x.nextInt(6);
        int rot = x.nextInt(4);
        Move m = new Move(col, rot);
        for (int i = 0; i < 2; i ++){
            for (int j = 0; j < 2; j ++){
                Assert.assertEquals(m.getCoord()[i][j], j == 0 ? (i == 0 ? col : rot == 0 ? col : col - 2 + rot) :
                        (i == 0 ? rot == 2 ? 1 : 0 : rot == 0 ? 1 : 0));
            }
        }

    }
}
