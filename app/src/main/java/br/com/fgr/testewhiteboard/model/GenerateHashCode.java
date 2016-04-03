package br.com.fgr.testewhiteboard.model;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class GenerateHashCode {

    private GenerateHashCode() {

    }

    public static String hashCode(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.reset();
        byte[] buffer = text.getBytes("UTF-8");
        md.update(buffer);
        byte[] digest = md.digest();

        String hexStr = "";

        for (int i = 0; i < digest.length; i++)
            hexStr += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);

        return hexStr;

    }

}