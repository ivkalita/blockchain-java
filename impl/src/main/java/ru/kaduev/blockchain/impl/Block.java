package ru.kaduev.blockchain.impl;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Block {
    private Date timestamp;
    private byte[] data;
    private byte[] prevBlockHash;
    private byte[] hash;
    private long nonce;

    Block(String data, byte[] prevBlockHash) {
        this.timestamp = new Date();
        this.prevBlockHash = new byte[prevBlockHash.length];
        System.arraycopy(prevBlockHash, 0, this.prevBlockHash, 0, prevBlockHash.length);

        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        this.data = new byte[dataBytes.length];
        System.arraycopy(dataBytes, 0, this.data, 0, dataBytes.length);

        ProofOfWork.Proof proof = new ProofOfWork(this).run();
        this.hash = proof.hash;
        this.nonce = proof.nonce;
    }

    Block() {
        this("Genesis block", new byte[32]);
    }

    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        return String.format(
                "Timestamp: %s\nP. Hash: %s\nC. Hash: %s\nData: %s\nNonce: %s\n",
                df.format(this.timestamp.getTime()),
                HexHelper.printHexBinary(this.prevBlockHash),
                HexHelper.printHexBinary(this.hash),
                new String(this.data),
                nonce);
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
