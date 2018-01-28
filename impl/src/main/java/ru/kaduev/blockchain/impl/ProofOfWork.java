package ru.kaduev.blockchain.impl;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ProofOfWork {

    static class Proof {
        long nonce;
        byte[] hash;

        Proof(long nonce, byte[] hash) {
            this.nonce = nonce;
            this.hash = hash;
        }
    }

    private static final Integer TARGET_BITS = 5;

    private Block block;

    public ProofOfWork(Block block) {
        this.block = block;
    }

    private ByteBuffer prepareData(long nonce) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE + block.getPrevBlockHash().length + HexHelper.HASH_SIZE);
        buffer.put(block.getPrevBlockHash())
                .putLong(block.getTimestamp().getTime())
                .put(block.hashTransactions())
                .putLong(TARGET_BITS)
                .putLong(nonce);

        return buffer;
    }

    public Proof run() {
        byte[] hash = null;
        System.out.println("Mining the block");
        long nonce = 0;
        while (nonce < Long.MAX_VALUE) {
            hash = hashWithNonce(nonce);
            if (isHashValid(hash)) {
                System.out.println(HexHelper.printHexBinary(hash));
                System.out.println("zeroes = " + numberOfLeadingZeroes(hash));
                break;
            }
            nonce++;
        }
        System.out.println();

        return new Proof(nonce, hash);
    }

    private byte[] hashWithNonce(long nonce) {
        return HexHelper.hash(prepareData(nonce));
    }

    private boolean isHashValid(byte[] hash) {
        return numberOfLeadingZeroes(hash) >= TARGET_BITS;
    }

    private int numberOfLeadingZeroes(byte[] array) {
        int result = 0;
        for (int item : array) {
            item = item + Math.abs(Byte.MIN_VALUE);
            if (item == 0) {
                result += 8;
                continue;
            } else if (item == 1) {
                result += 7;
            } else if (item < 4) {
                result += 6;
            } else if (item < 8) {
                result += 5;
            } else if (item < 16) {
                result += 4;
            } else if (item < 32) {
                result += 3;
            } else if (item < 64) {
                result += 2;
            } else if (item < 128) {
                result += 1;
            }
            break;
        }

        return result;
    }

    public boolean isBlockValid() {
        return isHashValid(hashWithNonce(block.getNonce()));
    }
}
