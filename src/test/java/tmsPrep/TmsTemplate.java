package tmsPrep;

import appTest.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TmsTemplate {
    int[][] templateMatrix = new int[78][78];


    public TmsTemplate(String chainFile, String templateFile, String outputFile){
        Template chain = new Template("tmsTemplates", chainFile);
        Template template = new Template("tmsTemplates", templateFile);
        LabelledCells lc = new LabelledCells(chain, template);
        RelMatrix rm = new RelMatrix(lc, chain);
        Weights weights = new Weights(rm.sortedLetters);
        for (int i = 0; i < 78; i ++){
            for (int j = 0; j < 78; j ++){
                char labelI = lc.cells[i % 6][Math.floorDiv(i, 6)];
                char labelJ = lc.cells[j % 6][Math.floorDiv(j, 6)];
                char relation = rm.getCell(labelI, labelJ);
                if (relation == 'X')
                    templateMatrix[i][j] = - Math.min(weights.get(labelI), weights.get(labelJ));
                else if (relation == 'S')
                    templateMatrix[i][j] = Math.min(weights.get(labelI), weights.get(labelJ));
                else
                    templateMatrix[i][j] = 0;
            }
        }
        try {
            FileWriter fw = new FileWriter(new File("src/main/java/AI/tms/templates/" + outputFile));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 78; i ++){
                for (int j = 0; j < 78; j ++){
                    sb.append(templateMatrix[i][j]).append(",");
                }
                sb.deleteCharAt(sb.lastIndexOf(","));
                sb.append("\n");
            }
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        TmsTemplate tms = new TmsTemplate("TMS3Chain.csv", "TMS3.csv", "TMS3.csv");
    }

}