package app;

import AI.HumanStrategy;
import AI.RandomStrategy;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private Player[] players;
    private ArrayList<int[]>[] recentlyDropped;
    Output output;
    int turn = 0;

    private Game(int noOfBoards){
        Board[] b = new Board[noOfBoards];
        ArrayList[] al = new ArrayList[noOfBoards];
        for (int i = 0; i < noOfBoards; i ++){
            b[i] = new Board();
            al[i] = new ArrayList<int[]>();
        }
        setup(b, al);
    }

    public Game(Board b, ArrayList<int[]> startChain){
        setup(new Board[]{b}, new ArrayList[]{startChain});
    }

    public Game(Board[] b, ArrayList<int[]>[] startChain){
        setup(b, startChain);
    }

    private void setup(Board[] b, ArrayList<int[]>[] startChain){
        players = new Player[b.length];
        output = new Output(b);
        for (int i = 0; i < players.length; i ++) {
            players[i] = new Player(b[i], i, output);
        }
        recentlyDropped = startChain;
    }

    int playSinglePlayer(int noOfTurns){
        int max = 0;
        boolean popping = players[0].chain.isPopping(recentlyDropped[0]);
        while(players[0].board.checkPossibilities() && turn < noOfTurns) {
            if (popping) {
                System.out.println(output.printBoards());
            }
            popping = singlePlayerHelper(0, popping);
            if (!popping){
                max = Math.max(players[0].chain.score(), max);
                players[0].chain.resetChain();
            }
            updateTurn();
        }
        System.out.println(output.printBoards());
        System.out.println("GAME OVER");
        return max;
    }

    private boolean singlePlayerHelper(int playerNo, boolean popping){
        if (!popping) {
            Move m = players[playerNo].turn(new HumanStrategy(output, playerNo));
            popping = players[playerNo].chain.isPopping(players[playerNo].findRecentlyDropped(m));
            recentlyDropped[playerNo].clear();
            recentlyDropped[playerNo].addAll(players[playerNo].findRecentlyDropped(m));
            players[playerNo].garbage.dropGarbage();
        } else {
            System.out.println("Player " + (playerNo + 1) + " has a " + (players[playerNo].chain.chainLength() + 1) + "-Chain!");
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
            if (popping[0] && popping[1]) {
                System.out.println(output.printBoards());
            }
            int[] scores = new int[2];
            for (int playerNo = 0; playerNo < players.length; playerNo ++) {
                popping[playerNo] = singlePlayerHelper(playerNo, popping[playerNo]);
                if (!popping[playerNo]) {
                    scores[playerNo] = players[playerNo].chain.score();
                    players[playerNo].chain.resetChain();
                }
            }
            for (int playerNo = 0; playerNo < players.length; playerNo ++){
                if (scores[playerNo] > 0) {
                    scores[playerNo] = players[playerNo].garbage.removeGarbage(scores[playerNo]);
                    players[playerNo].garbage.makeGarbage(scores[1 - playerNo]);
                }
            }
            updateTurn();
        }
        System.out.println(output.printBoards());
        System.out.println("GAME OVER");
    }

    private void updateTurn(){
        for (int i = 0; i < players.length; i ++){
            output.updateCurrentPuyo(players[i].currentPuyo, i);
            output.updateBoard(players[i].board, i);
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
        Game g = new Game(2);
        g.play();
    }
}
