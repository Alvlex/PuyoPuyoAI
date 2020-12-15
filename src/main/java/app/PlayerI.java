package app;

import AI.Strategy;

import java.util.List;

public interface PlayerI {
    Move turn(Board oppBoard);
    List<int[]> findRecentlyDropped(Move m);
}
