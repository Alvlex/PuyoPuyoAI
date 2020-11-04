import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Player {
    Puyo[][] currentPuyo;
    Board board;

    public Player(Board g){
        currentPuyo = Puyo.createInitialPuyo();
        board = g;
    }

    private void getNextPuyo(){
        currentPuyo[0] = currentPuyo[1];
        currentPuyo[1] = currentPuyo[2];
        currentPuyo[2] = Puyo.create2Puyo();
    }

    Move turn(){
        Move move = new Move();
        getNextPuyo();
        Scanner x = new Scanner(System.in);
        int input = 0;
        while(true) {
            while (input != 5) {
                Output.printCurrentPuyo(currentPuyo, move);
                Output.printBoard(board);
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
            if (board.dropPuyo(currentPuyo[0], move)) break;
            else{
                System.out.println("Can't place puyo there!");
            }
        }
        return move;
    }

    List<int[]> findRecentlyDropped(Move m){
        int[][] coords = m.getCoord();
        ArrayList<int[]> result = new ArrayList<>();
        if (coords[0][0] == coords[1][0]){
            result.add(new int[]{coords[0][0], board.peekCol(coords[0][0])});
            result.add(new int[]{coords[1][0], board.peekCol(coords[1][0]) - 1});
        }
        else{
            result.add(new int[]{coords[0][0], board.peekCol(coords[0][0])});
            result.add(new int[]{coords[1][0], board.peekCol(coords[1][0])});
        }
        return result;
    }
}
