package appTest;

import AI.Strategy;
import AI.tms.TMS;
import app.Game;
import org.junit.Test;

public class testsTMS {
    @Test
    public void evaluation(){
        TMS tms = new TMS();
        Game g;
        int[] chainLengths = new int[20];
        int noOfGames = 100;
        for (int i = 0; i < noOfGames; i ++) {
            System.out.println("Game number " + i);
            g = new Game(new Strategy[]{tms});
            chainLengths[g.play(Integer.MAX_VALUE)] ++;
        }
        int avgChains = 0;
        for(int i = 0; i < chainLengths.length; i++) {
            System.out.print((i) + ":" + "\t");
            avgChains += i * chainLengths[i];
            System.out.println(chainLengths[i]);
        }
        System.out.println("Average chain: " + (double) avgChains / noOfGames);
        tms.printStats();
    }

}
