package appTest;

import app.*;
import org.junit.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class appTest {

    ArrayList<Puyo[][]> boards;
    ArrayList<ArrayList<int[]>> recentlyDropped;
    ArrayList<Integer> targetScores;

    @Before
    public void before(){
         processFile(new File("testPatterns.csv"));
    }

    //@Test
    public void testSinglePlayer(){
        Game g = new Game(new Board(boards.get(0)), recentlyDropped.get(0));
        System.out.println(g.play(20));
        if (targetScores.get(0) != -1)
            Assert.assertEquals((long)targetScores.get(0), (long)g.play(20));
    }

    //@Test
    public void testMultiPlayer(){
        Board b = new Board(boards.get(0));
        Game g = new Game(new Board[]{b.copyBoard(), b.copyBoard()}, new ArrayList[]{recentlyDropped.get(0), recentlyDropped.get(0)});
        g.play(4);
    }

    //@Test
    public void testDoubleOutput(){
        Board b = new Board(boards.get(0));
        Output output = new Output(new Board[]{b,b});
        output.updateCurrentPuyo(PuyoI.createInitialPuyo(4), 0);
        output.updateCurrentPuyo(PuyoI.createInitialPuyo(4), 1);
        output.printCurrentPuyo();
        output.printBoards();
    }

    ArrayList<String> readFile(File f){
        ArrayList<String> lines = new ArrayList<>();
        try {
            Scanner myReader = new Scanner(f);
            while (myReader.hasNextLine()) {
                lines.add(myReader.nextLine());
            }
            lines.remove(0);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return lines;
    }

    ArrayList<Puyo[][]> readBoards(ArrayList<String> lines){
        ArrayList<Puyo[][]> boardList = new ArrayList<>();
        for (String line: lines) {
            Puyo[][] board = new Puyo[6][13];
            String[] temp = line.split(",");
            String[] cols = new String[6];
            for (int i = 0; i < 6; i++) {
                cols[i] = temp[i];
            }
            for (int i = 0; i < cols.length; i++) {
                String col = cols[i];
                ArrayList<Column> runs = new ArrayList<>();
                String number = "";
                for (char c : col.toCharArray()) {
                    if (Character.isDigit(c)) {
                        number += c;
                    } else {
                        runs.add(new Column(Integer.parseInt(number), decodeColour(c)));
                        number = "";
                    }
                }
                int progressDownCol = board[0].length - 1;
                for (Column run : runs) {
                    for (int j = progressDownCol; j > progressDownCol - run.number; j--) {
                        board[i][j] = run.type;
                    }
                    progressDownCol -= run.number;
                }
            }
            boardList.add(board);
        }
        return boardList;
    }

    ArrayList<ArrayList<int[]>> readRecentlyDropped(ArrayList<String> lines){
        ArrayList<ArrayList<int[]>> recentlyDroppedList = new ArrayList<>();
        for (String line: lines){
            ArrayList<int[]> recentlyDropped = new ArrayList<>();
            String[] temp = line.split(",");
            String recDropCol = temp[6];
            if (!recDropCol.equals("-")){
                String[] posS = recDropCol.split(" ");
                for (int i = 0; i < posS.length; i += 2) {
                    int[] pos = new int[]{Integer.parseInt(posS[i]), Integer.parseInt(posS[i + 1])};
                    recentlyDropped.add(pos);
                }
            }
            recentlyDroppedList.add(recentlyDropped);
        }
        return recentlyDroppedList;
    }

    private ArrayList<Integer> readTargetScores(ArrayList<String> lines){
        ArrayList<Integer> targetScores = new ArrayList<>();
        for (String line: lines){
            targetScores.add(Integer.parseInt(line.split(",")[7]));
        }
        return targetScores;
    }

    private void processFile(File f){
        ArrayList<String> lines = readFile(f);

        this.boards = readBoards(lines);
        this.recentlyDropped = readRecentlyDropped(lines);
        this.targetScores = readTargetScores(lines);
    }
    private Puyo decodeColour(char c){
        switch(c){
            case '_':
                return null;
            case 'N':
                return PuyoI.createGarbage();
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

    // Tests to implement:
    // Popping neighbouring garbage
    // Garbage system stuff:
    // - Correctly displacing incoming garbage if there's a chain
    // - Correctly calculating how much garbage
}
