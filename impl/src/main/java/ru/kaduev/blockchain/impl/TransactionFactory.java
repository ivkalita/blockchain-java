package ru.kaduev.blockchain.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransactionFactory {
    private static final long SUBSIDY = 50;

    public static Transaction createCoinbase(String address) {
        List<TransactionInput> inputs = new ArrayList<>();
        inputs.add(new TransactionInput(new byte[HexHelper.HASH_SIZE], -1, address));

        List<TransactionOutput> outputs = new ArrayList<>();
        outputs.add(new TransactionOutput(SUBSIDY, address));

        return new Transaction(inputs, outputs);
    }

    public static Transaction create(String from, String to, long amount, BlockChain blockChain) {
        UnspentTransactionsFinder.Result result = new UnspentTransactionsFinder(blockChain, from).find();
        Map<Transaction, List<Integer>> spendable = result.getSpendableOutputs(amount);

        List<TransactionInput> inputs = new ArrayList<>();
        long accumulated = 0;
        for (final Map.Entry<Transaction, List<Integer>> pair : spendable.entrySet()) {
            final Transaction tx = pair.getKey();
            for (final Integer outIdx : pair.getValue()) {
                inputs.add(new TransactionInput(pair.getKey().getId(), outIdx, from));
                accumulated += tx.getOutputs().get(outIdx).getValue();
            }
        }

        List<TransactionOutput> outputs = new ArrayList<>();
        outputs.add(new TransactionOutput(amount, to));
        if (accumulated > amount) {
            outputs.add(new TransactionOutput(accumulated - amount, from));
        }

        return new Transaction(inputs, outputs);
    }
}
