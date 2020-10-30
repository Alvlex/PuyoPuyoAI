public enum Attribute {
    RED(31),
    GREEN(32),
    YELLOW(33),
    BLUE(34),
    MAGENTA(35),
    GREY(37),
    BLACK(30);

    int value;
    Attribute(int i) {
        this.value = i;
    }
}
