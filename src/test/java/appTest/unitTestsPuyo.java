package appTest;

import app.Colour;
import app.Puyo;
import app.PuyoI;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class unitTestsPuyo {

    @Test
    public void create2PuyoTest(){
        Random x = new Random();
        Puyo[] p = PuyoI.create2Puyo(1);
        twoPuyoTestHelper(p, 1);
        int noOfColours = x.nextInt(5) + 2;
        Puyo[] p2 = PuyoI.create2Puyo(noOfColours);
        twoPuyoTestHelper(p2, noOfColours);
    }

    @Test
    public void createInitialPuyoTest(){
        Random x = new Random();
        Puyo[][] ps = PuyoI.createInitialPuyo(1);
        for (Puyo[] p: ps){
            twoPuyoTestHelper(p, 1);
        }
        int noOfColours = x.nextInt(5) + 2;
        Puyo[][] ps2 = PuyoI.createInitialPuyo(noOfColours);
        for (Puyo[] p: ps2){
            twoPuyoTestHelper(p, noOfColours);
        }
    }

    private void twoPuyoTestHelper(Puyo[] p, int noOfColours){
        Random x = new Random();
        if (noOfColours == 1){
            Assert.assertEquals(p[0].getColour(), "RED");
            Assert.assertEquals(p[1].getColour(), "RED");
        }
        else{
            Assert.assertNotEquals(p[0].getColour(), Colour.values()[x.nextInt(Colour.values().length - noOfColours) + noOfColours]);
            Assert.assertNotEquals(p[1].getColour(), Colour.values()[x.nextInt(Colour.values().length - noOfColours) + noOfColours]);
        }
    }
}
