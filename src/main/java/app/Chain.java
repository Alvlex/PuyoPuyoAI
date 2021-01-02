package app;

import java.util.ArrayList;
import java.util.List;

public class Chain implements ChainI{

    private Board board;
    private ArrayList<Integer> chain;

    public Chain(Board board){
        this.board = board;
        chain = new ArrayList<>();
    }

    @Override
    public void resetChain(){
        chain.clear();
    }

    private ArrayList<Coordinate> findAdjacent(Coordinate c){
        return findAdjacent(c.getX(), c.getY());
    }

    private ArrayList<Coordinate> findAdjacent(int col, int row){
        ArrayList<Coordinate> result = new ArrayList<>();
        if (col > 0){
            if (board.getPuyo(col - 1, row) != null){
                result.add(new Coordinate(col - 1, row));
            }
        }
        if (col < board.getNoCols() - 1){
            if (board.getPuyo(col + 1, row) != null){
                result.add(new Coordinate(col + 1, row));
            }
        }
        if (row > 0){
            if (board.getPuyo(col, row - 1) != null){
                result.add(new Coordinate(col, row - 1));
            }
        }
        if (row < board.getNoRows() - 1){
            if (board.getPuyo(col, row + 1) != null){
                result.add(new Coordinate(col, row + 1));
            }
        }
        return result;
    }

    @Override
    public ArrayList<ArrayList<Coordinate>> findPops(List<Coordinate> recentlyDropped){
        ArrayList<ArrayList<Coordinate>> groupsThatPop = new ArrayList<>();
        for (Coordinate pos: recentlyDropped){
            boolean skip = false;
            for (ArrayList<Coordinate> group: groupsThatPop){
                if (contains(group, pos)){
                    skip = true;
                    break;
                }
            }
            try {
                if (skip || board.getPuyo(pos).getColour().equals("GREY")) continue;
            }
            catch(NullPointerException e){
                String message = new Output(new Board[]{board}).printBoards();
                message += "\n" + pos.getX() + ", " + pos.getY();
                throw new RuntimeException(message);
            }
            ArrayList<Coordinate> connected = new ArrayList<>();
            connected.add(pos);
            ArrayList<Coordinate> toCheck = findAdjacent(pos);
            while(!toCheck.isEmpty()){
                Coordinate currentCheck = toCheck.remove(0);
                if (board.getPuyo(currentCheck).getColour().equals(board.getPuyo(pos).getColour()) && !contains(connected, currentCheck)){
                    connected.add(currentCheck);
                    toCheck.addAll(findAdjacent(currentCheck));
                }
            }
            groupsThatPop.add(connected);
        }
        return groupsThatPop;
    }

    private boolean contains(ArrayList<Coordinate> array, Coordinate check){
        for (Coordinate itemList: array){
            if (itemList.getX() == check.getX() && itemList.getY() == check.getY())
                return true;
        }
        return false;
    }

    @Override
    public boolean isPopping(List<Coordinate> recentlyDropped){
        return popIncoming(findPops(recentlyDropped));
    }

    @Override
    public boolean isPopping(){
        return popIncoming(findPops(board.findAllPuyo()));
    }

    private boolean popIncoming(ArrayList<ArrayList<Coordinate>> groups){
        for (ArrayList<Coordinate> group: groups){
            if (group.size() >= 4){
                return true;
            }
        }
        return false;
    }

    private void popGroup(ArrayList<Coordinate> group){
        for (Coordinate coords: group){
            board.removePuyo(coords);
            for(Coordinate pos: findAdjacent(coords)){
                if (board.getPuyo(pos).getColour().equals("GREY")){
                    board.removePuyo(pos);
                }
            }
        }
    }

    public ArrayList<Coordinate> chainTurn(){
        return chainTurn(board.findAllPuyo());
    }

    @Override
    public ArrayList<Coordinate> chainTurn(ArrayList<Coordinate> recentlyDropped){
        ArrayList<ArrayList<Coordinate>> groups = findPops(recentlyDropped);
        int totalPuyo = 0;
        for (ArrayList<Coordinate> group: groups){
            if (group.size() >= 4){
                totalPuyo += group.size();
                popGroup(group);
            }
        }
        chain.add(totalPuyo);
        return board.cascadePuyo();
    }

    @Override
    public int score(){
        int accumulator = 0;
        for (int i = 0; i < chain.size(); i ++){
            accumulator += (i + 1) * (chain.get(i) - 4);
        }
        return accumulator + 3 * (chain.size() - 1) * chain.size();
    }

    @Override
    public int chainLength(){
        return chain.size();
    }

    @Override
    public void runChain(ArrayList<Coordinate> recDrop){
        while(isPopping(recDrop)){
            recDrop = chainTurn(recDrop);
        }
    }

}
