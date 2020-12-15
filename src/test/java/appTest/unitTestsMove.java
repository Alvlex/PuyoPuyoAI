package appTest;

import app.Move;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class unitTestsMove {

    private Move move;
    private Move leftEdge;
    private Move rightEdge;

    @Before
    public void prepare(){
        move = new Move(2, 0);
        leftEdge = new Move(0,0);
        rightEdge = new Move(5,2);
    }

    @Test
    public void leftTest(){
        for (int i = 5; i >= 0; i --){
            Assert.assertEquals(rightEdge.getCoord()[0][0], i);
            Assert.assertEquals(rightEdge.getCoord()[1][0], i);
            rightEdge.left();
        }
        Assert.assertEquals(rightEdge.getCoord()[0][0], 0);
        Assert.assertEquals(rightEdge.getCoord()[1][0], 0);
    }

    @Test
    public void rightTest(){
        for (int i = 0; i < 6; i ++){
            Assert.assertEquals(leftEdge.getCoord()[0][0], i);
            Assert.assertEquals(leftEdge.getCoord()[1][0], i);
            leftEdge.right();
        }
        Assert.assertEquals(leftEdge.getCoord()[0][0], 5);
        Assert.assertEquals(leftEdge.getCoord()[1][0], 5);
    }

    @Test
    public void rotClockTest(){
        for (int i = 0; i < 4; i ++){
            Assert.assertEquals(move.getCoord()[0][0], 2);
            Assert.assertEquals(move.getCoord()[0][1], i == 2 ? 1: 0);
            Assert.assertEquals(move.getCoord()[1][0], i == 0 ? 2: i);
            Assert.assertEquals(move.getCoord()[1][1], i == 0 ? 1: 0);
            move.rotClock();
        }
    }

    @Test
    public void rotClockEdgeTest(){
        leftEdge.rotClock();
        rightEdge.rotClock();
        for (int i = 0; i < 2; i ++){
            for (int j = 0; j < 2; j ++){
                Assert.assertEquals(rightEdge.getCoord()[i][j], j == 1 ? 0 : i + 4);
                Assert.assertEquals(leftEdge.getCoord()[i][j], j == 1 ? 0 : 1 - i);
            }
        }
    }

    @Test
    public void rotAntiTest(){
        for (int i = 0; i < 4; i ++){
            Assert.assertEquals(move.getCoord()[0][0], 2);
            Assert.assertEquals(move.getCoord()[0][1], i == 2 ? 1: 0);
            Assert.assertEquals(move.getCoord()[1][0], i == 0 ? 2: 4 - i);
            Assert.assertEquals(move.getCoord()[1][1], i == 0 ? 1: 0);
            move.rotAnti();
        }

    }

    @Test
    public void rotAntiEdgeTest(){
        for (int i = 0; i < 3; i ++) {
            rightEdge.rotAnti();
            leftEdge.rotAnti();
        }
        for (int i = 0; i < 2; i ++){
            for (int j = 0; j < 2; j ++){
                Assert.assertEquals(rightEdge.getCoord()[i][j], j == 1 ? 0 : i + 4);
                Assert.assertEquals(leftEdge.getCoord()[i][j], j == 1 ? 0 : 1 - i);
            }
        }
    }

    @Test
    public void getCoordTest(){
        for (int col = 0; col < 6; col ++){
            for (int rot = 0; rot < 4; rot ++){
                if ((col == 0 && rot == 1) || (col == 5 && rot == 3)){
                    continue;
                }
                Move m = new Move(col, rot);
                for (int i = 0; i < 2; i ++){
                    for (int j = 0; j < 2; j ++){
                        Assert.assertEquals(m.getCoord()[i][j], j == 0 ? (i == 0 ? col : rot == 0 ? col : col - 2 + rot) :
                                (i == 0 ? rot == 2 ? 1 : 0 : rot == 0 ? 1 : 0));
                    }
                }
            }
        }
    }
}
