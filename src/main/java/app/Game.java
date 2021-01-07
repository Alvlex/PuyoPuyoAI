package app;

import AI.HumanStrategy;
import AI.RandomStrategy;
import AI.Strategy;
import AI.pms.PMS;
import AI.tms.TMS;

import java.util.ArrayList;

public class Game {
    public Player[] players;
    private ArrayList<Coordinate>[] recentlyDropped;
    private Output output;
    private int turn = 0;

    public Game(Strategy[] strategies){
        setup(strategies, new int[]{0,0});
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
        }
    }

    private int playSinglePlayer(int noOfTurns){
        int max = 0;
        boolean popping = players[0].chain.isPopping(recentlyDropped[0]);
        while((players[0].board.checkPossibilities() || popping) && turn < noOfTurns && max == 0) {
            updateTurn();
            if (popping && false) {
                System.out.println(output.printBoards());
            }
            popping = singlePlayerHelper(0, popping, new Board());
            if (!popping){
                max = Math.max(players[0].chain.chainLength(), max);
                players[0].chain.resetChain();
            }
        }
        updateTurn();
        if (false) {
            System.out.println(output.printBoards());
            System.out.println("GAME OVER");
        }
        return max;
    }

    private boolean singlePlayerHelper(int playerNo, boolean popping, Board oppBoard){
        if (!popping) {
            Move m = players[playerNo].turn(oppBoard);
            popping = players[playerNo].chain.isPopping(players[playerNo].findRecentlyDropped(m));
            recentlyDropped[playerNo].clear();
            recentlyDropped[playerNo].addAll(players[playerNo].findRecentlyDropped(m));
            players[playerNo].garbage.dropGarbage();
        } else {
//            System.out.println("Player " + (playerNo + 1) + " has a " + (players[playerNo].chain.chainLength() + 1) + "-Chain!");
            // Implement chaining
            recentlyDropped[playerNo] = players[playerNo].chain.chainTurn(recentlyDropped[playerNo]);
            popping = players[playerNo].chain.isPopping(recentlyDropped[playerNo]);
        }
        return popping;
    }

    private void playMultiPlayer(int noOfTurns){
        boolean[] popping = new boolean[2];
        popping[0] = players[0].chain.isPopping(recentlyDropped[0]);
        popping[1] = players[1].chain.isPopping(recentlyDropped[1]);
        while(players[0].board.checkPossibilities() && players[1].board.checkPossibilities() && turn < noOfTurns) {
            updateTurn();
            if (popping[0] && popping[1]) {
                System.out.println(output.printBoards());
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
        System.out.println(output.printBoards());
        System.out.println("GAME OVER");
    }

    private void updateTurn(){
        for (int i = 0; i < players.length; i ++){
            output.updateCurrentPuyo(players[i].currentPuyo, i);
            output.updateBoard(players[i].board, i);
            output.updateMoves(new Move(), i);
        }
        turn ++;
    }

    private void play(){
        if (players.length == 1){
            playSinglePlayer(Integer.MAX_VALUE);
        }
        else{
            playMultiPlayer(Integer.MAX_VALUE);
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
        Game g = new Game(new Strategy[]{new TMS()});
        g.play();
    }
}
