package ru.kaduev.blockchain.impl;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HexHelper {
    private static final String HEXES = "0123456789abcdef";
    static final int HASH_SIZE = 32;

    public static String printHexBinary(byte[] binary) {
        if (binary == null) {
            return null;
        }
        final int len = Math.max(binary.length, HASH_SIZE);
        final StringBuilder hex = new StringBuilder(2 * len);
        if (len > binary.length) {
            for (int i = 0; i < len - binary.length; i++) {
                hex.append(0);
            }
        }
        for (int b : binary) {
            if (b < 0) {
                b = b + Math.abs(Byte.MIN_VALUE);
            }
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    public static byte[] hash(byte[] data) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No SHA-256 algorithm provider found.");
        }
    }

    public static byte[] hash(ByteBuffer buffer) {
        return HexHelper.hash(buffer.array());
    }

    public static boolean isNull(byte[] data) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] != 0) {
                return false;
            }
        }

        return true;
    }
}
