package appTest;

import Game.Colour;
import Game.Puyo;
import Game.PuyoI;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class unitTestsPuyo {

    private Random x = new Random(0);

    @Test
    public void create2PuyoTest(){
        for (int noOfColours = 1; noOfColours < Colour.values().length - 1; noOfColours ++) {
            twoPuyoTestHelper(PuyoI.create2Puyo(noOfColours, x), noOfColours);
        }
    }

    @Test
    public void createInitialPuyoTest(){
        for (int noOfColours = 1; noOfColours < Colour.values().length - 1; noOfColours ++) {
            Puyo[][] ps2 = PuyoI.createInitialPuyo(noOfColours, x);
            for (Puyo[] p: ps2){
                twoPuyoTestHelper(p, noOfColours);
            }
        }
    }

    private void twoPuyoTestHelper(Puyo[] p, int noOfColours){
        if (noOfColours == 1){
            Assert.assertEquals(p[0].getColour(), "RED");
            Assert.assertEquals(p[1].getColour(), "RED");
        }
        else{
            for (int i = noOfColours; i < Colour.values().length; i ++) {
                Assert.assertNotEquals(p[0].getColour(), Colour.values()[i]);
                Assert.assertNotEquals(p[1].getColour(), Colour.values()[i]);
            }
        }
    }
}
