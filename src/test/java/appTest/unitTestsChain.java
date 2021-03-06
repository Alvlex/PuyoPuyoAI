package appTest;

import Game.Board;
import Game.Chain;
import Game.Coordinate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class unitTestsChain {

    private Template emptyT = new Template("empty.csv");
    private Template fullT = new Template("full.csv");
    private Template chain1T = new Template("chain1.csv");
    private Template chain4T = new Template("chain4.csv");
    private Template bigChain1T = new Template("bigChain1.csv");
    private Template garbageClearT = new Template("garbageClear.csv");
    private Template garbageClear2T = new Template("garbageClear2.csv");
    private Template garbageClear2OutputT = new Template("garbageClear2Output.csv");

    private Chain empty = new Chain(emptyT.getBoard());
    private Chain full = new Chain(fullT.getBoard());
    private Chain chain1;
    private Chain chain4;
    private Chain bigChain1 = new Chain(bigChain1T.getBoard());
    private Board garbageClearB = garbageClearT.getBoard();
    private Chain garbageClear = new Chain(garbageClearB);
    private Board garbageClear2B = garbageClear2T.getBoard();
    private Chain garbageClear2 = new Chain(garbageClear2B);

    @Before
    public void prepare(){
        chain1 = new Chain(chain1T.getBoard());
        chain4 = new Chain(chain4T.getBoard());
    }

    @Test
    public void isPoppingTestFalse(){
        Assert.assertFalse(empty.isPopping());
        Assert.assertFalse(full.isPopping());
    }

    @Test
    public void isPoppingTestTrue(){
        Assert.assertTrue(chain1.isPopping());
        Assert.assertTrue(chain4.isPopping());
    }

    @Test
    public void chainTurnTestChain1(){
        Assert.assertEquals(chain1.chainTurn(), new ArrayList<int[]>());

    }

    @Test
    public void scoreChain1Test(){
        chain1.chainTurn();
        Assert.assertEquals(chain1.score(), 0);
    }

    @Test
    public void scoreChain4Test(){
        ArrayList<Coordinate> recDropped = chain4.chainTurn();
        for (int i = 0; i < 3; i ++) {
            recDropped = chain4.chainTurn(recDropped);
        }
        Assert.assertEquals(chain4.score(), 36);
    }

    @Test
    public void scoreBigChain1Test(){
        bigChain1.chainTurn();
        Assert.assertEquals(bigChain1.score(), 5);
    }

    @Test
    public void garbageClearTest(){
        garbageClear.chainTurn();
        Assert.assertTrue(emptyT.equalBoards(garbageClearB));
    }

    @Test
    public void garbageClear2Test(){
        garbageClear2.chainTurn();
        Assert.assertTrue(garbageClear2OutputT.equalBoards(garbageClear2B));
    }
}
