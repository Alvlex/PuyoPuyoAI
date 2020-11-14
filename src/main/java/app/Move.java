package app;

public class Move implements MoveI{
    private int col; // Can only be between 0 and 5
    private int rot; // 0 means 2nd Puyo is below
                     // 1 means 2nd Puyo is to the left
                     // 2 means 2nd Puyo is above
                     // 3 means 2nd Puyo is to the right

    public Move(){
        col = 2;
        rot = 0;
    }

    public Move(int col, int rot){
        if (0 <= col && col < 6){
            this.col = col;
        }
        else{
            this.col = 2;
        }
        if (0 <= rot && rot < 4){
            this.rot = rot;
        }
        else{
            rot = 0;
        }
        rotHelper();
    }

    public void left(){
        if (col > 1 || (col == 1 && rot != 1))
            col --;
    }

    public void right(){
        if (col < 4 || (col == 4 && rot != 3))
            col ++;
    }

    public void rotClock(){
        rot = (rot + 1) % 4;
        rotHelper();
    }

    public void rotAnti(){
        rot = (rot + 3) % 4;
        rotHelper();
    }

    private void rotHelper(){
        if (rot == 1 && col == 0)
            col ++;
        else if (rot == 3 && col == 5)
            col --;
    }

    public int[][] getCoord(){
        int[][] coords = new int[2][2];
        coords[0][0] = col;
        switch(rot){
            case 0:
                coords[1][0] = col;
                coords[1][1] = 1;
                break;
            case 1:
                coords[1][0] = col - 1;
                break;
            case 2:
                coords[1][0] = col;
                coords[1][1] = 0;
                coords[0][1] = 1;
                break;
            case 3:
                coords[1][0] = col + 1;
                break;
        }
        return coords;
    }
}
