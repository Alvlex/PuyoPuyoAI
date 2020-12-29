package appTest;

import AI.Strategy;
import AI.pms.PMS;
import AI.pms.Node;
import app.Board;
import app.Game;
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
    private PMS PMS2;
    private PMS PMS3;
    private PMS PMS4;

    @Before
    public void prepare(){
        PMS2 = new PMS(2, 32, 120);
        PMS3 = new PMS(3, 24, 270);
        PMS4 = new PMS(4, 16, 400);
    }

    @Test
    public void moveCheck(){
        Board b = PMS1.getBoard();
        PMS3.makeMove(b, next3Pairs);
        Assert.assertEquals(b.getPuyo(4,0).getColour(), "RED");
    }

    @Test
    public void evaluation(){
        Game g;
        int[] chainLengths = new int[20];
        int noOfGames = 100;
        for (int i = 0; i < noOfGames; i ++) {
            System.out.println("Game number " + i);
            g = new Game(new Strategy[]{PMS2});
            chainLengths[g.play(Integer.MAX_VALUE)] ++;
        }
        int avgChains = 0;
        for(int i = 0; i < chainLengths.length; i++) {
            System.out.print((i) + ":" + "\t");
            avgChains += i * chainLengths[i];
            System.out.println(chainLengths[i]);
        }
        System.out.println("Average chain: " + (double) avgChains / noOfGames);
        PMS2.printStats();
    }

    private void getMoveMetrics(PMS pms, Board b, int depth){
        System.out.println("Depth " + depth + " PMS");
        pms.makeMove(b, next3Pairs);
        pms.printStats();
    }

    @Test
    public void timingEmpty(){
        getMoveMetrics(PMS2, empty.getBoard(), 2);
        getMoveMetrics(PMS3, empty.getBoard(), 3);
        getMoveMetrics(PMS4, empty.getBoard(), 4);
    }

    @Test
    public void timingPMS1(){
        getMoveMetrics(PMS2, PMS1.getBoard(), 2);
        getMoveMetrics(PMS3, PMS1.getBoard(), 3);
        getMoveMetrics(PMS4, PMS1.getBoard(), 4);
    }

    @Test
    public void generatePossFullCheck(){
        Board b = full.getBoard();
        Assert.assertEquals(PMS3.generatePoss(new Node(b), PuyoI.create2Puyo(1)).size(), 0);
    }

    @Test
    public void generatePossEmptyCheck(){
        Board b = empty.getBoard();
        Assert.assertEquals(PMS3.generatePoss(new Node(b), PuyoI.create2Puyo(1)).size(), 22);
    }
}
