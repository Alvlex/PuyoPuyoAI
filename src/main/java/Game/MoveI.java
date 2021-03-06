package Game;

public interface MoveI {
    void left();
    void right();
    void rotClock();
    void rotAnti();
    Coordinate[] getCoord();
}
