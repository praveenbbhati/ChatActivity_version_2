package com.users.bktc.user.brahmkumaristrafficcontrol.encryption;


import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
//import java.util.Base64;

/**
 * Created by Lakshay Juneja on 21,June,2018.
 * Contec Global Infotech Limited
 * lakshay.juneja@cg-infotech.com
 */
public class RSAUtil {

    public static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzjHd8r9tzpqc4Ws7PLt/qKiM+NgF+p/dPi0Qg5Okoew/IJI5Zl/hqgrZ4nS4kCAciKSOzfjse7yjMxYnkm3Nf4Z4YNWuv7+lG+TYsfzEDYsgg1e4dElFUqELtNi+JFw92nihqOesYqVoHBpBPzibQhvkdCvXPUF/LJkadEsVbqaSrKAxSe5vMOE+3w5a/CqQZoZ6j046K3LJb8ZAUw6FgLaBPfgGUC4Vv342QwT3PCc5NCkqI+DRk0/4r2sLbnYIQQ1ilCizAwOsEYfiKIlEAPS0Rsuoj6i5WZ49MXBAlmBSRmpbLKjwd6PynQ4ALvTxqjOUiKSsXg0OejcLCCcm5QIDAQAB";

    public static PublicKey getPublicKey(String base64PublicKey) {
        PublicKey publicKey = null;
        try {
            // X509EncodedKeySpec keySpec = new
            // X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(base64PublicKey, Base64.NO_WRAP));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public static PrivateKey getPrivateKey(String base64PrivateKey) {
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(base64PrivateKey, Base64.NO_WRAP));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public static byte[] encrypt(String data) throws BadPaddingException, IllegalBlockSizeException,
            InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return cipher.doFinal(data.getBytes());
    }

    public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }

    public static String decrypt(String data, String base64PrivateKey) throws IllegalBlockSizeException,
            InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(Base64.decode(data.getBytes(), Base64.NO_WRAP), getPrivateKey(base64PrivateKey));
    }
}
