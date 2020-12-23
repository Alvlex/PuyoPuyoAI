package appTest;

import AI.pms.PMS;
import AI.pms.Node;
import app.Board;
import app.Puyo;
import app.PuyoI;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class unitTestsPMS {

    private Template PMS1 = new Template("PMS1.csv");
    private Template full = new Template("full.csv");
    private Template empty = new Template("empty.csv");
    private Puyo[][] next3Pairs = {PuyoI.create2Puyo(1), PuyoI.create2Puyo(1), PuyoI.create2Puyo(1)};
    private PMS PMS;
    private PMS PMS4;


    @Before
    public void prepare(){
        PMS = new PMS();
        PMS4 = new PMS(true);
    }

    @Test
    public void moveCheck(){
        Board b = PMS1.getBoard();
        PMS.makeMove(b, next3Pairs);
        Assert.assertEquals(b.getPuyo(4,0).getColour(), "RED");
    }

//    @Test
//    public void treeEmptyCheck(){
//        PMS.recursiveTree();
//        Assert.assertEquals(PMS.findDepth(), 3);
//    }

//    @Test
//    public void runPMS10Turns(){
//        PMS PMS = getPMS(empty.getBoard());
//        for (int i = 0; i < 10; i ++){
//            PMS.makeMove(PuyoI.create2Puyo(1), new Board());
//        }
//    }

//    @Test
//    public void treeDepth4EmptyCheck(){
//        PMS PMS = getPMS(empty.getBoard(), true);
//        PMS.recursiveTree();
//        Assert.assertEquals(PMS.findDepth(), 4);
//    }

//    @Test
//    public void treeFullCheck(){
//        PMS PMS = getPMS(full.getBoard());
//        PMS.recursiveTree();
//        Assert.assertEquals(PMS.findDepth(), 0);
//    }

    @Test
    public void generatePossFullCheck(){
        Board b = full.getBoard();
        Assert.assertEquals(PMS.generatePoss(new Node(b), PuyoI.create2Puyo(1)).size(), 0);
    }

    @Test
    public void generatePossEmptyCheck(){
        Board b = empty.getBoard();
        Assert.assertEquals(PMS.generatePoss(new Node(b), PuyoI.create2Puyo(1)).size(), 22);
    }
}
