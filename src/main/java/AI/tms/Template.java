package AI.tms;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;

public class Template {
    int[][] matrix = new int[78][78];
    String name;

    Template(String fileName){
        name = fileName.replace(".csv", "");
        readBoard(fileName);
    }

    private void readBoard(String file){
        File f = new File("src/main/java/AI/tms/templates/" + file);
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
