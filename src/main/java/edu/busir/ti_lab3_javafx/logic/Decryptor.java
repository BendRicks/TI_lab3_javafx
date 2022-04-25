package edu.busir.ti_lab3_javafx.logic;

import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Decryptor {

    private final ElgamalClosedKey key;

    public Decryptor(long p, long x){
        key = new ElgamalClosedKey(p, x);
    }

    public void decryptFile(File fileToOpen, File fileToSave) throws IOException {
        var fis = new BufferedInputStream(new FileInputStream(fileToOpen));
        var fos = new BufferedOutputStream(new FileOutputStream(fileToSave));
        while (fis.available() > 0) {
            byte[] buffer = new byte[8192];
            int shortsAmount = fis.read(buffer) / 2;
            short[] shorts = new short[shortsAmount];
            byte[] result = new byte[shorts.length/2];
            ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
            for (int i = 0; i < shortsAmount; i += 2) {
                long a = shorts[i];
                long b = shorts[i + 1];
                BigInteger m = new BigInteger(String.valueOf(b));
                BigInteger ax = ArithmeticOperations.bigIntegerExp(a, key.getP() - 1 - key.getX());
                m = m.multiply(ax);
                m = m.mod(new BigInteger(String.valueOf(key.getP())));
                byte bt = m.byteValue();
                result[i / 2] = bt;
            }
            fos.write(result);
        }
        fis.close();
        fos.close();
    }

}