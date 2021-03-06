package appTest;

import Game.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class unitTestsOutput {

    private Template empty = new Template("empty.csv");
    private Output output;
    private Board b;
    private Output o2;
    private Puyo[][] puyo;
    private Random x;

    @Before
    public void prepare(){
        x = new Random(0);
        b = empty.getBoard();
        output = new Output(new Board[]{b});
        o2 = new Output(new Board[]{empty.getBoard()});
        puyo = PuyoI.createInitialPuyo(4, x);
        output.updateCurrentPuyo(puyo, 0);
        o2.updateCurrentPuyo(puyo, 0);
    }

    @Test
    public void updateBoardTest(){
        b.dropGarbage(0);
        o2 = new Output(new Board[]{b});
        Assert.assertNotEquals(output.printBoards(), o2.printBoards());
        output.updateBoard(b, 0);
        Assert.assertEquals(output.printBoards(), o2.printBoards());
    }

    private Puyo[][] nextPuyo(Puyo[][] puyo){
        Puyo[][] result = new Puyo[puyo.length][puyo[0].length];
        result[0] = puyo[1];
        result[1] = puyo[2];
        result[2] = PuyoI.create2Puyo(4, x);
        return result;
    }

    @Test
    public void updateCurrentPuyoTest(){
        puyo = nextPuyo(puyo);
        o2.updateCurrentPuyo(puyo, 0);
        Assert.assertNotEquals(output.printCurrentPuyo(), o2.printCurrentPuyo());
        output.updateCurrentPuyo(puyo, 0);
        Assert.assertEquals(output.printCurrentPuyo(), o2.printCurrentPuyo());
    }

    @Test
    public void updateMovesTest(){
        for (int col = 0; col < 6; col ++) {
            for (int rot = 0; rot < 4; rot ++) {
                Move m = new Move(col, rot);
                o2.updateMoves(m, 0);
                Assert.assertNotEquals(output.printCurrentPuyo(), o2.printCurrentPuyo());
                output.updateMoves(m, 0);
                Assert.assertEquals(output.printCurrentPuyo(), o2.printCurrentPuyo());
            }
        }
    }

    @Test
    public void printCurrentPuyoTest(){
        for (int col = 0; col < 6; col ++) {
            for (int rot = 0; rot < 4; rot ++) {
                Move m = new Move(col, rot);
                output.updateMoves(m, 0);
                StringBuilder result = new StringBuilder();
                Coordinate[] coords = m.getCoord();
                for (int i = 0; i < 2; i++) {
                    result.append(" ");
                    for (int j = 0; j < 6; j++) {
                        for (int k = 0; k < 2; k++) {
                            if (coords[k].getX() == j && coords[k].getY() == i) {
                                result.append(" \033[").append(Colour.valueOf(puyo[0][k].getColour()).value).append("m⬤\033[0m");
                                break;
                            }
                            else if (k == 1) {
                                result.append(" \033[").append(Colour.valueOf("BLACK").value).append("m⬤\033[0m");
                            }
                        }
                    }
                    result.append("    \033[").append(Colour.valueOf(puyo[1][i].getColour()).value).append("m⬤\033[0m  \033[").append(Colour.valueOf(puyo[2][i].getColour()).value).append("m⬤\033[0m");
                    result.append("     \n");
                }
                Assert.assertEquals(output.printCurrentPuyo(), result.toString());
            }
        }
    }

    @Test
    public void printBoardsTest(){
        b.dropGarbage(0);
        output.updateBoard(b, 0);
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
        Assert.assertEquals(output.toString(), this.output.printBoards());
    }
}
