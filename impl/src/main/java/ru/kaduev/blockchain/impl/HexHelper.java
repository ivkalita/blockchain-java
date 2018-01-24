package ru.kaduev.blockchain.impl;

public class HexHelper {
    private static final String HEXES = "0123456789ABCDEF";

    public static String printHexBinary(byte[] binary) {
        if (binary == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * binary.length);
        for (final byte b : binary) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4))
                    .append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }
}
