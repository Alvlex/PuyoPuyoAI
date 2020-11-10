import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private Player[] players;
    private ArrayList<int[]>[] recentlyDropped;
    Output output;

    private Game(int noOfBoards){
        Board[] b = new Board[noOfBoards];
        ArrayList[] al = new ArrayList[noOfBoards];
        for (int i = 0; i < noOfBoards; i ++){
            b[i] = new Board();
            al[i] = new ArrayList<int[]>();
        }
        setup(b, al);
    }

    Game(Board b, ArrayList<int[]> startChain){
        setup(new Board[]{b}, new ArrayList[]{startChain});
    }

    Game(Board[] b, ArrayList<int[]>[] startChain){
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

    void playSinglePlayer(){
        Scanner x = new Scanner(System.in);
        boolean popping = players[0].chain.isPopping(recentlyDropped[0]);
        while(!players[0].board.checkEnd()) {
            if (popping) {
                output.printBoards();
                System.out.println("Press 1 to proceed");
                int input = x.nextInt();
                while (input != 1) {
                    input = x.nextInt();
                }
            }
            popping = singlePlayerHelper(0, popping);
            updateTurn();
        }
        output.printBoards();
        System.out.println("GAME OVER");
    }

    private boolean singlePlayerHelper(int playerNo, boolean popping){
        if (!popping) {
            System.out.println("Player " + (playerNo + 1) + "'s turn!" );
            Move m = players[playerNo].turn();
            popping = players[playerNo].chain.isPopping(players[playerNo].findRecentlyDropped(m));
            recentlyDropped[playerNo].clear();
            recentlyDropped[playerNo].addAll(players[playerNo].findRecentlyDropped(m));
        } else {
            System.out.println("Player " + (playerNo + 1) + " has a " + (players[playerNo].chain.chainLength() + 1) + "-Chain!");
            // Implement chaining
            recentlyDropped[playerNo] = players[playerNo].chain.chainTurn(recentlyDropped[playerNo]);
            popping = players[playerNo].chain.isPopping(recentlyDropped[playerNo]);
            if (!popping) {
                // Do something with score
                players[playerNo].chain.score();
            }
        }
        return popping;
    }

    private void playMultiPlayer(){
        Scanner x = new Scanner(System.in);
        boolean[] popping = new boolean[2];
        popping[0] = players[0].chain.isPopping(recentlyDropped[0]);
        popping[1] = players[1].chain.isPopping(recentlyDropped[1]);
        while(!players[0].board.checkEnd() && !players[1].board.checkEnd()) {
            if (popping[0] && popping[1]) {
                output.printCurrentPuyo();
                output.printBoards();
                System.out.println("Press 1 to proceed");
                int input = x.nextInt();
                while (input != 1) {
                    input = x.nextInt();
                }
            }
            for (int playerNo = 0; playerNo < players.length; playerNo ++) {
                popping[playerNo] = singlePlayerHelper(playerNo, popping[playerNo]);
            }
            updateTurn();
        }
        output.printBoards();
        System.out.println("GAME OVER");
    }

    private void updateTurn(){
        for (int i = 0; i < players.length; i ++){
            output.updateCurrentPuyo(players[i].currentPuyo, i);
            output.updateBoard(players[i].board, i);
        }
    }

    void play(){
        if (players.length == 1){
            playSinglePlayer();
        }
        else{
            playMultiPlayer();
        }
    }

    public static void main(String[] args){
        Game g = new Game(2);
        g.play();
    }
}
