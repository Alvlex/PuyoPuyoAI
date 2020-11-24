package appTest;

import app.*;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import com.opencsv.CSVReader;

public class Template {

    Board b;

    public Template(String file){
        b = readBoard(file);
    }

    private Board readBoard(String file){
        File f = getFileFromResource("templates/" + file);
        Puyo[][] board = new Puyo[6][13];
        try (FileReader fr = new FileReader(f);
            CSVReader reader = new CSVReader(fr)) {
            String[] nextLine;
            int row = 12;
            while ((nextLine = reader.readNext()) != null && row >= 0) {
                for (int i = 0; i < nextLine.length; i ++) {
                    board[i][row] = decodeColour(nextLine[i].charAt(0));
                }
                row --;
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return new Board(board);
    }

    private File getFileFromResource(String fileName) {

        ClassLoader classLoader = Template.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {

            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());

            try {
                return new File(resource.toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return null;
            }
        }
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

    ArrayList<int[]> getRecentlyDropped(){
        ArrayList<int[]> result = new ArrayList<>();
        for (int i = 0; i < b.getNoCols(); i ++){
            for (int j = 0; j < b.getNoRows(); j ++){
                if (b.getPuyo(i,j) != null){
                    result.add(new int[]{i,j});
                }
            }
        }
        return result;
    }

    Board getBoard(){
        return b.copyBoard();
    }

    // Tests to implement:
    // Popping neighbouring garbage
    // Garbage system stuff:
    // - Correctly displacing incoming garbage if there's a chain
    // - Correctly calculating how much garbage
}
