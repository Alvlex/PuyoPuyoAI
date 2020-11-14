package app;

import AI.Strategy;

import java.util.List;

public interface PlayerI {
    Move turn(Strategy s);
    List<int[]> findRecentlyDropped(Move m);
}
