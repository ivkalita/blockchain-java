package ru.kaduev.blockchain.impl;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Block {
    private Date timestamp;
    private byte[] data;
    private byte[] prevBlockHash;
    private byte[] hash;

    public Block(String data, byte[] prevBlockHash) throws NoSuchAlgorithmException {
        this.timestamp = new Date();
        this.prevBlockHash = new byte[prevBlockHash.length];
        System.arraycopy(prevBlockHash, 0, this.prevBlockHash, 0, prevBlockHash.length);

        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        this.data = new byte[dataBytes.length];
        System.arraycopy(dataBytes, 0, this.data, 0, dataBytes.length);

        this.setHash();
    }

    private void setHash() throws NoSuchAlgorithmException {
        ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE + this.prevBlockHash.length + this.data.length);
        buffer.put(this.prevBlockHash);
        buffer.putLong(this.timestamp.getTime());
        buffer.put(this.data);
        this.hash = MessageDigest.getInstance("SHA-256").digest(buffer.array());

    }

    public String toString() {
        return "P. Hash: " + HexHelper.printHexBinary(this.prevBlockHash) + "\n" +
                "C. Hash: " + HexHelper.printHexBinary(this.hash) + "\n" +
                "Data: " + new String(this.data) + "\n";
    }
}
