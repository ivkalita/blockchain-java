package ru.kaduev.blockchain.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnspentTransactionsFinder {
    private BlockChain blockChain;
    private String address;
    private List<Transaction> unspentTXs;
    private Map<String, Map<Integer, Boolean>> spentTXOs;

    public UnspentTransactionsFinder(BlockChain blockChain, String address) {
        this.blockChain = blockChain;
        this.address = address;
        unspentTXs = new ArrayList<>();
        spentTXOs = new HashMap<>();
    }

    public List<Transaction> findTX() {
        for (final Block block : blockChain) {
            visitBlock(block);
        }
        return unspentTXs;
    }

    public List<TransactionOutput> findUTXOs() {
        List<Transaction> transactions = findTX();
        List<TransactionOutput> UTXOs = new ArrayList<>();
        for (final Transaction tx: transactions) {
            for (final TransactionOutput out : tx.getOutputs()) {
                if (!out.canBeUnlockedWith(address)) {
                    continue;
                }
                UTXOs.add(out);
            }
        }

        return UTXOs;
    }

    private void visitBlock(Block block) {
        for (final Transaction tx : block.getTransactions()) {
            visitTransaction(tx);
        }
    }

    private void visitTransaction(Transaction tx) {
        TransactionOutput[] outs = tx.getOutputs();
        for (int outIdx = 0; outIdx < outs.length; outIdx++) {
            if (outputWasSpent(tx, outIdx)) {
                continue;
            }
            final TransactionOutput out = outs[outIdx];
            if (!out.canBeUnlockedWith(address)) {
                continue;
            }
            unspentTXs.add(tx);
        }

        if (tx.isCoinbase()) {
            return;
        }

        for (final TransactionInput in : tx.getInputs()) {
            if (!in.canBeUnlockedWith(address)) {
                continue;
            }
            markOutputAsSpent(HexHelper.printHexBinary(in.getTransactionId()), in.getOutputIndex());
        }
    }

    private boolean outputWasSpent(Transaction tx, Integer outIdx) {
        final String txId = HexHelper.printHexBinary(tx.getId());
        return spentTXOs.get(txId) != null && spentTXOs.get(txId).get(outIdx) != null;
    }

    private void markOutputAsSpent(String txId, Integer outIdx) {
        if (!spentTXOs.containsKey(txId)) {
            spentTXOs.put(txId, new HashMap<>());
        }
        spentTXOs.get(txId).put(outIdx, true);
    }
}
