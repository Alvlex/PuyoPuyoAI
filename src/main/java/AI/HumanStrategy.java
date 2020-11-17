package AI;

import app.Board;
import app.Move;
import app.Output;
import app.Puyo;

import java.util.Scanner;

public class HumanStrategy implements Strategy{
    private Output output;
    private int playerNo;

    public HumanStrategy(Output output, int playerNo){
        this.output = output;
        this.playerNo = playerNo;
    }

    public HumanStrategy(int playerNo){
        this.playerNo = playerNo;
    }

    public void updateOutput(Output output){
        this.output = output;
    }
    @Override
    public Move makeMove(Board b, Puyo[][] currentPuyo) {
        System.out.println("Player " + (playerNo + 1) + "'s turn!" );
        Move move = new Move();
        Scanner x = new Scanner(System.in);
        int input = 0;
        while(true) {
            while (input != 5) {
                output.updateMoves(move, playerNo);
                System.out.println(output.printCurrentPuyo());
                System.out.println(output.printBoards());
                System.out.println("Pick an action:\n(1) Move left\n(2) Move right\n(3) Rotate clockwise\n" +
                        "(4) Rotate counter-clockwise\n(5) End turn");
                input = x.nextInt();
                switch (input) {
                    case 1:
                        move.left();
                        break;
                    case 2:
                        move.right();
                        break;
                    case 3:
                        move.rotClock();
                        break;
                    case 4:
                        move.rotAnti();
                        break;
                }
            }
            input = 0;
            if (b.dropPuyo(currentPuyo[0], move)) break;
            else{
                System.out.println("Can't place puyo there!");
            }
        }
        return move;
    }
}
