package Game;

public enum Colour {
    RED((byte) 31),
    GREEN((byte) 32),
    YELLOW((byte) 33),
    BLUE((byte) 34),
    MAGENTA((byte) 35),
    GREY((byte) 37),
    BLACK((byte) 30);

    public byte value;
    Colour(byte i) {
        this.value = i;
    }
}
