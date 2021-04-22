package aiTest;

import Strategies.Strategy;
import Strategies.pms.PMS;
import Game.*;
import Strategies.tms.TMS;
import org.junit.Test;

import java.util.Arrays;

public class Comparisons {

    PMS pms = new PMS(3, 12, Integer.MAX_VALUE, 8);
    TMS tms = new TMS(pms);

    @Test
    public void prepare8Chain(){
        int[] sorted = new int[100];
        for (int i = 0; i < 100; i ++) {
            System.out.println("Game number " + i);
            int noOfTurns = get8Chain(new Player(new Board(), 4, tms, i));
            sorted[i] = noOfTurns;
        }
        Arrays.sort(sorted);
        System.out.println(Arrays.toString(sorted));
    }

    private int get8Chain(Player p){
        int turn = 0;
        boolean popping = false;
        while(p.board.checkPossibilities() || popping) {
            popping = singlePlayerHelper(p, popping);
            turn ++;
            if (!popping){
                p.chain.resetChain();
            }
            else{
                Chain c = new Chain(p.board.copyBoard());
                c.runChain(p.board.findAllPuyo());
                if (c.chainLength() >= 8){
                    return turn;
                }
            }
        }
        return -1;
    }

    private boolean singlePlayerHelper(Player p, boolean popping){
        if (!popping) {
            Move m = p.turn(new Board());
            popping = p.chain.isPopping(p.findRecentlyDropped(m));
        } else {
//            System.out.println("Player " + (playerNo + 1) + " has a " + (players[playerNo].chain.chainLength() + 1) + "-Chain!");
            // Implement chaining
            popping = p.chain.isPopping(p.chain.chainTurn(p.board.findAllPuyo()));
        }
        return popping;
    }

    @Test
    public void compareAIWrap(){
        compareAI(new PMS(4, 4, 260), new PMS(3, 12, 160));
    }

    public void compareAI(Strategy strat1, Strategy strat2){
        int[] results = new int[3];
        for (int i = 0; i < 100; i ++){
            System.out.println("Game number " + i);
            Game g = new Game(new Strategy[]{strat1, new TMS()});
            //g.play() returns -1 for draw, 0 for 1st player lose and 1 for second player lose.
            // Returns 2 otherwise.
            results[g.play() + 1] ++;
        }
        for (int i = 0; i < 3; i ++){
            System.out.println(results[i]);
        }
    }
}
