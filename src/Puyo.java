import java.util.Random;

public class Puyo {
    Attribute colour;
    int col;
    int row;
    boolean placed = false;

    private Puyo(Attribute colour){
        this.colour = colour;
    }

    public static Puyo[] create2Puyo(){
        Random x = new Random();
        int rand1 = x.nextInt(Attribute.values().length - 3);
        int rand2 = x.nextInt(Attribute.values().length - 3);
        Puyo p1 = new Puyo(Attribute.values()[rand1]);
        Puyo p2 = new Puyo(Attribute.values()[rand2]);
        p1.col = 7;
        p1.row = 0;
        p2.col = 7;
        p2.row = 1;
        return new Puyo[]{p1,p2};
    }

    public static Puyo[] createInitialPuyo(){
        Puyo[] p12 = create2Puyo();
        Puyo[] p34 = create2Puyo();
        Puyo[] p56 = create2Puyo();
        for (Puyo p: p12){
            p.col = 2;
        }
        for (Puyo p: p34){
            p.col = 6;
        }
        return new Puyo[] {p12[0],p12[1],p34[0],p34[1],p56[0],p56[1]};
    }

    public static Puyo createBlack(){
        return new Puyo(Attribute.BLACK);
    }

    public static Puyo createGrey(){
        return new Puyo(Attribute.GREY);
    }

    public void setPosition(int col, int row){
        this.col = col;
        this.row = row;
    }
}
