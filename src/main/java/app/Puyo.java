package app;

import java.util.Random;

public class Puyo implements PuyoI{
    Colour colour;

    public Puyo(Colour colour){
        this.colour = colour;
    }

    @Override
    public Puyo copyPuyo(){
        return new Puyo(colour);
    }

    @Override
    public String getColour(){
        return colour.name();
    }
}
