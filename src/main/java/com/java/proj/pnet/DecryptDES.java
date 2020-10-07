package com.java.proj.pnet;

import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;

public class DecryptDES {

    
    public static String decrypt(String str) {

        Cipher dcipher;
        SecretKey key;

        try {

            // decode with base64 to get bytes


            String password = "abcd1234";
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            key = keyFactory.generateSecret(new DESKeySpec(password.getBytes()));
            dcipher = Cipher.getInstance("DES");
            dcipher.init(Cipher.DECRYPT_MODE, key);

            // byte[] dec = BASE64DecoderStream.decode(str.getBytes());
            byte[] dec = Base64.getDecoder().decode(str);
            byte[] utf8 = dcipher.doFinal(dec);

            // create new string based on the specified charset

            return new String(utf8, "UTF8");

        }

        catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    public static String encrypt(String str) {

        Cipher ecipher;
        SecretKey key;

        try {

            // decode with base64 to get bytes


            String password = "abcd1234";
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            key = keyFactory.generateSecret(new DESKeySpec(password.getBytes()));
            ecipher = Cipher.getInstance("DES");
            ecipher.init(Cipher.ENCRYPT_MODE, key);
 
            // byte[] dec = Base64.getEncoder().encode(str.getBytes());
            byte[] utf8 = str.getBytes("UTF8");
            byte[] enc = ecipher.doFinal(utf8);

            // create new string based on the specified charset

            // return new String(utf8, "UTF8");
            return new String(Base64.getEncoder().encode(enc));

        }

        catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

}