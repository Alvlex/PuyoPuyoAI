package app;

import java.util.ArrayList;
import java.util.List;

public interface ChainI {
    void resetChain(); // Don't need tests
    boolean isPopping(List<int[]> recentlyDropped);
    ArrayList<int[]> chainTurn(ArrayList<int[]> recentlyDropped);
    int score();
    int chainLength(); // Don't need tests
}
