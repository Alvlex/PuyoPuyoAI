package appTest;

import app.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class unitTestsOutput {

    @Test
    public void updateBoardTest(){
        Board b = new Board();
        Output output = new Output(new Board[]{b});
        Random x = new Random();
        b.dropGarbage(x.nextInt(6));
        Output o2 = new Output(new Board[]{b});
        Assert.assertNotEquals(output.printBoards(), o2.printBoards());
        output.updateBoard(b, 0);
        Assert.assertEquals(output.printBoards(), o2.printBoards());
    }

    @Test
    public void updateCurrentPuyoTest(){
        Puyo[][] puyo = PuyoI.createInitialPuyo(4);
        Output output = new Output(new Board[]{new Board()});
        output.updateCurrentPuyo(puyo, 0);
        puyo[0] = puyo[1];
        puyo[1] = puyo[2];
        puyo[2] = PuyoI.create2Puyo(4);
        Output o2 = new Output(new Board[]{new Board()});
        o2.updateCurrentPuyo(puyo, 0);
        Assert.assertNotEquals(output.printCurrentPuyo(), o2.printCurrentPuyo());
        output.updateCurrentPuyo(puyo, 0);
        Assert.assertEquals(output.printCurrentPuyo(), o2.printCurrentPuyo());
    }

    @Test
    public void updateMovesTest(){
        Puyo[][] puyo = PuyoI.createInitialPuyo(4);
        Random x = new Random();
        Move m = new Move(x.nextInt(6), x.nextInt(4));
        Output o1 = new Output(new Board[]{new Board()});
        Output o2 = new Output(new Board[]{new Board()});
        o2.updateMoves(m, 0);
        o1.updateCurrentPuyo(puyo, 0);
        o2.updateCurrentPuyo(puyo, 0);
        Assert.assertNotEquals(o1.printCurrentPuyo(), o2.printCurrentPuyo());
        o1.updateMoves(m, 0);
        Assert.assertEquals(o1.printCurrentPuyo(), o2.printCurrentPuyo());
    }

    @Test
    public void printCurrentPuyoTest(){
        Puyo[][] puyo = PuyoI.createInitialPuyo(4);
        Random x = new Random();
        Move m = new Move(x.nextInt(6), x.nextInt(4));
        Output o1 = new Output(new Board[]{new Board()});
        o1.updateCurrentPuyo(puyo, 0);
        o1.updateMoves(m, 0);
        StringBuilder result = new StringBuilder();
        int[][] coords = m.getCoord();
        for (int i = 0; i < 2; i ++){
            result.append(" ");
            for (int j = 0; j < 6; j ++){
                for (int k = 0; k < 2; k ++) {
                    if (coords[k][0] == j && coords[k][1] == i) {
                        result.append(" \033[").append(Colour.valueOf(puyo[0][k].getColour()).value).append("m⬤\033[0m");
                        break;
                    }
                    if (k == 1){
                        result.append(" \033[").append(Colour.valueOf("BLACK").value).append("m⬤\033[0m");
                    }
                }
            }
            result.append("    \033[").append(Colour.valueOf(puyo[1][i].getColour()).value).append("m⬤\033[0m  \033[").append(Colour.valueOf(puyo[2][i].getColour()).value).append("m⬤\033[0m");
            result.append("     \n");
        }
        Assert.assertEquals(o1.printCurrentPuyo(), result.toString());
    }

    @Test
    public void printBoardsTest(){
        Random x = new Random();
        Board b = new Board();
        b.dropGarbage(x.nextInt(6));
        Output o1 = new Output(new Board[]{b});
        StringBuilder output = new StringBuilder();
        for (int row = 12; row >= 0; row --){
            output.append("|");
            for (int col = 0; col < 6; col++) {
                if (b.getPuyo(col, row) != null) {
                    output.append(" \033[").append(Colour.valueOf(b.getPuyo(col, row).getColour()).value).append("m⬤\033[0m");
                } else {
                    output.append(" \033[").append(Colour.valueOf("BLACK").value).append("m⬤\033[0m");
                }
            }
            output.append(" |  \033[").append(Colour.valueOf("BLACK").value).append("m⬤\033[0m  \033[").append(Colour.valueOf("BLACK").value).append("m⬤\033[0m     \n");
        }
        Assert.assertEquals(output.toString(), o1.printBoards());
    }
}
