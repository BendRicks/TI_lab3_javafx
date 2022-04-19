package edu.busir.ti_lab3_javafx.logic;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

public class Decryptor {

    private final ElgamalClosedKey key;

    public Decryptor(long p, long x){
        key = new ElgamalClosedKey(p, x);
    }

    public void decryptFile(File fileToOpen, File fileToSave) throws IOException {
        var fis = new DataInputStream(new FileInputStream(fileToOpen));
        var fos = new FileWriter(fileToSave);
        var ffios = new BufferedWriter(new FileWriter("bin\\code.txt"));
        while (fis.available() > 0) {
            long a = fis.readLong();
            long b = fis.readLong();
            BigInteger m = new BigInteger(String.valueOf(b));
            BigInteger ax = ArithmeticOperations.bigIntegerExp(a, key.getP() - 1 - key.getX());
            m = m.multiply(ax);
            m = m.mod(new BigInteger(String.valueOf(key.getP())));
            fos.write(m.byteValue());
            ffios.write(m.byteValue() + " ");
        }
        fis.close();
        fos.close();
        ffios.close();
    }

}
