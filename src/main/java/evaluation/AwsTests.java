package evaluation;

import AI.Strategy;
import AI.pms.PMS;
import app.Game;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AwsTests {

    public static void main(String[] args){
        String pmsS = args[0];
        String[] parts = pmsS.split(",");
        int arg1 = Integer.parseInt(parts[1]);
        int arg2 = Integer.parseInt(parts[2]);
        int arg3 = Integer.parseInt(parts[3]);

        String output = "";
        if (parts[0].equals("PMS")){
            output = evaluation(new PMS(arg1, arg2, arg3));
        }

        try {
            FileWriter fw = new FileWriter(args[1]);
            fw.append(output);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String evaluation(PMS pms){
        Game g;
        int[] chainLengths = new int[20];
        int noOfGames = 1000;
        for (int i = 0; i < noOfGames; i ++) {
            System.out.println("Game number " + i);
            g = new Game(new Strategy[]{pms}, i);
            chainLengths[g.play(Integer.MAX_VALUE)] ++;
        }
        int avgChains = 0;
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < chainLengths.length; i++) {
            output.append(i).append(":").append("\t");
            avgChains += i * chainLengths[i];
            output.append(chainLengths[i]).append("\n");
        }
        output.append("Average chain: ").append((double) avgChains / noOfGames).append("\n");
        HashMap<Long, Integer> times = pms.printStats();
        ArrayList<Long> manipulated = new ArrayList<>();
        for (long key: times.keySet()){
            for (int i = 0; i < times.get(key); i ++){
                manipulated.add(key);
            }
        }
        long[] sorted = manipulated.stream().mapToLong(i -> i).toArray();
        Arrays.sort(sorted);
        output.append(Arrays.toString(sorted));
        return output.toString();
    }
}
