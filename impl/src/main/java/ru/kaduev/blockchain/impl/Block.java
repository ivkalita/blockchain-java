package ru.kaduev.blockchain.impl;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Block implements Serializable {
    private Date timestamp;
    private Transaction[] transactions;
    private byte[] prevBlockHash;
    private byte[] hash;
    private long nonce;

    Block(Transaction[] transactions, byte[] prevBlockHash) {
        this.timestamp = new Date();
        this.prevBlockHash = prevBlockHash;
        this.transactions = transactions;

        ProofOfWork.Proof proof = new ProofOfWork(this).run();

        this.hash = proof.hash;
        this.nonce = proof.nonce;
    }

    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("Block: %s\n", HexHelper.printHexBinary(hash)))
                .append(String.format("Timestamp: %s\n", df.format(timestamp.getTime())))
                .append(String.format("Previous hash: %s\n", HexHelper.printHexBinary(prevBlockHash)))
                .append(String.format("Nonce: %s\n", nonce))
                .append("Transactions:\n");
        for (final Transaction transaction : transactions) {
            builder.append(transaction);
        }

        return builder.toString();
    }

    public byte[] getBytes() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream stream = new ObjectOutputStream(out);
            stream.writeObject(this);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Block fromBytes(byte[] bytes) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ObjectInputStream stream = new ObjectInputStream(in);
            return (Block) stream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public byte[] hashTransactions() {
        ByteBuffer hashes = ByteBuffer.allocate(transactions.length * 32);
        for (final Transaction transaction : transactions) {
            hashes.put(HexHelper.hash(transaction.getId()));
        }

        return HexHelper.hash(hashes);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Transaction[] getTransactions() {
        return this.transactions;
    }

    public byte[] getPrevBlockHash() {
        return prevBlockHash;
    }

    public byte[] getHash() {
        return hash;
    }

    public long getNonce() {
        return nonce;
    }
}
