package aiTest;

import Strategies.Strategy;
import Strategies.pms.PMS;
import Strategies.pms.Node;
import Game.Board;
import Game.Game;
import Game.Puyo;
import Game.PuyoI;
import appTest.Template;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class unitTestsPMS {

    private Template PMS1 = new Template("PMS1.csv");
    private Template full = new Template("full.csv");
    private Template empty = new Template("empty.csv");
    private Random x = new Random(0);
    private Puyo[][] next3Pairs = {PuyoI.create2Puyo(1, x), PuyoI.create2Puyo(1, x), PuyoI.create2Puyo(1, x)};
    private PMS PMS2;
    private PMS PMS3;
    private PMS PMS4;

    @Before
    public void prepare(){
        PMS2 = new PMS(2, 12, 60);
        PMS3 = new PMS(3, 4, Integer.MAX_VALUE, 7);
        PMS4 = new PMS(4, 4, 260);
    }

    @Test
    public void testHeuristics() throws IOException{
        int depth = 4;
        FileWriter myWriter = new FileWriter("PMS" + depth + "heuristics2.csv");
        for (int randomSeed = 0; randomSeed < 10; randomSeed ++) {
            for (int spaceLeft = 0; spaceLeft <= 12; spaceLeft += 4) {
                for (int garbageSatisfy = 160; garbageSatisfy <= 180; garbageSatisfy += 20) {
                    PMS pms = new PMS(depth, spaceLeft, garbageSatisfy);
                    Game g = new Game(new Strategy[]{pms}, randomSeed);
                    int tempChain = g.play(Integer.MAX_VALUE);
                    myWriter.write(randomSeed + "," + spaceLeft + "," + garbageSatisfy + "," + tempChain + '\n');
                    myWriter.flush();
                }
            }
        }
        myWriter.close();
    }

    @Test
    public void moveCheck(){
        Board b = PMS1.getBoard();
        PMS3.makeMove(b, next3Pairs, new Board());
        Assert.assertEquals(b.getPuyo(4,0).getColour(), "RED");
    }

    @Test
    public void testPMS(){
//        PMS oldPms3 = new PMS(3, 4, 220);
//        PMS chainPms3 = new PMS(3, 4, Integer.MAX_VALUE, 7);
//        evaluation(oldPms3, 1000, "OldPMS3");
//        evaluation(chainPms3, 1000, "chainPMS3");
        evaluation(PMS3, 1000, "Test");
    }

    @Test
    public void testPMS4(){
        evaluation(PMS4, 350, "EvaluationOutput");
    }

    public static void evaluation(PMS pms, int noOfGames, String fileName){
        Game g;
        int[] chainLengths = new int[20];
        for (int i = 0; i < noOfGames; i ++) {
            System.out.println("Game number " + i);
            g = new Game(new Strategy[]{pms}, i);
            chainLengths[g.play(Integer.MAX_VALUE)] ++;
        }
        int avgChains = 0;
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < chainLengths.length; i++) {
            output.append(i).append(":").append("\t");
            avgChains += i * chainLengths[i];
            output.append(chainLengths[i]).append("\n");
        }
        output.append("Average chain: ").append((double) avgChains / noOfGames).append("\n");
        ArrayList<Long> times = pms.getTimes();
        long[] sorted = times.stream().mapToLong(i -> i).toArray();
        Arrays.sort(sorted);
        output.append(Arrays.toString(sorted));
        try {
            FileWriter fw = new FileWriter(fileName + ".txt");
            fw.write(output.toString());
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getMoveMetrics(PMS pms, Board b, int depth){
        System.out.println("Depth " + depth + " PMS");
        pms.makeMove(b, next3Pairs, new Board());
        System.out.println(Arrays.toString(pms.getTimes().toArray()));
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
        Assert.assertEquals(PMS3.generatePoss(new Node(b), PuyoI.create2Puyo(1, x)).size(), 0);
    }

    @Test
    public void generatePossEmptyCheck(){
        Board b = empty.getBoard();
        Assert.assertEquals(PMS3.generatePoss(new Node(b), PuyoI.create2Puyo(1, x)).size(), 22);
    }
}
