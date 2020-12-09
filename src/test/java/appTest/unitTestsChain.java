package appTest;

import app.Board;
import app.Chain;
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
    private ArrayList<int[]> emptyR = getRecentlyDropped(emptyT.getBoard());
    private Chain full = new Chain(fullT.getBoard());
    private ArrayList<int[]> fullR = getRecentlyDropped(fullT.getBoard());
    private Chain chain1;
    private ArrayList<int[]> chain1R;
    private Chain chain4;
    private ArrayList<int[]> chain4R;
    private Chain bigChain1 = new Chain(bigChain1T.getBoard());
    private ArrayList<int[]> bigChain1R = getRecentlyDropped(bigChain1T.getBoard());
    private Board garbageClearB = garbageClearT.getBoard();
    private Chain garbageClear = new Chain(garbageClearB);
    private ArrayList<int[]> garbageClearR = getRecentlyDropped(garbageClearB);
    private Board garbageClear2B = garbageClear2T.getBoard();
    private Chain garbageClear2 = new Chain(garbageClear2B);
    private ArrayList<int[]> garbageClear2R = getRecentlyDropped(garbageClear2B);

    @Before
    public void prepare(){
        chain1 = new Chain(chain1T.getBoard());
        chain1R = getRecentlyDropped(chain1T.getBoard());
        chain4 = new Chain(chain4T.getBoard());
        chain4R = getRecentlyDropped(chain4T.getBoard());
    }

    private ArrayList<int[]> getRecentlyDropped(Board b){
        ArrayList<int[]> result = new ArrayList<>();
        for (int i = 0; i < b.getNoCols(); i ++){
            for (int j = 0; j < b.getNoRows(); j ++){
                if (b.getPuyo(i,j) != null){
                    result.add(new int[]{i,j});
                }
            }
        }
        return result;
    }

    @Test
    public void isPoppingTestFalse(){
        Assert.assertFalse(empty.isPopping(emptyR));
        Assert.assertFalse(full.isPopping(fullR));
    }

    @Test
    public void isPoppingTestTrue(){
        Assert.assertTrue(chain1.isPopping(chain1R));
        Assert.assertTrue(chain4.isPopping(chain4R));
    }

    @Test
    public void chainTurnTestChain1(){
        Assert.assertEquals(chain1.chainTurn(chain1R), new ArrayList<int[]>());

    }

    @Test
    public void scoreChain1Test(){
        chain1.chainTurn(chain1R);
        Assert.assertEquals(chain1.score(), 0);
    }

    @Test
    public void scoreChain4Test(){
        for (int i = 0; i < 4; i ++) {
            chain4R = chain4.chainTurn(chain4R);
        }
        Assert.assertEquals(chain4.score(), 36);
    }

    @Test
    public void scoreBigChain1Test(){
        bigChain1.chainTurn(bigChain1R);
        Assert.assertEquals(bigChain1.score(), 5);
    }

    @Test
    public void garbageClearTest(){
        garbageClear.chainTurn(garbageClearR);
        Assert.assertTrue(emptyT.equalBoards(garbageClearB));
    }

    @Test
    public void garbageClear2Test(){
        garbageClear2.chainTurn(garbageClear2R);
        Assert.assertTrue(garbageClear2OutputT.equalBoards(garbageClear2B));
    }
}
