package edu.busir.ti_lab3_javafx.logic;

public class ElgamalClosedKey {

    private final long p;
    private final long x;

    public ElgamalClosedKey(long p, long x){
        this.p = p;
        this.x = x;
    }

    public long getP() {
        return p;
    }

    public long getX() {
        return x;
    }

}
