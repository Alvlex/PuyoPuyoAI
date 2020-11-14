package app;

public enum Colour {
    RED(31),
    GREEN(32),
    YELLOW(33),
    BLUE(34),
    MAGENTA(35),
    GREY(37),
    BLACK(30);

    int value;
    Colour(int i) {
        this.value = i;
    }
}
