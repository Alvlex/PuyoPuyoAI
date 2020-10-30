public class Output {

    public static String printPuyo(Puyo p) {
        String output = "⬤";
        output = "\033[" + p.colour.value + "m" + output + "\033[0m";
        return output;
    }

    public static void printCurrentPuyo(Puyo[] currentPuyo){
        String output = "";
        for (int i = 1; i >= 0; i --) {
            output += " ";
            for (int j = 0; j < 8; j++) {
                boolean puyoPrinted = false;
                for (Puyo p: currentPuyo) {
                    if (p.row == i && p.col == j) {
                        output += " " + printPuyo(p);
                        puyoPrinted = true;
                    }
                }
                if (!puyoPrinted) {
                    output += " " + printPuyo(Puyo.createBlack());
                }
                if (j == 5){
                    output += "   ";
                }
                else if (j == 6){
                    output += " ";
                }
            }
            output += "\n";
        }
        System.out.print(output);
    }

    public static void printBoard(Puyo[][] board){
        String output = "";
        for (int row = board[0].length - 1; row >= 0; row --){
            output += "|";
            for (int col = 0; col < board.length; col ++){
                if (board[col][row] != null){
                    output += " " + printPuyo(board[col][row]);
                }
                else{
                    output += " " + printPuyo(Puyo.createBlack());
                }
            }
            output += " |\n";
        }
        System.out.println(output);
    }

}