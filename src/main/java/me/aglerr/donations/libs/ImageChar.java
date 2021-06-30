package me.aglerr.donations.libs;

public enum ImageChar {
    BLOCK('█'),
    DARK_SHADE('▓'),
    MEDIUM_SHADE('▒'),
    LIGHT_SHADE('░');

    private final char c;

    ImageChar(char Char) {
        this.c = Char;
    }

    public char getChar() {
        return this.c;
    }
}
