package ru.kaduev.blockchain.impl;

import java.util.*;

public class BlockChain implements Iterable<Block> {
    private byte[] tip;
    private BlockChainStorage storage;

    class Iterator implements java.util.Iterator<Block> {
        Block next;

        Iterator() {
            next = blockByHash(tip);
        }

        @Override
        public boolean hasNext() {
            return next != null;

        }

        private Block blockByHash(byte[] hash) {
            byte[] serialized = storage.get(hash);
            if (serialized == null) {
                return null;
            }

            return Block.fromBytes(serialized);
        }

        @Override
        public Block next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Block current = next;
            next = blockByHash(current.getPrevBlockHash());
            return current;
        }
    }

    /**
     * Creates brand new blockchain and stores it into the storage.
     *
     * @param address coinbase miner address
     * @return Blockchain instance
     */
    public static BlockChain createBlockChain(String address) {
        BlockChainStorage storage = new BlockChainStorage();
        if (storage.blockChainExists()) {
            throw new RuntimeException("Blockchain already exists.");
        }

        return new BlockChain(storage, TransactionFactory.createCoinbase(address));
    }

    /**
     * Creates blockchain instance for data stored in the storage.
     *
     * @return Blockchain instance
     */
    public static BlockChain openBlockChain() {
        BlockChainStorage storage = new BlockChainStorage();
        if (!storage.blockChainExists()) {
            throw new RuntimeException("Blockchain does not exist.");
        }

        return new BlockChain(storage);
    }

    /**
     * Clears storage from any existing blockchains.
     */
    public static void dropBlockChain() {
        BlockChainStorage storage = new BlockChainStorage();
        storage.drop();
    }

    /**
     * Mines new block with transactions.
     *
     * @param transactions transactions to be mined
     */
    public void mineBlock(Transaction[] transactions) {
        mineBlock(transactions, storage.getTip());
    }

    @Override
    public java.util.Iterator<Block> iterator() {
        return new Iterator();
    }

    /**
     * Loads blockchain from existing storage.
     *
     * @param storage blockchain storage
     */
    private BlockChain(BlockChainStorage storage) {
        if (!storage.blockChainExists()) {
            throw new RuntimeException("Blockchain does not exist.");
        }
        this.storage = storage;
        this.tip = storage.getTip();
    }

    /**
     * Creates new blockchain, stores it in the storage.
     *
     * @param storage  blockchain storage
     * @param coinbase coinbase transaction
     */
    private BlockChain(BlockChainStorage storage, Transaction coinbase) {
        if (storage.blockChainExists()) {
            throw new RuntimeException("Blockchain already exists.");
        }
        this.storage = storage;
        this.tip = storage.getTip();

        mineBlock(new Transaction[]{coinbase}, new byte[HexHelper.HASH_SIZE]);
    }

    private void mineBlock(Transaction[] transactions, byte[] lastHash) {
        Block block = new Block(transactions, lastHash);
        storage.put(block.getHash(), block.getBytes());
        storage.setTip(block.getHash());
        tip = block.getHash();
    }
}
