import java.util.Random;

public class Puyo {
    Colour colour;

    Puyo(Colour colour){
        this.colour = colour;
    }

    public Puyo copyPuyo(){
        return new Puyo(colour);
    }

    public static Puyo[] create2Puyo(){
        Random x = new Random();
        int rand1 = x.nextInt(Colour.values().length - 3);
        int rand2 = x.nextInt(Colour.values().length - 3);
        Puyo p1 = new Puyo(Colour.values()[rand1]);
        Puyo p2 = new Puyo(Colour.values()[rand2]);
        return new Puyo[]{p1,p2};
    }

    public static Puyo[][] createInitialPuyo(){
        Puyo[] p12 = create2Puyo();
        Puyo[] p34 = create2Puyo();
        Puyo[] p56 = create2Puyo();
        return new Puyo[][] {p12, p34, p56};
    }

    public static Puyo createBlack(){
        return new Puyo(Colour.BLACK);
    }

    public static Puyo createGarbage(){
        return new Puyo(Colour.GREY);
    }
}
