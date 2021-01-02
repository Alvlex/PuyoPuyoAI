package app;

import java.util.ArrayList;
import java.util.List;

public interface ChainI {
    void resetChain(); // Don't need tests
    boolean isPopping(List<Coordinate> recentlyDropped);
    boolean isPopping();
    ArrayList<Coordinate> chainTurn(ArrayList<Coordinate> recentlyDropped);
    // Returns the next recentlyDropped Puyo
    int score();
    int chainLength(); // Don't need tests
    void runChain(ArrayList<Coordinate> recDrop);
    ArrayList<ArrayList<Coordinate>> findPops(List<Coordinate> recentlyDropped);
}
