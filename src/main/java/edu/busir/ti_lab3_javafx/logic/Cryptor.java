package edu.busir.ti_lab3_javafx.logic;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

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
        var fis = new FileInputStream(fileToOpen);
        var fos = new DataOutputStream(new FileOutputStream(fileToSave));
        var ffios = new BufferedWriter(new FileWriter("bin\\code.txt"));
        while (fis.available() > 0) {
            long b = (ArithmeticOperations.bigIntegerExp(key.getY(), k)
                    .multiply(new BigInteger(String.valueOf(fis.read())))
                    .mod(new BigInteger(String.valueOf(key.getP())))).longValue();
            ffios.write(a + "," + b + " ");
            fos.writeLong(a);
            fos.writeLong(b);
        }
        fis.close();
        fos.close();
        ffios.close();
    }

}
