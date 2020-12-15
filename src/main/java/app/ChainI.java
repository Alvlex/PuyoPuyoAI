package app;

import java.util.ArrayList;
import java.util.List;

public interface ChainI {
    void resetChain(); // Don't need tests
    boolean isPopping(List<int[]> recentlyDropped);
    ArrayList<int[]> chainTurn(ArrayList<int[]> recentlyDropped);
    // Returns the next recentlyDropped Puyo
    int score();
    int chainLength(); // Don't need tests
    void runChain(ArrayList<int[]> recDrop);
}
