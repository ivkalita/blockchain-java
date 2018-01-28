package ru.kaduev.blockchain.impl;

import java.io.Serializable;

public class TransactionInput implements Serializable {
    private byte[] transactionId;
    private int outputIndex;
    private String scriptSignature;

    TransactionInput(byte[] transactionId, int outputIndex, String scriptSignature) {
        this.transactionId = transactionId;
        this.outputIndex = outputIndex;
        this.scriptSignature = scriptSignature;
    }

    public byte[] getTransactionId() {
        return transactionId;
    }

    public int getOutputIndex() {
        return outputIndex;
    }

    public String getScriptSignature() {
        return scriptSignature;
    }

    @Override
    public String toString() {
        return String.format(
                "TxInput(transaction=%s, output=%s, signature=%s)",
                HexHelper.printHexBinary(transactionId),
                outputIndex,
                scriptSignature
        );
    }

    public boolean canBeUnlockedWith(String unlockingData) {
        return scriptSignature.equals(unlockingData);
    }
}
