package appTest;
import app.*;
import org.junit.*;

import java.util.ArrayList;
import java.util.Random;

public class unitTestsBoard {

    private Template empty = new Template("empty.csv");
    private Template full = new Template("full.csv");
    private Template partialFull = new Template("partialFull.csv");
    private Template cascadeInput = new Template("cascadeInput.csv");
    private Template cascadeOutput = new Template("cascadeOutput.csv");
    private Template cascade2 = new Template("cascade2.csv");

    private Puyo[] puyo = PuyoI.create2Puyo(4, new Random(0));
    private Move move = new Move();

    @Test
    public void dropGarbageTest(){
        Board emptyCopy = empty.getBoard();
        emptyCopy.dropGarbage(1);
        Assert.assertEquals(emptyCopy.getPuyo(1, 0).getColour(), "GREY");
    }
    @Test
    public void checkPossibilitiesTest(){
        Assert.assertFalse(full.getBoard().checkPossibilities());
        Assert.assertTrue(empty.getBoard().checkPossibilities());
        Assert.assertFalse(partialFull.getBoard().checkPossibilities());
    }
    @Test
    public void dropPuyoTest(){
        Board emptyCopy = empty.getBoard();
        // Check it succeeded
        Assert.assertTrue(emptyCopy.dropPuyo(puyo, move));
        Assert.assertEquals(emptyCopy.getPuyo(2, 0).getColour(), puyo[1].getColour());
        Assert.assertEquals(emptyCopy.getPuyo(2,1).getColour(), puyo[0].getColour());

        Board partialFullCopy = partialFull.getBoard();
        // Check it failed properly
        Assert.assertFalse(partialFullCopy.dropPuyo(puyo, move));
        Assert.assertNull(partialFullCopy.getPuyo(2, 12));
    }

    @Test
    public void cascadePuyoBoardTest(){
        Board cascadeInputCopy = cascadeInput.getBoard();
        cascadeInputCopy.cascadePuyo();
        Assert.assertTrue(cascadeOutput.equalBoards(cascadeInputCopy));
        // May need to check that cascade Puyo returns the right recently dropped as well
    }

    @Test
    public void cascadePuyoReturnTest(){
        ArrayList<Coordinate> actual = cascade2.getBoard().cascadePuyo();
        Coordinate[] expected = {new Coordinate(0,0), new Coordinate(1,0),
                new Coordinate(1,1), new Coordinate(1,2)};
        assertCoordinatesEqual(actual, expected);
    }

    private void assertCoordinatesEqual(ArrayList<Coordinate> c1, Coordinate[] c2){
        for (int i = 0; i < c1.size(); i ++){
            Assert.assertEquals(c1.get(i).getX(), c2[i].getX());
            Assert.assertEquals(c1.get(i).getY(), c2[i].getY());
        }
    }

    @Test
    public void copyBoardTest(){
        Assert.assertTrue(empty.equalBoards(empty.getBoard()));
        Assert.assertTrue(full.equalBoards(full.getBoard()));
        Assert.assertTrue(partialFull.equalBoards(partialFull.getBoard()));
        Assert.assertTrue(cascadeInput.equalBoards(cascadeInput.getBoard()));
        Assert.assertTrue(cascadeOutput.equalBoards(cascadeOutput.getBoard()));
    }

    @Test
    public void getPuyoTest(){
        Assert.assertEquals(cascadeOutput.getBoard().getPuyo(2,0).getColour(), "GREEN");
        Assert.assertEquals(cascadeOutput.getBoard().getPuyo(1,8).getColour(), "BLUE");
        Assert.assertEquals(cascadeOutput.getBoard().getPuyo(0,2).getColour(), "YELLOW");
        Assert.assertEquals(cascadeOutput.getBoard().getPuyo(5,5).getColour(), "RED");
        Assert.assertEquals(cascadeOutput.getBoard().getPuyo(3,12).getColour(), "GREY");
    }

    @Test
    public void peekColTest(){
        for (int i = 0; i < 6; i ++){
            Assert.assertEquals(empty.getBoard().peekCol(i), -1);
            Assert.assertEquals(full.getBoard().getPuyo(i, 12).getColour(), "GREY");
            Assert.assertEquals(full.getBoard().peekCol(i), 12);
        }
    }

}
