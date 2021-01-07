package appTest;

import AI.Strategy;
import AI.tms.TMS;
import app.Game;
import app.PuyoI;
import org.junit.Test;

import java.util.HashMap;
import java.util.Random;

public class testsTMS {

    Template pms1 = new Template("PMS1.csv");

    @Test
    public void evaluation(){
        Game g;
        HashMap<String, Integer> chainsUsed = new HashMap<>();
        int[] chainLengths = new int[20];
        int noOfGames = 20;
        for (int i = 0; i < noOfGames; i ++) {
            System.out.println("Game number " + i);
            TMS tms = new TMS();
            g = new Game(new Strategy[]{tms}, i);
            chainLengths[g.play(Integer.MAX_VALUE)] ++;
            chainsUsed.put(tms.getTemplate(), chainsUsed.getOrDefault(tms.getTemplate(), 0) + 1);
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
    }

    @Test
    public void getTimings(){
        TMS tms = new TMS();
        tms.makeMove(pms1.getBoard(), PuyoI.createInitialPuyo(4, new Random(0)));

    }

}
