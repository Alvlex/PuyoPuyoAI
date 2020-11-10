class Output {

    Board[] boards;
    Puyo[][][] currentPuyo;
    Move[] moves;

    Output(Board[] boards){
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

    void updateBoard(Board b, int playerNo){
        boards[playerNo] = b.copyBoard();
    }

    void updateCurrentPuyo(Puyo[][] currentPuyo, int playerNo){
        Puyo[][] currentPuyoCopy = new Puyo[currentPuyo.length][currentPuyo[0].length];
        for (int i = 0; i < currentPuyoCopy.length; i ++){
            for (int j = 0; j < currentPuyoCopy[j].length; j ++){
                currentPuyoCopy[i][j] = currentPuyo[i][j].copyPuyo();
            }
        }
        this.currentPuyo[playerNo] = currentPuyoCopy;
    }

    void updateMoves(Move m, int playerNo){
        this.moves[playerNo] = m;
    }

    private String printPuyo(Puyo p) {
        String output = "⬤";
        output = "\033[" + p.colour.value + "m" + output + "\033[0m";
        return " " + output;
    }

    void printCurrentPuyo(){
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
                        output += printPuyo(Puyo.createBlack());
                }
                output += "   " + printPuyo(currentPuyo[boardNo][1][i]) + " " + printPuyo(currentPuyo[boardNo][2][i]);
                output += "     ";
            }
            output += "\n";
        }
        System.out.print(output);
    }

    void printBoards(){
        String output = "";
        for (int row = boards[0].getNoRows() - 1; row >= 0; row --){
            for (int boardNo = 0; boardNo < boards.length; boardNo ++) {
                output += "|";
                for (int col = 0; col < boards[boardNo].getNoCols(); col++) {
                    if (boards[boardNo].getPuyo(col, row) != null) {
                        output += printPuyo(boards[boardNo].getPuyo(col, row));
                    } else {
                        output += printPuyo(Puyo.createBlack());
                    }
                }
                output += " | " + printPuyo(Puyo.createBlack()) + " " + printPuyo(Puyo.createBlack()) + "     ";
            }
            output += "\n";
        }
        System.out.println(output);
    }
}