package com.example.pizza_appproject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Hash {
    public static String hashPassword(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.reset();
        md.update(password.getBytes());
        byte[] mdArray = md.digest();
        StringBuilder sb = new StringBuilder(mdArray.length * 2);
        for (byte b : mdArray) {
            int v = b & 0xff;
            if (v < 16)
                sb.append('0');
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }
}


