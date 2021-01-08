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

    public Board b;

    public Template(String file){
        b = readBoard("templates/" + file);
    }
    public Template(String directory, String file){
        b = readBoard(directory + "/" + file);
    }

    private Board readBoard(String file){
        File f = getFileFromResource(file);
        Puyo[][] board = new Puyo[6][13];
        try (FileReader fr = new FileReader(f);
            CSVReader reader = new CSVReader(fr)) {
            String[] nextLine;
            int row = 12;
            while ((nextLine = reader.readNext()) != null && row >= 0) {
                for (int i = 0; i < nextLine.length; i ++) {
                    Puyo temp = decodeColour(nextLine[i].charAt(nextLine[i].length() - 1));
                    board[i][row] = temp;
                }
                row --;
            }
        } catch (Exception e) {
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

    private Puyo decodeColour(char c) throws Exception {
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
        throw new Exception("Invalid Character " + c);
    }

    public Board getBoard(){
        return b.copyBoard();
    }

    public boolean equalBoards(Board b2){
        for (int i = 0; i < 6; i ++){
            for (int j = 0; j < 13; j ++){
                if (b.getPuyo(i,j) == null || b2.getPuyo(i,j) == null){
                    if(b.getPuyo(i,j) != b2.getPuyo(i,j)){
                        return false;
                    }
                }
                else if (!b.getPuyo(i,j).getColour().equals(b2.getPuyo(i,j).getColour())){
                    return false;
                }
            }
        }
        return true;
    }

    // Tests to implement:
    // Popping neighbouring garbage
    // Garbage system stuff:
    // - Correctly displacing incoming garbage if there's a chain
    // - Correctly calculating how much garbage
}
