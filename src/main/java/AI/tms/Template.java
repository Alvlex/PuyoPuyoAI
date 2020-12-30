package AI.tms;

public class Template {
    int[][] matrix = new int[78][78];

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
