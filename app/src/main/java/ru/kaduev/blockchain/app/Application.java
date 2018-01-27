package ru.kaduev.blockchain.app;

import ru.kaduev.blockchain.impl.Block;
import ru.kaduev.blockchain.impl.BlockChain;
import ru.kaduev.blockchain.impl.ProofOfWork;

public class Application {
    public static void main(String[] args) {
        BlockChain blockChain = new BlockChain();
//        blockChain.add("I sent 1 BTC to Ivan");
        for (final Block block: blockChain) {
            System.out.println(block);
            ProofOfWork pow = new ProofOfWork(block);
            System.out.println(String.format("Is valid: %s\n", pow.isBlockValid()));
        }
        blockChain.close();
    }
}
