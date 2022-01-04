package domain;

public enum Color {
    WHITE,
    BLACK;

    public Color opposite;

    static {
        WHITE.opposite = BLACK;
        BLACK.opposite = WHITE;
    }
}
