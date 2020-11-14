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

    private ArrayList<int[]> findAdjacent(int col, int row){
        ArrayList<int[]> result = new ArrayList<>();
        if (col > 0){
            if (board.getPuyo(col - 1, row) != null){
                result.add(new int[]{col - 1, row});
            }
        }
        if (col < board.getNoCols() - 1){
            if (board.getPuyo(col + 1, row) != null){
                result.add(new int[]{col + 1, row});
            }
        }
        if (row > 0){
            if (board.getPuyo(col, row - 1) != null){
                result.add(new int[]{col, row - 1});
            }
        }
        if (row < board.getNoRows() - 1){
            if (board.getPuyo(col, row + 1) != null){
                result.add(new int[]{col, row + 1});
            }
        }
        return result;
    }

    private ArrayList<ArrayList<int[]>> findPops(List<int[]> recentlyDropped){
        ArrayList<ArrayList<int[]>> groupsThatPop = new ArrayList<>();
        for (int[] pos: recentlyDropped){
            boolean skip = false;
            for (ArrayList<int[]> group: groupsThatPop){
                if (group.contains(pos)){
                    skip = true;
                    break;
                }
            }
            if (skip || board.getPuyo(pos).getColour().equals("GREY")) continue;
            ArrayList<int[]> connected = new ArrayList<>();
            connected.add(pos);
            ArrayList<int[]> toCheck = findAdjacent(pos[0], pos[1]);
            while(!toCheck.isEmpty()){
                int[] currentCheck = toCheck.remove(0);
                if (board.getPuyo(currentCheck).colour == board.getPuyo(pos).colour && !contains(connected, currentCheck)){
                    connected.add(currentCheck);
                    toCheck.addAll(findAdjacent(currentCheck[0], currentCheck[1]));
                }
            }
            groupsThatPop.add(connected);
        }
        return groupsThatPop;
    }

    private boolean contains(ArrayList<int[]> array, int[] check){
        for (int[] itemList: array){
            if (itemList.length != check.length)
                continue;
            int noOfMatches = 0;
            for (int i = 0; i < itemList.length; i ++){
                if (itemList[i] == check[i])
                    noOfMatches ++;
            }
            if (noOfMatches == check.length)
                return true;
        }
        return false;
    }

    @Override
    public boolean isPopping(List<int[]> recentlyDropped){
        return popIncoming(findPops(recentlyDropped));
    }

    private boolean popIncoming(ArrayList<ArrayList<int[]>> groups){
        for (ArrayList<int[]> group: groups){
            if (group.size() >= 4){
                return true;
            }
        }
        return false;
    }

    private void popGroup(ArrayList<int[]> group){
        for (int[] coords: group){
            board.removePuyo(coords);
        }
    }

    @Override
    public ArrayList<int[]> chainTurn(ArrayList<int[]> recentlyDropped){
        ArrayList<ArrayList<int[]>> groups = findPops(recentlyDropped);
        int totalPuyo = 0;
        for (ArrayList<int[]> group: groups){
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
        int output = accumulator + 3 * (chain.size() - 1) * chain.size();
        return output;
    }

    @Override
    public int chainLength(){
        return chain.size();
    }

}
