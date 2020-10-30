import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Turn {
    Puyo[] currentPuyo;
    Grid grid;
    boolean popping = false;
    ArrayList<Integer> chain = new ArrayList<>();

    public Turn(Grid g){
        currentPuyo = Puyo.createInitialPuyo();
        grid = g;
    }

    public void moveLeft(Puyo[] puyos){
        for (Puyo p: puyos){
            if (p.col == 0){
                return;
            }
        }
        for (Puyo p: puyos){
            p.col -= 1;
        }
    }

    public void moveRight(Puyo[] puyos){
        for (Puyo p: puyos){
            if (p.col == 5){
                return;
            }
        }
        for (Puyo p: puyos){
            p.col += 1;
        }
    }
    public void rotateClock(Puyo[] puyos){
        if (puyos[1].col == puyos[0].col){
            // Second puyo is below
            if (puyos[1].row < puyos[0].row){
                puyos[1].row ++;
                puyos[1].col --;
            }
            // Second puyo is above
            else{
                puyos[1].col ++;
                puyos[1].row --;
            }
        }
        else{
            // Second puyo is to the left
            if (puyos[1].col < puyos[0].col){
                puyos[1].col ++;
                puyos[1].row ++;
            }
            // Second puyo is to the right
            else{
                puyos[1].col --;
                puyos[1].row --;
            }
        }
        if (puyos[1].col < 0){
            moveRight(puyos);
        }
        else if (puyos[1].col > 5){
            moveLeft(puyos);
        }
        if (puyos[1].row < 0){
            puyos[1].row ++;
            puyos[0].row ++;
        }
        else if (puyos[1].row > 1){
            puyos[1].row --;
            puyos[0].row --;
        }
    }

    public void rotateAnti(Puyo[] puyos){
        if (puyos[1].col == puyos[0].col){
            // Second puyo is below
            if (puyos[1].row < puyos[0].row){
                puyos[1].row ++;
                puyos[1].col ++;
            }
            // Second puyo is above
            else{
                puyos[1].col --;
                puyos[1].row --;
            }
        }
        else{
            // Second puyo is to the left
            if (puyos[1].col < puyos[0].col){
                puyos[1].col ++;
                puyos[1].row --;
            }
            // Second puyo is to the right
            else{
                puyos[1].col --;
                puyos[1].row ++;
            }
        }
        if (puyos[1].col < 0){
            moveRight(puyos);
        }
        else if (puyos[1].col > 5){
            moveLeft(puyos);
        }
        if (puyos[1].row < 0){
            puyos[1].row ++;
            puyos[0].row ++;
        }
        else if (puyos[1].row > 1){
            puyos[1].row --;
            puyos[0].row --;
        }
    }

    private Puyo[] getMovablePuyo(){
        return new Puyo[]{currentPuyo[0], currentPuyo[1]};
    }


    public void turn(){
        Scanner x = new Scanner(System.in);
        int input = 0;
        while(true) {
            while (input != 5) {
                Output.printCurrentPuyo(currentPuyo);
                Output.printBoard(grid.board);
                System.out.println("Pick an action:\n(1) Move left\n(2) Move right\n(3) Rotate clockwise\n" +
                        "(4) Rotate counter-clockwise\n(5) End turn");
                input = x.nextInt();
                switch (input) {
                    case 1:
                        moveLeft(getMovablePuyo());
                        break;
                    case 2:
                        moveRight(getMovablePuyo());
                        break;
                    case 3:
                        rotateClock(getMovablePuyo());
                        break;
                    case 4:
                        rotateAnti(getMovablePuyo());
                        break;
                }
            }
            input = 0;
            if (grid.addPuyo(getMovablePuyo())) break;
            else{
                System.out.println("Can't place puyo there!");
            }
        }
    }

    private ArrayList<Puyo> findAdjacent(Puyo p){
        ArrayList<Puyo> result = new ArrayList<>();
        if (p.col > 0){
            if (grid.board[p.col - 1][p.row] != null){
                result.add(grid.board[p.col - 1][p.row]);
            }
        }
        if (p.col < grid.board.length - 1){
            if (grid.board[p.col + 1][p.row] != null){
                result.add(grid.board[p.col + 1][p.row]);
            }
        }
        if (p.row > 0){
            if (grid.board[p.col][p.row - 1] != null){
                result.add(grid.board[p.col][p.row - 1]);
            }
        }
        if (p.row < grid.board[0].length - 1){
            if (grid.board[p.col][p.row + 1] != null){
                result.add(grid.board[p.col][p.row + 1]);
            }
        }
        return result;
    }

    private ArrayList<ArrayList<Puyo>> findPops(List<Puyo> recentlyDropped){
        ArrayList<ArrayList<Puyo>> groupsThatPop = new ArrayList<>();
        for (Puyo p: recentlyDropped){
            boolean skip = false;
            for (ArrayList<Puyo> group: groupsThatPop){
                if (group.contains(p)){
                    skip = true;
                    break;
                }
            }
            if (skip) continue;
            ArrayList<Puyo> connected = new ArrayList<>();
            connected.add(p);
            ArrayList<Puyo> toCheck = findAdjacent(p);
            while(!toCheck.isEmpty()){
                Puyo currentCheck = toCheck.remove(0);
                if (currentCheck.colour == p.colour && !connected.contains(currentCheck)){
                    connected.add(currentCheck);
                    toCheck.addAll(findAdjacent(currentCheck));
                }
            }
            groupsThatPop.add(connected);
        }
        return groupsThatPop;
    }

    private boolean popIncoming(ArrayList<ArrayList<Puyo>> groups){
        for (ArrayList<Puyo> group: groups){
            if (group.size() >= 4){
                return true;
            }
        }
        return false;
    }


    private void getNextPuyo(){
        currentPuyo[0] = currentPuyo[2];
        currentPuyo[1] = currentPuyo[3];
        currentPuyo[2] = currentPuyo[4];
        currentPuyo[3] = currentPuyo[5];
        for (int i = 0; i < 2; i ++){
            currentPuyo[i].col = 2;
        }
        for (int i = 2; i < 4; i ++){
            currentPuyo[i].col = 6;
        }
        Puyo[] p56 = Puyo.create2Puyo();
        currentPuyo[4] = p56[0];
        currentPuyo[5] = p56[1];
    }

    private void popGroup(ArrayList<Puyo> group){
        for (Puyo p: group){
            grid.board[p.col][p.row] = null;
        }
    }

    private ArrayList<Puyo> cascadePuyo(){
        ArrayList<Puyo> dropped = new ArrayList<>();
        for (int col = 0; col < grid.board.length; col ++){
            int noOfSpaces = 0;
            for (int row = 0; row < grid.board[col].length; row ++){
                if (grid.board[col][row] == null){
                    noOfSpaces ++;
                }
                else if (noOfSpaces > 0){
                    for (int i = row; i < grid.board[col].length; i ++){
                        grid.board[col][i - noOfSpaces] = grid.board[col][i];
                        grid.board[col][i] = null;
                        if (grid.board[col][i - noOfSpaces] != null) {
                            dropped.add(grid.board[col][i - noOfSpaces]);
                            grid.board[col][i - noOfSpaces].row = i - noOfSpaces;
                        }
                    }
                    noOfSpaces = 0;
                    row -= noOfSpaces;
                }
            }
        }
        return dropped;
    }

    private ArrayList<Puyo> chainTurn(ArrayList<Puyo> recentlyDropped){
        Scanner x = new Scanner(System.in);
        int input = 0;
        Output.printBoard(grid.board);
        while(input != 5) {
            System.out.println("Pick an action:\n(5) End turn");
            input = x.nextInt();
        }
        ArrayList<ArrayList<Puyo>> groups = findPops(recentlyDropped);
        int totalPuyo = 0;
        for (ArrayList<Puyo> group: groups){
            if (group.size() >= 4){
                totalPuyo += group.size();
                popGroup(group);
            }
        }
        chain.add(totalPuyo);
        return cascadePuyo();
    }

    private int score(ArrayList<Integer> chain){
        int accumulator = 0;
        for (int i = 0; i < chain.size(); i ++){
            accumulator += (i + 1) * (chain.get(i) - 4);
        }
        return accumulator + 3 * (chain.size() - 1) * chain.size();
    }

    public void playGame(){
        ArrayList<Puyo> recentlyDropped = new ArrayList<>();
        int chainNo = 0;
        while(!grid.checkEnd()) {
            if (!popping) {
                chainNo = 0;
                getNextPuyo();
                turn();
                popping = popIncoming(findPops(Arrays.asList(getMovablePuyo())));
                recentlyDropped.clear();
                recentlyDropped.addAll(Arrays.asList(getMovablePuyo()));
            }
            else{
                System.out.println(++chainNo + "-Chain!");
                // Implement chaining
                recentlyDropped = chainTurn(recentlyDropped);
                popping = popIncoming(findPops(recentlyDropped));
                if (!popping){
                    // Do something with score
                    score(chain);
                    chain.clear();
                }
            }
        }
        Output.printBoard(grid.board);
        System.out.println("GAME OVER");
    }

    public static void main(String[] args){
        Turn t = new Turn(new Grid());
        t.playGame();
    }

}
