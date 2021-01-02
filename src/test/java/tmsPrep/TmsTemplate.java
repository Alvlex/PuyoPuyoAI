package tmsPrep;

import appTest.Template;

public class TmsTemplate {
    int[][] templateMatrix = new int[78][78];


    public TmsTemplate(String file){
        Template chain = new Template(file);
        LabelledCells lc = new LabelledCells(chain);
        RelMatrix rm = new RelMatrix(lc);
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
    }

    public static void main(String[] args){
        // Currently uses a full chain but should use an unfinished chain as the template.
        // Also still needs manual edits with relMatrix and weights
        TmsTemplate tms = new TmsTemplate("TMS1.csv");
    }

}
