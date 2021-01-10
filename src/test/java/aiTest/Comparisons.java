package aiTest;

import AI.HumanStrategy;
import AI.Strategy;
import AI.pms.PMS;
import AI.tms.TMS;
import app.Game;
import app.Output;
import org.junit.Test;

public class Comparisons {

    @Test
    public void prepare8Chain(){
        Game tms = new Game(new Strategy[]{new TMS()}, 0);
        Game pms = new Game(new Strategy[]{new PMS(3, 8, Integer.MAX_VALUE, 8)}, 0);
        Game human = new Game(new Strategy[]{new HumanStrategy(0)}, 0);
        System.out.println(tms.play(Integer.MAX_VALUE));
        System.out.println(pms.play(Integer.MAX_VALUE));
        System.out.println(human.play(Integer.MAX_VALUE));
    }
}
