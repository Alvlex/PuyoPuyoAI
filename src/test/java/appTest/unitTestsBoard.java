package appTest;
import app.*;
import org.junit.*;

import java.util.Random;

public class unitTestsBoard {

    Board empty;
    Board full;

    @Before
    public void prepare(){
        empty = new Board();
        Puyo[][] full = new Puyo[6][13];
        for (int i = 0; i < 6; i ++){
            for (int j = 0; j < 13; j ++){
                full[i][j] = PuyoI.createGarbage();
            }
        }
        this.full = new Board(full);
    }
    @Test
    public void dropGarbageTest(){
        Random x = new Random();
        Board copy = empty.copyBoard();
        int col = x.nextInt(6);
        copy.dropGarbage(col);
        Assert.assertEquals(copy.getPuyo(col, 0).getColour(), "GREY");
    }
    @Test
    public void checkPossibilitiesTest(){
        Assert.assertEquals(full.checkPossibilities(), false);
        Assert.assertEquals(empty.checkPossibilities(), true);
        Board copyFull = full.copyBoard();
        copyFull.removePuyo(0, 12);
        copyFull.removePuyo(2, 12);
        copyFull.removePuyo(4, 12);
        Assert.assertEquals(copyFull.checkPossibilities(), false);
    }
    @Test
    public void dropPuyoTest(){
        Random x = new Random();
        Colour colour1 = Colour.values()[x.nextInt(Colour.values().length)];
        Colour colour2 = Colour.values()[x.nextInt(Colour.values().length)];
        Puyo[] puyo = {new Puyo(colour1), new Puyo(colour2)};
        Move m = new Move();
        Board copy = empty.copyBoard();
        // Check it succeeded
        Assert.assertEquals(copy.dropPuyo(puyo, m), true);
        Assert.assertEquals(copy.getPuyo(2, 0).getColour(), colour2.name());
        Assert.assertEquals(copy.getPuyo(2,1).getColour(), colour1.name());

        Board copyFull = full.copyBoard();
        copyFull.removePuyo(2, 12);
        // Check it failed properly
        Assert.assertEquals(copyFull.dropPuyo(puyo, m), false);
        Assert.assertEquals(copyFull.getPuyo(2,12), null);
    }

    @Test
    public void cascadePuyoTest(){
        Random x = new Random();
        Puyo[][] temp = new Puyo[6][13];
        Puyo[][] result = new Puyo[6][13];
        for (int i = 0; i < 6; i ++){
            int colPos = 1;
            int count = 0;
            while (colPos < 13){
                Puyo p = new Puyo(Colour.values()[x.nextInt(Colour.values().length - 1)]);
                temp[i][colPos] = p;
                result[i][count] = p;
                colPos += x.nextInt(2) + 1;
                count ++;
            }
        }
        Board b = new Board(temp);
        b.cascadePuyo();
        assertEqualBoards(b, new Board(result));
    }

    void assertEqualBoards(Board b1, Board b2){
        for (int i = 0; i < 6; i ++){
            for (int j = 0; j < 13; j ++){
                if (b1.getPuyo(i,j) == null || b2.getPuyo(i,j) == null){
                    Assert.assertEquals(b1.getPuyo(i,j), b2.getPuyo(i,j));
                }
                else {
                    Assert.assertEquals(b1.getPuyo(i, j).getColour(), b2.getPuyo(i,j).getColour());
                }
            }
        }
    }

    @Test
    public void copyBoardTest(){
        Board emptyCopy = empty.copyBoard();
        Board fullCopy = full.copyBoard();
        assertEqualBoards(emptyCopy, empty);
        assertEqualBoards(fullCopy, full);
        for (int i = 0; i < 6; i ++){
            for (int j = 0; j < 13; j ++){
                if (emptyCopy.getPuyo(i,j) != null){
                    Assert.assertNotEquals(empty.getPuyo(i,j), emptyCopy.getPuyo(i,j));
                }
                if (fullCopy.getPuyo(i,j) != null){
                    Assert.assertNotEquals(full.getPuyo(i,j), fullCopy.getPuyo(i,j));
                }
            }
        }
    }

    @Test
    public void getPuyoTest(){
        Random x = new Random();
        Puyo[][] board = new Puyo[6][13];
        for (int i = 0; i < 6; i ++){
            for (int j = 0; j < 13; j ++){
                board[i][j] = new Puyo(Colour.values()[x.nextInt(Colour.values().length)]);
            }
        }
        Board b = new Board(board);
        for (int i = 0; i < 6; i ++){
            for (int j = 0; j < 13; j ++){
                Assert.assertNotEquals(b.getPuyo(i,j), board[i][j]);
                Assert.assertEquals(b.getPuyo(i,j).getColour(), board[i][j].getColour());
                Assert.assertEquals(empty.getPuyo(i,j), null);
                Assert.assertNotEquals(b.getPuyo(new int[]{i,j}), board[i][j]);
                Assert.assertEquals(b.getPuyo(new int[]{i,j}).getColour(), board[i][j].getColour());
                Assert.assertEquals(empty.getPuyo(new int[]{i,j}), null);
            }
        }
    }

    @Test
    public void removePuyoTest(){
        for (int i = 0; i < 6; i ++){
            Assert.assertEquals(empty.peekCol(i), -1);
            Assert.assertEquals(full.peekCol(i), 12);
        }
    }

}
