package app;

import AI.Strategy;

import java.util.List;

public interface PlayerI {
    Move turn();
    List<int[]> findRecentlyDropped(Move m);
}
