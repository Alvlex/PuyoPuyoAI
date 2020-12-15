package appTest;

import AI.pms.PMS;
import AI.pms.Node;
import app.Board;
import app.Puyo;
import app.PuyoI;
import org.junit.Assert;
import org.junit.Test;

public class unitTestsPMS {

    private Template PMS1 = new Template("PMS1.csv");
    private Template full = new Template("full.csv");
    private Template empty = new Template("empty.csv");
    private PMS PMS = new PMS();
    private Puyo[][] next3Pairs = {PuyoI.create2Puyo(1), PuyoI.create2Puyo(1), PuyoI.create2Puyo(1)};


    @Test
    public void moveCheck(){
        Board b = PMS1.getBoard();
        PMS.makeMove(b, next3Pairs);
        Assert.assertEquals(b.getPuyo(4,0).getColour(), "RED");
    }

    private int findDepth(Node n){
        int currentDepth = 0;
        for (Node n1: n.getChildren()){
            currentDepth = Math.max(1 + findDepth(n1), currentDepth);
        }
        return currentDepth;
    }

    @Test
    public void treeEmptyCheck(){
        Node root = new Node(empty.getBoard());
        PMS.generateTree(root, next3Pairs, new Board());
        Assert.assertEquals(findDepth(root), 3);
    }

    @Test
    public void treeFullCheck(){
        Node root = new Node(full.getBoard());
        PMS.generateTree(root, next3Pairs, new Board());
        Assert.assertEquals(findDepth(root), 0);
    }

    @Test
    public void generatePossFullCheck(){
        Assert.assertEquals(PMS.generatePoss(full.getBoard(), PuyoI.create2Puyo(1)).size(), 0);
    }

    @Test
    public void generatePossEmptyCheck(){
        Assert.assertEquals(PMS.generatePoss(empty.getBoard(), PuyoI.create2Puyo(1)).size(), 22);
    }
}
