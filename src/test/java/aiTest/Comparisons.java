package aiTest;

import AI.HumanStrategy;
import AI.Strategy;
import AI.pms.PMS;
import AI.tms.TMS;
import app.*;
import org.junit.Test;

import java.util.Arrays;

public class Comparisons {

    PMS pms = new PMS(4, 8, Integer.MAX_VALUE, 8);

    @Test
    public void prepare8Chain(){
        int[] sorted = new int[100];
        for (int i = 0; i < 100; i ++) {
            System.out.println("Game number " + i);
            int noOfTurns = get8Chain(new Player(new Board(), 4, pms, i));
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
                    break;
                }
            }
        }
        return turn;
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
    public void compareAI(){
        int[] results = new int[3];
        for (int i = 0; i < 100; i ++){
            System.out.println("Game number " + i);
            Game g = new Game(new Strategy[]{new PMS(3,8,220), new PMS(2, 8, 300)}, i);
            results[g.play() + 1] ++;
        }
        for (int i = 0; i < 3; i ++){
            System.out.println(results[i]);
        }
    }
}
