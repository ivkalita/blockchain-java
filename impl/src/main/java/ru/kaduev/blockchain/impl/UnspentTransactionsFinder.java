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

    public class Result {
        public List<Transaction> getTransactions() {
            return unspentTXs;
        }

        public List<TransactionOutput> getOutputs() {
            List<TransactionOutput> UTXOs = new ArrayList<>();
            for (final Transaction tx : unspentTXs) {
                for (final TransactionOutput out : tx.getOutputs()) {
                    if (!out.canBeUnlockedWith(address)) {
                        continue;
                    }
                    UTXOs.add(out);
                }
            }

            return UTXOs;
        }

        public Map<Transaction, List<Integer>> getSpendableOutputs(long amount) {
            Map<Transaction, List<Integer>> result = new HashMap<>();
            long accumulated = 0;
            for (final Transaction tx : unspentTXs) {
                for (int outIdx = 0; outIdx < tx.getOutputs().size(); outIdx++) {
                    final TransactionOutput out = tx.getOutputs().get(outIdx);
                    if (!out.canBeUnlockedWith(address)) {
                        continue;
                    }
                    accumulated += out.getValue();
                    if (!result.containsKey(tx)) {
                        result.put(tx, new ArrayList<>());
                    }
                    result.get(tx).add(outIdx);
                    if (accumulated >= amount) {
                        return result;
                    }
                }
            }

            throw new RuntimeException(String.format("Not enough funds (%s < %s)", accumulated, amount));
        }

        public long getBalance() {
            return getOutputs().stream().mapToLong(TransactionOutput::getValue).sum();
        }
    }

    public UnspentTransactionsFinder(BlockChain blockChain, String address) {
        this.blockChain = blockChain;
        this.address = address;
        unspentTXs = new ArrayList<>();
        spentTXOs = new HashMap<>();
    }

    public Result find() {
        for (final Block block : blockChain) {
            visitBlock(block);
        }
        return new Result();
    }

    private void visitBlock(Block block) {
        for (final Transaction tx : block.getTransactions()) {
            visitTransaction(tx);
        }
    }

    private void visitTransaction(Transaction tx) {
        List<TransactionOutput> outs = tx.getOutputs();
        for (int outIdx = 0; outIdx < outs.size(); outIdx++) {
            if (outputWasSpent(tx, outIdx)) {
                continue;
            }
            final TransactionOutput out = outs.get(outIdx);
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
