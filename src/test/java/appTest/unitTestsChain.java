package appTest;

import app.Board;
import app.Chain;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class unitTestsChain {

    Templates t = new Templates();

    @Test
    public void isPoppingTestFalse(){
        Template[] toCheck = new Template[]{t.get("empty"), t.get("full")};
        for (Template t: toCheck) {
            Assert.assertFalse(new Chain(t.getBoard()).isPopping(t.getRecentlyDropped()));
        }
    }

    @Test
    public void isPoppingTestTrue(){
        Template[] toCheck = new Template[]{t.get("chain1"), t.get("chain4")};
        for (Template t: toCheck) {
            Assert.assertTrue(new Chain(t.getBoard()).isPopping(t.getRecentlyDropped()));
        }
    }

    @Test
    public void chainTurnTestChain1(){
        Template chain1 = t.get("chain1");
        Assert.assertEquals(new Chain(chain1.getBoard()).chainTurn(chain1.getRecentlyDropped()), new ArrayList<int[]>());

    }

    @Test
    public void scoreTest(){
//        int[] scores = {0, 36, 3};
//        for (int j = 0; j < customBoards.size(); j ++) {
//            Chain customCopy = new Chain(new Board(customBoards.get(j)));
//            ArrayList<int[]> recentlyDropped = customRecDrop.get(j);
//            while(customCopy.isPopping(recentlyDropped)){
//                recentlyDropped = customCopy.chainTurn(recentlyDropped);
//            }
//            Assert.assertEquals(customCopy.score(), scores[j]);
//        }
    }
}
