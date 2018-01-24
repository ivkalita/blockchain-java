package ru.kaduev.blockchain.app;

import ru.kaduev.blockchain.impl.Block;

import java.security.NoSuchAlgorithmException;

public class Application {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        byte[] prevBlockHash = new byte[32];
        prevBlockHash[0] = 125;
        Block block = new Block("Genesis block", prevBlockHash);
        System.out.println(block.toString());
        System.out.println("Hello world");
    }
}
