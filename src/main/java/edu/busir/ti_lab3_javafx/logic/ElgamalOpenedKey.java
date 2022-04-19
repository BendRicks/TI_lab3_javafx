package edu.busir.ti_lab3_javafx.logic;

public class ElgamalOpenedKey {

    private final long p;
    private final long g;
    private final long y;

    public ElgamalOpenedKey(long p, long g, long y){
        this.p = p;
        this.g = g;
        this.y = y;
    }

    public long getP() {
        return p;
    }

    public long getG() {
        return g;
    }

    public long getY() {
        return y;
    }
}
