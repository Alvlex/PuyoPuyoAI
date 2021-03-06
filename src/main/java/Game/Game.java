package Game;

import Strategies.HumanStrategy;
import Strategies.Strategy;
import Strategies.pms.PMS;
import Strategies.tms.TMS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private Scanner x = new Scanner(System.in);
    public Player[] players;
    private ArrayList<Coordinate>[] recentlyDropped;
    private Output output;
    private int turn = 0;

    public Game(Strategy[] strategies){
        int randSeed = new Random().nextInt();
        setup(strategies, new int[]{randSeed,randSeed});
    }

    public Game(Strategy[] strategies, int randomSeed){
        setup(strategies, new int[]{randomSeed,randomSeed});
    }

    private void setup(Strategy[] strategies, int[] randomSeeds){
        Board[] b = new Board[strategies.length];
        recentlyDropped = new ArrayList[2];
        for (int i = 0; i < strategies.length; i ++){
            recentlyDropped[i] = new ArrayList<>();
            b[i] = new Board();
        }
        players = new Player[b.length];
        output = new Output(b);
        for (int i = 0; i < players.length; i ++) {
            players[i] = new Player(b[i], 4, strategies[i], randomSeeds[i]);
            if (strategies[i] instanceof HumanStrategy){
                ((HumanStrategy) strategies[i]).updateOutput(output);
            }
        }
    }

    private int playSinglePlayer(int noOfTurns){
        int max = 0;
        String bestChainBoard = null;
        boolean popping = players[0].chain.isPopping(recentlyDropped[0]);
        boolean prevPop;
        while((players[0].board.checkPossibilities() || popping) && turn < noOfTurns) {
            updateTurn();
            System.out.println(output.printCurrentPuyo());
            System.out.println(output.printBoards());
            x.nextLine();
            prevPop = popping;
            popping = singlePlayerHelper(0, popping, new Board());
            if (!prevPop && popping){
                updateTurn();
                bestChainBoard = output.printBoards();
            }
            if (!popping){
                if (!(players[0].s instanceof TMS) || ((TMS) players[0].s).chainMade) {
                    max = Math.max(players[0].chain.chainLength(), max);
                }
                players[0].chain.resetChain();
            }
        }
        if (max >= 12)
            System.out.println(bestChainBoard);
//        updateTurn();
//        System.out.println(output.printBoards());
//        System.out.println("GAME OVER");
        return max;
    }

    private boolean singlePlayerHelper(int playerNo, boolean popping, Board oppBoard){
        if (!popping) {
            Move m = players[playerNo].turn(oppBoard);
            popping = players[playerNo].chain.isPopping(players[playerNo].findRecentlyDropped(m));
            recentlyDropped[playerNo].clear();
            recentlyDropped[playerNo].addAll(players[playerNo].findRecentlyDropped(m));
            if (!popping)
                players[playerNo].garbage.dropGarbage();
        } else {
//            System.out.println("Player " + (playerNo + 1) + " has a " + (players[playerNo].chain.chainLength() + 1) + "-Chain!");
            // Implement chaining
            recentlyDropped[playerNo] = players[playerNo].chain.chainTurn(recentlyDropped[playerNo]);
            popping = players[playerNo].chain.isPopping(recentlyDropped[playerNo]);
        }
        return popping;
    }

    private int playMultiPlayer(int noOfTurns){
        boolean[] popping = new boolean[2];
        popping[0] = players[0].chain.isPopping(recentlyDropped[0]);
        popping[1] = players[1].chain.isPopping(recentlyDropped[1]);
        while((players[0].board.checkPossibilities() || popping[0]) && (players[1].board.checkPossibilities() || popping[1]) && turn < noOfTurns) {
            updateTurn();
            if (popping[0]) {
                System.out.println(output.printBoards());
                x.nextLine();
            }

            int[] scores = new int[2];
            for (int playerNo = 0; playerNo < players.length; playerNo ++) {
                popping[playerNo] = singlePlayerHelper(playerNo, popping[playerNo], output.boards[1 - playerNo]);
                if (!popping[playerNo]) {
                    scores[playerNo] = players[playerNo].chain.score();
                    players[playerNo].chain.resetChain();
                }
            }
            for (int playerNo = 0; playerNo < players.length; playerNo ++){
                if (scores[playerNo] > 0) {
                    scores[playerNo] = players[playerNo].garbage.removeGarbage(scores[playerNo]);
                    players[1 - playerNo].garbage.makeGarbage(scores[playerNo]);
                }
            }
        }
        updateTurn();
//        System.out.println(output.printBoards());
//        System.out.println("GAME OVER");
        if (!(players[0].board.checkPossibilities() || popping[0]) && !(players[1].board.checkPossibilities() || popping[1]))
            return -1;
        else if (!(players[0].board.checkPossibilities() || popping[0]))
            return 0;
        else if (!(players[1].board.checkPossibilities() || popping[1]))
            return 1;
        else
            return 2;
    }

    private void updateTurn(){
        for (int i = 0; i < players.length; i ++){
            output.updateCurrentPuyo(players[i].currentPuyo, i);
            output.updateBoard(players[i].board, i);
            output.updateMoves(new Move(), i);
        }
        turn ++;
    }

    public int play(){
        if (players.length == 1){
            return playSinglePlayer(Integer.MAX_VALUE);
        }
        else{
            return playMultiPlayer(Integer.MAX_VALUE);
        }
    }

    public int play(int noOfTurns){
        if (players.length == 1) {
            return playSinglePlayer(noOfTurns);
        } else {
            playMultiPlayer(noOfTurns);
            return 0;
        }
    }

    public static void main(String[] args){
//        Game g = new Game(new Strategy[]{new PMS(3, 16, 320)});
//        Game g = new Game(new Strategy[]{new PMS(2,8,300), new PMS(3, 8, 220)});
//        int[] gameWins = new int[2];
//        for (int i = 5; i < 10; i ++) {
//            System.out.println("Game number " + i);
//            Game g = new Game(new Strategy[]{new PMS(4, 4, 260), new HumanStrategy(1)}, i);
//            gameWins[g.play()] ++;
//        }
//        System.out.println(Arrays.toString(gameWins));
//        //Game g = new Game(new Strategy[]{new HumanStrategy(0), new TMS(new PMS(3, 8, 220))});
//        //g.play();
        Game g = new Game(new Strategy[]{new HumanStrategy(0),new TMS(new PMS(3, 12, 160))}, 0);
        g.play();
    }
}
