package edu.busir.ti_lab3_javafx.logic;

import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Cryptor {

    private final ElgamalOpenedKey key;
    private final long x;
    private final long k;
    private final long a;

    public Cryptor(long p, long x, long k, long g){
        this.x = x;
        this.k = k;
        key = new ElgamalOpenedKey(p, g, ArithmeticOperations.bigIntegerExp(g, x).mod(new BigInteger(String.valueOf(p))).longValue());
        a = ArithmeticOperations.bigIntegerExp(g, k).mod(new BigInteger(String.valueOf(p))).longValue();
    }

    public void cryptFile(File fileToOpen, File fileToSave) throws IOException {
        var fis = new BufferedInputStream(new FileInputStream(fileToOpen));
        var fos = new BufferedOutputStream(new FileOutputStream(fileToSave));
        while (fis.available() > 0) {
            byte[] buffer = new byte[8192];
            int bytesAmount = fis.read(buffer);
            short[] shorts = new short[bytesAmount * 2];
            byte[] result = new byte[shorts.length*2];
            for (int i = 0; i < bytesAmount; i++) {
                int bt = buffer[i] & 0xff;
                long b = (ArithmeticOperations.bigIntegerExp(key.getY(), k)
                        .multiply(new BigInteger(String.valueOf(bt)))
                        .mod(new BigInteger(String.valueOf(key.getP())))).longValue();
                shorts[i*2] = (short) a;
                shorts[i*2 + 1] = (short) b;
            }
            ByteBuffer.wrap(result).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(shorts);
            fos.write(result);
        }
        fis.close();
        fos.close();
    }

}
