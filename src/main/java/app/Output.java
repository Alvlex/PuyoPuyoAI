package app;

public class Output implements OutputI{

    private Board[] boards;
    private Puyo[][][] currentPuyo;
    private Move[] moves;

    public Output(Board[] boards){
        Board[] boardCopies = new Board[boards.length];
        for (int i = 0; i < boards.length; i ++){
            boardCopies[i] = boards[i].copyBoard();
        }
        this.boards = boardCopies;
        moves = new Move[boards.length];
        for (int i = 0; i < moves.length; i ++){
            moves[i] = new Move();
        }
        currentPuyo = new Puyo[boards.length][][];
    }

    public void updateBoard(Board b, int playerNo){
        boards[playerNo] = b.copyBoard();
    }

    public void updateCurrentPuyo(Puyo[][] currentPuyo, int playerNo){
        Puyo[][] currentPuyoCopy = new Puyo[currentPuyo.length][currentPuyo[0].length];
        for (int i = 0; i < currentPuyoCopy.length; i ++){
            for (int j = 0; j < currentPuyoCopy[j].length; j ++){
                currentPuyoCopy[i][j] = currentPuyo[i][j].copyPuyo();
            }
        }
        this.currentPuyo[playerNo] = currentPuyoCopy;
    }

    public void updateMoves(Move m, int playerNo){
        this.moves[playerNo] = m;
    }

    private String printPuyo(Puyo p) {
        String output = "⬤";
        output = "\033[" + p.colour.value + "m" + output + "\033[0m";
        return " " + output;
    }

    public String printCurrentPuyo(){
        String output = "";
        for (int i = 0; i < 2; i ++){
            for (int boardNo = 0; boardNo < boards.length; boardNo ++) {
                int[][] coords = moves[boardNo].getCoord();
                output += " ";
                for (int j = 0; j < 6; j++) {
                    boolean contentAdded = false;
                    for (int k = 0; k < 2; k++) {
                        if (coords[k][0] == j && coords[k][1] == i) {
                            output += printPuyo(currentPuyo[boardNo][0][k]);
                            contentAdded = true;
                            break;
                        }
                    }
                    if (!contentAdded)
                        output += printPuyo(PuyoI.createBlack());
                }
                output += "   " + printPuyo(currentPuyo[boardNo][1][i]) + " " + printPuyo(currentPuyo[boardNo][2][i]);
                output += "     ";
            }
            output += "\n";
        }
        return output;
    }

    public String printBoards(){
        String output = "";
        for (int row = boards[0].getNoRows() - 1; row >= 0; row --){
            for (int boardNo = 0; boardNo < boards.length; boardNo ++) {
                output += "|";
                for (int col = 0; col < boards[boardNo].getNoCols(); col++) {
                    if (boards[boardNo].getPuyo(col, row) != null) {
                        output += printPuyo(boards[boardNo].getPuyo(col, row));
                    } else {
                        output += printPuyo(PuyoI.createBlack());
                    }
                }
                output += " | " + printPuyo(PuyoI.createBlack()) + " " + printPuyo(PuyoI.createBlack()) + "     ";
            }
            output += "\n";
        }
        return output;
    }
}