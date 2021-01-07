package app;

import java.util.Random;

public interface PuyoI {

    Puyo copyPuyo(); // Doesn't need tests

    static Puyo[] create2Puyo(int noOfColours, Random x){
        int rand1 = x.nextInt(noOfColours);
        int rand2 = x.nextInt(noOfColours);
        Puyo p1 = new Puyo(Colour.values()[rand1]);
        Puyo p2 = new Puyo(Colour.values()[rand2]);
        return new Puyo[]{p1,p2};
    }

    static Puyo[][] createInitialPuyo(int noOfColours, Random x){
        Puyo[] p12 = create2Puyo(noOfColours, x);
        Puyo[] p34 = create2Puyo(noOfColours, x);
        Puyo[] p56 = create2Puyo(noOfColours, x);
        return new Puyo[][] {p12, p34, p56};
    }

    static Puyo createBlack(){
        return new Puyo(Colour.BLACK);
    } // Doesn't need tests
    static Puyo createGarbage(){
        return new Puyo(Colour.GREY);
    } // Doesn't need tests

    String getColour(); // Doesn't need tests
}
