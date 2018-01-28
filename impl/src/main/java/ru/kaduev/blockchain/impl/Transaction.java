package ru.kaduev.blockchain.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class Transaction implements Serializable {
    private byte[] id;
    private List<TransactionInput> inputs;
    private List<TransactionOutput> outputs;

    Transaction(List<TransactionInput> inputs, List<TransactionOutput> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
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

    public List<TransactionInput> getInputs() {
        return inputs;
    }

    public List<TransactionOutput> getOutputs() {
        return outputs;
    }

    public byte[] getId() {
        return id;
    }

    public boolean isCoinbase() {
        if (inputs.size() != 1) {
            return false;
        }
        TransactionInput first = inputs.get(0);
        return HexHelper.isNull(first.getTransactionId()) && first.getOutputIndex() == -1;
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
