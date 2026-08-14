package com.unimarket.unimarketintegradora.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
    public static String convertirSHA256(String contrasena) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(contrasena.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear la contraseña", e);
        }
    }
}