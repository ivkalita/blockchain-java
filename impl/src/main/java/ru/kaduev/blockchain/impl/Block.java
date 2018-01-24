package ru.kaduev.blockchain.impl;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Block {
    private Date timestamp;
    private byte[] data;
    private byte[] prevBlockHash;
    private byte[] hash;

    Block(String data, byte[] prevBlockHash) {
        this.timestamp = new Date();
        this.prevBlockHash = new byte[prevBlockHash.length];
        System.arraycopy(prevBlockHash, 0, this.prevBlockHash, 0, prevBlockHash.length);

        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        this.data = new byte[dataBytes.length];
        System.arraycopy(dataBytes, 0, this.data, 0, dataBytes.length);

        this.setHash();
    }

    Block() {
        this("Genesis block", new byte[32]);
    }

    private void setHash() {
        ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE + this.prevBlockHash.length + this.data.length);
        buffer.put(this.prevBlockHash);
        buffer.putLong(this.timestamp.getTime());
        buffer.put(this.data);
        try {
            this.hash = MessageDigest.getInstance("SHA-256").digest(buffer.array());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No SHA-256 algorithm provider found.");
        }


    }

    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        return "Timestamp: " + df.format(this.timestamp.getTime()) + "\n" +
                "P. Hash: " + HexHelper.printHexBinary(this.prevBlockHash) + "\n" +
                "C. Hash: " + HexHelper.printHexBinary(this.hash) + "\n" +
                "Data: " + new String(this.data) + "\n";
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public byte[] getData() {
        return data;
    }

    public byte[] getPrevBlockHash() {
        return prevBlockHash;
    }

    public byte[] getHash() {
        return hash;
    }
}
