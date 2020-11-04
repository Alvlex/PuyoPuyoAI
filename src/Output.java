public class Output {

    public static String printPuyo(Puyo p) {
        String output = "⬤";
        output = "\033[" + p.colour.value + "m" + output + "\033[0m";
        return " " + output;
    }

    public static void printCurrentPuyo(Puyo[][] currentPuyo, Move m){
        String output = "";
        int[][] coords = m.getCoord();
        for (int i = 0; i < 2; i ++){
            output += " ";
            for (int j = 0; j < 6; j ++){
                boolean contentAdded = false;
                for (int k = 0; k < 2; k ++){
                    if (coords[k][0] == j && coords[k][1] == i){
                        output += printPuyo(currentPuyo[0][k]);
                        contentAdded = true;
                        break;
                    }
                }
                if (!contentAdded)
                    output += printPuyo(Puyo.createBlack());
            }
            output += "   " + printPuyo(currentPuyo[1][i]) + " " + printPuyo(currentPuyo[2][i]) + "\n";
        }
        System.out.print(output);
    }

    public static void printBoard(Board board){
        String output = "";
        for (int row = board.getNoRows() - 1; row >= 0; row --){
            output += "|";
            for (int col = 0; col < board.getNoCols(); col ++){
                if (board.getPuyo(col,row) != null){
                    output += printPuyo(board.getPuyo(col,row));
                }
                else{
                    output += printPuyo(Puyo.createBlack());
                }
            }
            output += " |\n";
        }
        System.out.println(output);
    }

}