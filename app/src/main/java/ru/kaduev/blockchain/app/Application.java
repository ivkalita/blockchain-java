package ru.kaduev.blockchain.app;

import ru.kaduev.blockchain.impl.Block;
import ru.kaduev.blockchain.impl.BlockChain;

public class Application {
    public static void main(String[] args) {
        BlockChain blockChain = new BlockChain();
        blockChain.add("I sent 1 BTC to Ivan");
        for (final Block block: blockChain.getBlocks()) {
            System.out.println(block);
        }
    }
}
