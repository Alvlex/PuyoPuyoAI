package app;

import AI.Strategy;

import java.util.List;

public interface PlayerI {
    Move turn(Board oppBoard);
    List<Coordinate> findRecentlyDropped(Move m);
}
