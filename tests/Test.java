import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Test {

    ArrayList<Puyo[][]> boards;
    ArrayList<ArrayList<int[]>> recentlyDropped;

    public Test(File f){
        processFile(f);
    }

    public static void main(String[] args){
        Test t = new Test(new File("testPatterns"));
//        t.testSinglePlayer();
        t.testMultiPlayer();
//        t.testDoubleOutput();
    }

    void testSinglePlayer(){
        Game g = new Game(new Board(boards.get(0)), recentlyDropped.get(0));
        g.play();
    }

    void testMultiPlayer(){
        Board b = new Board(boards.get(0));
        Game g = new Game(new Board[]{b.copyBoard(), b.copyBoard()}, new ArrayList[]{recentlyDropped.get(0), recentlyDropped.get(0)});
        g.play();
    }

    void testDoubleOutput(){
        Board b = new Board(boards.get(0));
        Output output = new Output(new Board[]{b,b});
        output.updateCurrentPuyo(Puyo.createInitialPuyo(), 0);
        output.updateCurrentPuyo(Puyo.createInitialPuyo(), 1);
        output.printCurrentPuyo();
        output.printBoards();
    }

    void processFile(File f){
        ArrayList<String> boards = new ArrayList<>();
        try {
            Scanner myReader = new Scanner(f);
            while (myReader.hasNextLine()) {
                boards.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        ArrayList<Puyo[][]> boardList = new ArrayList<>();
        ArrayList<ArrayList<int[]>> recentlyDroppedList = new ArrayList<>();
        for (String boardS: boards){
            Puyo[][] board = new Puyo[6][13];
            ArrayList<int[]> recentlyDropped = new ArrayList<>();
            String[] temp = boardS.split(";");
            String[] posS = temp[1].split(",");
            for (int i = 0; i < posS.length; i += 2) {
                int[] pos = new int[]{Integer.parseInt(posS[i]), Integer.parseInt(posS[i + 1])};
                recentlyDropped.add(pos);
            }
            String[] cols = temp[0].split("-");
            for (int i = 0; i < cols.length; i ++){
                String col = cols[i];
                String[] runs = col.split(",");
                int progressDownCol = board[0].length - 1;
                for (String run: runs){
                    String lengthRun = "";
                    for (char c: run.toCharArray()){
                        if (Character.isDigit(c)){
                            lengthRun += c;
                        }
                        else{
                            // Colour code here
                            Puyo type = decodeColour(c);
                            int length = Integer.parseInt(lengthRun);
                            for (int j = progressDownCol; j > progressDownCol - length; j --){
                                board[i][j] = type;
                            }
                            progressDownCol -= length;
                            break;
                        }
                    }
                }
            }
            recentlyDroppedList.add(recentlyDropped);
            boardList.add(board);
        }
        this.boards = boardList;
        this.recentlyDropped = recentlyDroppedList;
    }
    Puyo decodeColour(char c){
        switch(c){
            case '_':
                return null;
            case 'N':
                return Puyo.createGarbage();
            case 'Y':
                return new Puyo(Colour.YELLOW);
            case 'R':
                return new Puyo(Colour.RED);
            case 'G':
                return new Puyo(Colour.GREEN);
            case 'B':
                return new Puyo(Colour.BLUE);
            case 'P':
                return new Puyo(Colour.MAGENTA);
        }
        return null;
    }
}
