package evaluation;

import Strategies.Strategy;
import Strategies.pms.PMS;
import Game.Game;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class AwsTests {

    public static void main(String[] args){
//        args = new String[]{"PMS,3,8,300","PMS3.txt"};
        String pmsS = args[0];
        String[] parts = pmsS.split(",");
        int arg1 = Integer.parseInt(parts[1]);
        int arg2 = Integer.parseInt(parts[2]);
        int arg3 = Integer.parseInt(parts[3]);

        String output = "";
        if (parts[0].equals("PMS")){
            output = evaluation(arg1, arg2, arg3);
        }

        try {
            FileWriter fw = new FileWriter(args[1]);
            fw.append(output);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String evaluation(int arg1, int arg2, int arg3){
        Game g;
        int[] chainLengths = new int[20];
        int noOfGames = 1000;
        for (int i = 0; i < noOfGames; i ++) {
            PMS pms = new PMS(arg1, arg2, arg3);
            System.out.println("Game number " + i);
            g = new Game(new Strategy[]{pms}, i);
            chainLengths[g.play(Integer.MAX_VALUE)] ++;
            BufferedWriter bw = null;
            try {
                // --------------------
                // APPEND MODE SET HERE
                // --------------------
                bw = new BufferedWriter(new FileWriter("temp.txt", true));
                HashMap<Long, Integer> times = pms.printStats();
                for (long key: times.keySet()){
                    for (int j = 0; j < times.get(key); j ++){
                        bw.write(String.valueOf(key));
                        bw.newLine();
                        bw.flush();
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {                       // always close the file
                if (bw != null) try {
                    bw.close();
                } catch (IOException ioe2) {
                    // just ignore it
                }
            } // end try/catch/finally
        }
        int avgChains = 0;
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < chainLengths.length; i++) {
            output.append(i).append(":").append("\t");
            avgChains += i * chainLengths[i];
            output.append(chainLengths[i]).append("\n");
        }
        output.append("Average chain: ").append((double) avgChains / noOfGames).append("\n");
        ArrayList<Long> manipulated = new ArrayList<>();
        try {
            Scanner myReader = new Scanner(new File("temp.txt"));
            while (myReader.hasNextLine()) {
                manipulated.add(Long.parseLong(myReader.nextLine()));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        long[] sorted = manipulated.stream().mapToLong(i -> i).toArray();
        Arrays.sort(sorted);
        output.append(Arrays.toString(sorted));
        return output.toString();
    }
}
