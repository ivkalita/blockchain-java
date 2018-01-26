package ru.kaduev.blockchain.impl;

public class HexHelper {
    private static final String HEXES = "0123456789abcdef";

    public static String printHexBinary(byte[] binary) {
        if (binary == null) {
            return null;
        }
        final int len = Math.max(binary.length, 32);
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
}
