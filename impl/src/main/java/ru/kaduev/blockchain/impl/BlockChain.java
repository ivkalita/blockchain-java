package ru.kaduev.blockchain.impl;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

import static org.fusesource.leveldbjni.JniDBFactory.factory;

public class BlockChain implements Iterable<Block> {
    private final static byte[] TIP = "tip".getBytes();
    private byte[] tip;
    private DB database;

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
            byte[] serialized = database.get(hash);
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

    public BlockChain() {
        database = getDatabase();
        byte[] tip = this.database.get(TIP);
        if (tip == null) {
            Block genesis = new Block();
            database.put(genesis.getHash(), genesis.getBytes());
            database.put(TIP, genesis.getHash());
            this.tip = genesis.getHash();
        } else {
            this.tip = tip;
        }
    }

    private DB getDatabase() {
        Options options = new Options();
        options.createIfMissing(true);
        try {
            return factory.open(new File("blockchain_database"), options);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void close() {
        try {
            database.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void add(String data) {
        byte[] lastHash = database.get(TIP);
        Block block = new Block(data, lastHash);
        database.put(block.getHash(), block.getBytes());
        database.put(TIP, block.getHash());
        tip = block.getHash();
    }

    @Override
    public java.util.Iterator<Block> iterator() {
        return new Iterator();
    }
}
