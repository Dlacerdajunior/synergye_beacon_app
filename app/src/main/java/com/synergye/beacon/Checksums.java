package com.synergye.beacon;

import java.util.zip.Checksum;

//Adler32

class Checksums {


    public static String chkSumAdler32(byte[] buffer, int bufferSize, int numBytes) {
        int modAdler = 65521;
        int a = 1;
        int b = 0;
        for (int x = 0; x < bufferSize; x++) {
            a = (a + buffer[x]) % modAdler;
            b = (b + a) % modAdler;
        }
        b = b << 16;
        int retInt = b | a;
        String retStr = Integer.toHexString(retInt);
        retStr = lpad(retStr, "0", numBytes);
        retStr = retStr.toUpperCase();
        return retStr.substring(retStr.length()-numBytes,retStr.length());
    }

    private static String lpad(String s, String pad, int plength) {
        for (int i = s.length(); i < plength; i++) {
            s = pad + s;
        }
        return s;
    }



}