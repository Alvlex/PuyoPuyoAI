package appTest;

//TODO Need to make these tests more generic

import app.Board;
import app.Puyo;
import app.Chain;
import app.PuyoI;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

public class unitTestsChain {

    private Chain empty;
    private Chain full;
    private ArrayList<Puyo[][]> customBoards;
    private ArrayList<ArrayList<int[]>> customRecDrop;
    private ArrayList<int[]> fullRecentlyDropped;

    @Before
    public void prepare(){
        empty = new Chain(new Board());
        Puyo[][] full = new Puyo[6][13];
        for (int i = 0; i < 6; i ++){
            for (int j = 0; j < 13; j ++){
                full[i][j] = PuyoI.createGarbage();
            }
        }
        this.full = new Chain(new Board(full));
        appTest at = new appTest();
        ArrayList<String> lines = at.readFile(new File("testPatterns.csv"));
        customBoards = at.readBoards(lines);
        customRecDrop = at.readRecentlyDropped(lines);
        fullRecentlyDropped = new ArrayList<>();
        for (int i = 0; i < 6; i ++){
            for (int j = 0; j < 13; j ++){
                fullRecentlyDropped.add(new int[]{i,j});
            }
        }
    }

    @Test
    public void isPoppingTest(){
        Assert.assertFalse(empty.isPopping(new ArrayList<>()));
        Assert.assertFalse(full.isPopping(fullRecentlyDropped));
        Assert.assertTrue(new Chain(new Board(customBoards.get(1))).isPopping(customRecDrop.get(1)));
    }

    @Test
    public void chainTurnTest(){
        Board b = new Board(customBoards.get(1));
        Board customBoardCopy = b.copyBoard();
        Chain customCopy = new Chain(customBoardCopy);
        Board resultBoard = b.copyBoard();

        resultBoard.removePuyo(4,0);
        resultBoard.removePuyo(3,0);
        resultBoard.removePuyo(3,1);
        resultBoard.removePuyo(3,2);
        resultBoard.cascadePuyo();

        Assert.assertArrayEquals(new int[]{3,0}, customCopy.chainTurn(customRecDrop.get(1)).get(0));
        Assert.assertTrue(full.chainTurn(fullRecentlyDropped).isEmpty());
        Assert.assertTrue(empty.chainTurn(new ArrayList<>()).isEmpty());

        unitTestsBoard utb = new unitTestsBoard();
        utb.assertEqualBoards(resultBoard, customBoardCopy);
    }

    @Test
    public void scoreTest(){
        int[] scores = {0, 36, 3};
        for (int j = 0; j < customBoards.size(); j ++) {
            Chain customCopy = new Chain(new Board(customBoards.get(j)));
            ArrayList<int[]> recentlyDropped = customRecDrop.get(j);
            while(customCopy.isPopping(recentlyDropped)){
                recentlyDropped = customCopy.chainTurn(recentlyDropped);
            }
            Assert.assertEquals(customCopy.score(), scores[j]);
        }
    }
}
