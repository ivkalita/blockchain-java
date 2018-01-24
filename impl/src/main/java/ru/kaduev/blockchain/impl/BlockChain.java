package ru.kaduev.blockchain.impl;

import java.util.ArrayList;

public class BlockChain {
    private ArrayList<Block> blocks;

    public BlockChain() {
        this.blocks = new ArrayList<>();
        this.blocks.add(new Block());
    }

    public void add(String data) {
        Block prevBlock = this.blocks.get(this.blocks.size() - 1);
        Block newBlock = new Block(data, prevBlock.getHash());
        this.blocks.add(newBlock);
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }
}
