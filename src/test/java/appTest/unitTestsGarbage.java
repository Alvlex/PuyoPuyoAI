package appTest;

import Game.Board;
import Game.Garbage;
import org.junit.*;

public class unitTestsGarbage {

    private Template empty = new Template("empty.csv");
    private Template dropGarbageOutput = new Template("dropGarbageOutput.csv");
    private Board b = empty.getBoard();
    private Garbage g = new Garbage(b);

    @Before
    public void reset(){
        b = empty.getBoard();
        g = new Garbage(b);
    }

    @Test
    public void dropGarbageTest(){
        g.makeGarbage(6);
        g.dropGarbage();
        Assert.assertEquals(10, g.removeGarbage(10));
        Assert.assertTrue(dropGarbageOutput.equalBoards(b));

    }

    @Test
    public void dropGarbageFillTest(){
        g.makeGarbage(78);
        g.dropGarbage();
        Assert.assertFalse(b.checkPossibilities());
    }

    @Test
    public void removeGarbageTest(){
        Assert.assertEquals(50, g.removeGarbage(50));
        g.makeGarbage(25);
        Assert.assertTrue(g.removeGarbage(50) == 25);
    }

    @Test
    public void removeGarbageOverflowTest(){
        g.makeGarbage(100);
        Assert.assertEquals(0, g.removeGarbage(50));
    }

}
