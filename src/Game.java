import java.lang.reflect.Array;
import java.util.ArrayList;

public class Game {
    Player p1;
    ArrayList<int[]> recentlyDropped;

    public Game(){
        p1 = new Player(new Board());
        recentlyDropped = new ArrayList<>();
    }

    public Game(Board b, ArrayList<int[]> startChain){
        p1 = new Player(b);
        recentlyDropped = startChain;
    }
    public void playSinglePlayer(){
        boolean popping = p1.board.chain.isPopping(recentlyDropped);
        while(!p1.board.checkEnd()) {
            if (!popping) {
                Move m = p1.turn();
                popping = p1.board.chain.isPopping(p1.findRecentlyDropped(m));
                recentlyDropped.clear();
                recentlyDropped.addAll(p1.findRecentlyDropped(m));
            }
            else{
                System.out.println((p1.board.chain.chainLength() + 1) + "-Chain!");
                // Implement chaining
                recentlyDropped = p1.board.chain.chainTurn(recentlyDropped);
                popping = p1.board.chain.isPopping(recentlyDropped);
                if (!popping){
                    // Do something with score
                    p1.board.chain.score();
                }
            }
        }
        Output.printBoard(p1.board);
        System.out.println("GAME OVER");
    }

    public static void main(String[] args){
        Game g = new Game();
        g.playSinglePlayer();
    }
}
