package ru.kaduev.blockchain.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Transaction implements Serializable {
    private byte[] id;
    private TransactionInput[] inputs;
    private TransactionOutput[] outputs;

    private static final long SUBSIDY = 50;

    Transaction(String to, String data) {
        if (data == null) {
            data = String.format("Reward to %s", to);
        }

        TransactionInput input = new TransactionInput(new byte[HexHelper.HASH_SIZE], -1, data);
        TransactionOutput output = new TransactionOutput(SUBSIDY, to);
        this.inputs = new TransactionInput[]{input};
        this.outputs = new TransactionOutput[]{output};
        setId();
    }

    private void setId() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream stream = new ObjectOutputStream(out);
            stream.writeObject(this);
            id = HexHelper.hash(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public byte[] getId() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("Transaction: %s\n", HexHelper.printHexBinary(id)));
        for (final TransactionInput input : inputs) {
            builder.append(String.format("%s\n", input));
        }
        for (final TransactionOutput output : outputs) {
            builder.append(String.format("%s\n", output));
        }

        return builder.toString();
    }
}
