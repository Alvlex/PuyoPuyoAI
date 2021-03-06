package Strategies.tms;

import Game.Board;
import Game.Coordinate;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Template {
    private int[][] matrix = new int[78][78];
    String name;
    List<Coordinate> chainStart = new ArrayList<>();

    Template(String fileName){
        name = fileName.replace(".csv", "");
        readBoard(fileName);
        getChainStart();
    }

    public int getNoBlocked(Board b){
        int total = 0;
        for (Coordinate c: chainStart){
            if (b.getPuyo(c) != null){
                total += 1;
            }
        }
        return total;
    }

    private void getChainStart(){
        File f = new File("src/test/testTemplates/tmsTemplates/" + name + "/startChain.csv");
        try (FileReader fr = new FileReader(f);
             CSVReader reader = new CSVReader(fr)){
            String[] nextLine;
            while((nextLine = reader.readNext()) != null){
                chainStart.add(new Coordinate(Integer.parseInt(nextLine[0]), Integer.parseInt(nextLine[1])));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readBoard(String file){
        File f = new File("src/main/java/Strategies/tms/templates/" + file);
        try (FileReader fr = new FileReader(f);
             CSVReader reader = new CSVReader(fr)) {
            String[] nextLine;
            int row = 0;
            while ((nextLine = reader.readNext()) != null && row < 78) {
                for (int i = 0; i < nextLine.length; i ++) {
                    matrix[row][i] = Integer.parseInt(nextLine[i]);
                }
                row ++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int getEntry(int i, int j){
        return matrix[i][j];
    }

    int getTotalWeight(){
        int total = 0;
        for (int i = 0; i < 78; i ++){
            for (int j = 0; j < 78; j ++){
                total += Math.abs(matrix[i][j]);
            }
        }
        return total;
    }

}
