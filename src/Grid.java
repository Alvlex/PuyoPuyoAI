import java.awt.event.KeyEvent;
import java.util.Scanner;
import java.awt.event.KeyListener;

public class Grid {

    Puyo[][] board = new Puyo[6][13];

    // Returns true if successfully added, otherwise false
    private boolean addPuyoHelper(Puyo p){
        for (int row = 0; row < board[0].length; row ++){
            if (board[p.col][row] == null){
                board[p.col][row] = p;
                p.row = row;
                p.placed = true;
                return true;
            }
        }
        return checkEnd();
    }

    private void removePuyo(Puyo[] puyos){
        for (int i = 0; i < puyos.length; i ++){
            if (puyos[i].placed){
                puyos[i].placed = false;
                board[puyos[i].col][puyos[i].row] = null;
                if (puyos[1 - i].col == puyos[i].col){
                    puyos[i].row = 0;
                }
                else{
                    puyos[i].row = puyos[1 - i].row;
                }
            }
        }
    }

    public boolean checkEnd(){
        for (int col = 0; col < board.length; col ++){
            if (board[col][board[col].length - 1] == null){
                return false;
            }
        }
        return true;
    }

    public boolean addPuyo(Puyo[] puyos){
        for (int i = 0; i < 2; i ++){
            for (Puyo p: puyos){
                if (p.row == i && !p.placed){
                    if (!addPuyoHelper(p)){
                        removePuyo(puyos);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}