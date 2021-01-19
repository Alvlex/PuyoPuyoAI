package aiTest;

import AI.Strategy;
import AI.pms.PMS;
import AI.pms.Node;
import app.Board;
import app.Game;
import app.Puyo;
import app.PuyoI;
import appTest.Template;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        PMS2 = new PMS(2, 8, 300);
        PMS3 = new PMS(3, 4, 220);
        PMS4 = new PMS(4, 16, 400);
    }

    @Test
    public void testHeuristics(){
        int depth = 2;
        HashMap<Integer, HashMap<Integer, Integer>> bestHeuristics = new HashMap<>();
        for (int randomSeed = 0; randomSeed < 10; randomSeed ++) {
            ArrayList<Integer> bestSpaceLeft = new ArrayList<>();
            ArrayList<Integer> bestGarbageSatisfy = new ArrayList<>();
            int highestChain = 0;
            for (int spaceLeft = 0; spaceLeft <= 32; spaceLeft += 4) {
                for (int garbageSatisfy = 100; garbageSatisfy <= 400; garbageSatisfy += 10) {
                    PMS pms = new PMS(depth, spaceLeft, garbageSatisfy);
                    Game g = new Game(new Strategy[]{pms}, randomSeed);
                    int tempChain = g.play(Integer.MAX_VALUE);
                    if (tempChain > highestChain) {
                        highestChain = tempChain;
                        bestSpaceLeft.clear();
                        bestGarbageSatisfy.clear();
                        bestSpaceLeft.add(spaceLeft);
                        bestGarbageSatisfy.add(garbageSatisfy);
                    } else if (tempChain == highestChain) {
                        bestGarbageSatisfy.add(garbageSatisfy);
                        bestSpaceLeft.add(spaceLeft);
                    }
                }
            }
            System.out.println("Highest Chain: " + highestChain);
            for (int i = 0; i < bestGarbageSatisfy.size(); i ++) {
                if (!bestHeuristics.containsKey(bestSpaceLeft.get(i))){
                    bestHeuristics.put(bestSpaceLeft.get(i), new HashMap<>());
                    bestHeuristics.get(bestSpaceLeft.get(i)).put(bestGarbageSatisfy.get(i), 1);
                }
                else if (!bestHeuristics.get(bestSpaceLeft.get(i)).containsKey(bestGarbageSatisfy.get(i))){
                    bestHeuristics.get(bestSpaceLeft.get(i)).put(bestGarbageSatisfy.get(i), 1);
                }
                else{
                    bestHeuristics.get(bestSpaceLeft.get(i)).put(bestGarbageSatisfy.get(i), bestHeuristics.get(bestSpaceLeft.get(i)).get(bestGarbageSatisfy.get(i)) + 1);
                }
            }
        }
        int highestCount = 0;
        ArrayList<Integer> bestSpaceList = new ArrayList<>();
        ArrayList<Integer> bestGarbageList = new ArrayList<>();
        for (int bestSpace: bestHeuristics.keySet()){
            for (int bestGarbage: bestHeuristics.get(bestSpace).keySet()){
                if (bestHeuristics.get(bestSpace).get(bestGarbage) > highestCount){
                    highestCount = bestHeuristics.get(bestSpace).get(bestGarbage);
                    bestSpaceList.clear();
                    bestGarbageList.clear();
                    bestSpaceList.add(bestSpace);
                    bestGarbageList.add(bestGarbage);
                }
                else if (bestHeuristics.get(bestSpace).get(bestGarbage) == highestCount){
                    bestGarbageList.add(bestGarbage);
                    bestSpaceList.add(bestSpace);
                }
            }
        }
        System.out.println("Highest Count: " + highestCount);
        for (int i = 0; i < bestGarbageList.size(); i ++){
            System.out.println(bestSpaceList.get(i) + ", " + bestGarbageList.get(i));
        }
    }

    @Test
    public void moveCheck(){
        Board b = PMS1.getBoard();
        PMS3.makeMove(b, next3Pairs, new Board());
        Assert.assertEquals(b.getPuyo(4,0).getColour(), "RED");
    }

    @Test
    public void evaluation(){
        Game g;
        int[] chainLengths = new int[20];
        int noOfGames = 1000;
        for (int i = 0; i < noOfGames; i ++) {
            System.out.println("Game number " + i);
            g = new Game(new Strategy[]{PMS3}, i);
            chainLengths[g.play(Integer.MAX_VALUE)] ++;
        }
        int avgChains = 0;
        for(int i = 0; i < chainLengths.length; i++) {
            System.out.print((i) + ":" + "\t");
            avgChains += i * chainLengths[i];
            System.out.println(chainLengths[i]);
        }
        System.out.println("Average chain: " + (double) avgChains / noOfGames);
        HashMap<Integer, Integer> times = PMS3.printStats();
        ArrayList<Integer> manipulated = new ArrayList<>();
        for (int key: times.keySet()){
            for (int i = 0; i < times.get(key); i ++){
                manipulated.add(key);
            }
        }
        int[] sorted = manipulated.stream().mapToInt(i -> i).toArray();
        Arrays.sort(sorted);
        System.out.println(Arrays.toString(sorted));
    }

    private void getMoveMetrics(PMS pms, Board b, int depth){
        System.out.println("Depth " + depth + " PMS");
        pms.makeMove(b, next3Pairs, new Board());
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
        Assert.assertEquals(PMS3.generatePoss(new Node(b), PuyoI.create2Puyo(1, x)).size(), 0);
    }

    @Test
    public void generatePossEmptyCheck(){
        Board b = empty.getBoard();
        Assert.assertEquals(PMS3.generatePoss(new Node(b), PuyoI.create2Puyo(1, x)).size(), 22);
    }
}
