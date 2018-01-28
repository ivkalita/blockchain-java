package ru.kaduev.blockchain.impl;

import java.io.Serializable;

public class TransactionOutput implements Serializable {
    private long value;
    private String scriptPublicKey;

    TransactionOutput(long value, String scriptPublicKey) {
        this.value = value;
        this.scriptPublicKey = scriptPublicKey;
    }

    public long getValue() {
        return value;
    }

    public String getScriptPublicKey() {
        return scriptPublicKey;
    }

    @Override
    public String toString() {
        return String.format("TxOutput(value=%s, scriptPublicKey=%s)", value, scriptPublicKey);
    }

    public boolean canBeUnlockedWith(String unlockingData) {
        return scriptPublicKey.equals(unlockingData);
    }
}
