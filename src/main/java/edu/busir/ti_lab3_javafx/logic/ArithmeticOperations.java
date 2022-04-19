package edu.busir.ti_lab3_javafx.logic;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

public class ArithmeticOperations {

    public static boolean isPrime(long value) {
        if (value == 2 || value == 3){
            return true;
        }
        if (value <= 1){
            return false;
        }
        for (long i = 2; i <= Math.sqrt(value); i++){
            if (value % i == 0){
                return false;
            }
        }
        return true;
    }

    public static long fastExpModulo(long value, long power, long module) {
        long result = 1;
        while (power != 0) {
            while (power % 2 == 0) {
                power = power / 2;
                value = (value * value) % module;
            }
            power = power - 1;
            result = (result * value) % module;
        }
        return result;
    }

    public static long fastExp(long value, long power){
        long result = 1;
        while (power != 0) {
            while (power % 2 == 0) {
                power = power / 2;
                value = value * value;
            }
            power = power - 1;
            result = result * value;
        }
        return result;
    }

    public static BigInteger bigIntegerExp(long value, long power){
        BigInteger val = new BigInteger(String.valueOf(value));
        int a = (int)power;
        val = val.pow(a);
        return val;
    }

    public static boolean isMutuallyPrime(long value1, long value2) {
        while (value1 != 0 && value2 != 0) {
            if (value1 > value2) {
                value1 = value1 % value2;
            } else {
                value2 = value2 % value1;
            }
        }
        return (value1 + value2) == 1;
    }

    public static ArrayList<Long> findPrimitiveRoots(long value) {
        ArrayList<Long> roots = new ArrayList<>();
        long phiFunc = value - 1;
        HashSet<Long> modSet = new HashSet<>();
        for (long i = 1; i <= value - 1; i++) {
            boolean IsError = false;
            for (long j = 1; j <= phiFunc; j++) {
                long modRes = fastExpModulo(i, j, value);

                if (modRes == 0 || modSet.contains(modRes)) {
                    IsError = true;
                    break;
                } else
                    modSet.add(modRes);
            }
            if (!IsError)
                roots.add(i);
            modSet.clear();
        }
        return roots;
    }

}