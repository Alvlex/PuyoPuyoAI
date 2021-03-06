package aiTest;

import Strategies.Strategy;
import Strategies.pms.PMS;
import Strategies.tms.TMS;
import Game.*;
import appTest.Template;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class testsTMS {

    Template pms1 = new Template("PMS1.csv");
    Template tms1 = new Template("TMS1.csv");
    Puyo[][] currentPuyo = new Puyo[3][2];
    PMS pms;

    @Before
    public void prepare(){
        pms = new PMS(3,8,220);
        for (int i = 0; i < 3; i ++)
            for (int j = 0; j < 2; j ++)
                currentPuyo[i][j] = new Puyo(Colour.YELLOW);
    }

    @Test
    public void checkCompleteTemplate(){
        TMS tms = new TMS(pms);
        currentPuyo[0][0] = new Puyo(Colour.BLUE);
        Move m = tms.makeMove(tms1.getBoard(), currentPuyo, new Board());
        System.out.println(tms.getTemplate());
        for (int i = 0; i < 2; i ++){
            Assert.assertTrue(sameCoord(m.getCoord()[i], new Coordinate(5, 1 - i)));
        }
    }

    private boolean sameCoord(Coordinate coord1, Coordinate coord2){
        System.out.println(coord1.getX() + ", " + coord2.getX());
        System.out.println(coord1.getY() + ", " + coord2.getY());
        return coord1.getX() == coord2.getX() && coord1.getY() == coord2.getY();
    }

    @Test
    public void evalTMS(){
        evaluation(100, "TMSEval", new PMS(4, 4, 300));
    }

    public static void evaluation(int noOfGames, String fileName, PMS pms){
        Game g;
        ArrayList<Long> times = new ArrayList<>();
        int[] chainLengths = new int[20];
        for (int i = 0; i < noOfGames; i ++) {
            System.out.println("Game number " + i);
            TMS tms = new TMS(pms);
            g = new Game(new Strategy[]{tms}, i);
            chainLengths[g.play(Integer.MAX_VALUE)] ++;
            times.addAll(tms.getTimes());
        }
        int avgChains = 0;
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < chainLengths.length; i++) {
            output.append(i).append(":").append("\t");
            avgChains += i * chainLengths[i];
            output.append(chainLengths[i]).append("\n");
        }
        output.append("Average chain: ").append((double) avgChains / noOfGames).append("\n");
        long[] sorted = times.stream().mapToLong(i -> i).toArray();
        Arrays.sort(sorted);
        output.append(Arrays.toString(sorted));
        try {
            FileWriter fw = new FileWriter(fileName + ".txt");
            fw.write(output.toString());
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTimings(){
        TMS tms = new TMS(pms);
        tms.makeMove(pms1.getBoard(), PuyoI.createInitialPuyo(4, new Random(0)), new Board());

    }

}
