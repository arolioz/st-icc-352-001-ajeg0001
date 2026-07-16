package Util;

import org.jasypt.util.text.AES256TextEncryptor;

public class Encriptador {
    private static final AES256TextEncryptor encryptor = new AES256TextEncryptor();

    static {
        encryptor.setPassword("MyEncryptedPasswordP#3");
    }

    public static String encrypt(String texto) {
        return encryptor.encrypt(texto);
    }

    public static String decrypt(String texto) {
        return encryptor.decrypt(texto);
    }
}
