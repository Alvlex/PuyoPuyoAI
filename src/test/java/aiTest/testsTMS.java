package aiTest;

import AI.Strategy;
import AI.tms.TMS;
import app.*;
import appTest.Template;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Random;

public class testsTMS {

    Template pms1 = new Template("PMS1.csv");
    Template tms1 = new Template("TMS1.csv");
    Puyo[][] currentPuyo = new Puyo[3][2];

    @Before
    public void prepare(){
        for (int i = 0; i < 3; i ++)
            for (int j = 0; j < 2; j ++)
                currentPuyo[i][j] = new Puyo(Colour.YELLOW);
    }

    @Test
    public void checkCompleteTemplate(){
        TMS tms = new TMS();
        currentPuyo[0][0] = new Puyo(Colour.BLUE);
        Move m = tms.makeMove(tms1.getBoard(), currentPuyo);
        System.out.println(tms.getTemplate());
        for (int i = 0; i < 2; i ++){
            Assert.assertTrue(sameCoord(m.getCoord()[i], new Coordinate(5, 1 - i)));
        }
    }

    private boolean sameCoord(Coordinate coord1, Coordinate coord2){
        System.out.println(coord1.getX() + ", " + coord2.getX());
        System.out.println(coord1.getY() + ", " + coord2.getY());
        return coord1.getX() == coord2.getX() && coord1.getY() == coord2.getY();
    }

    @Test
    public void evaluation(){
        Game g;
        HashMap<String, Integer> chainsUsed = new HashMap<>();
        int[] chainLengths = new int[20];
        int noOfGames = 100;
        double totalTime = 0;
        for (int i = 0; i < noOfGames; i ++) {
            System.out.println("Game number " + i);
            TMS tms = new TMS();
            g = new Game(new Strategy[]{tms}, i);
            chainLengths[g.play(Integer.MAX_VALUE)] ++;
            chainsUsed.put(tms.getTemplate(), chainsUsed.getOrDefault(tms.getTemplate(), 0) + 1);
            totalTime += tms.getAverageTime();
        }
        int avgChains = 0;
        for(int i = 0; i < chainLengths.length; i++) {
            System.out.print((i) + ":" + "\t");
            avgChains += i * chainLengths[i];
            System.out.println(chainLengths[i]);
        }
        System.out.println("Average chain: " + (double) avgChains / noOfGames);
        for (String chain: chainsUsed.keySet()){
            System.out.println(chain + ": " + chainsUsed.get(chain));
        }
        System.out.println("Average Time: " + totalTime / noOfGames);
    }

    @Test
    public void getTimings(){
        TMS tms = new TMS();
        tms.makeMove(pms1.getBoard(), PuyoI.createInitialPuyo(4, new Random(0)));

    }

}
