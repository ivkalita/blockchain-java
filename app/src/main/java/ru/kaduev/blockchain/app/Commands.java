package ru.kaduev.blockchain.app;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.kaduev.blockchain.impl.Block;
import ru.kaduev.blockchain.impl.BlockChain;
import ru.kaduev.blockchain.impl.ProofOfWork;

@ShellComponent
public class Commands {

    @ShellMethod("Prints block chain")
    public void printChain() {
        BlockChain blockChain = new BlockChain();
        for (final Block block : blockChain) {
            System.out.println(block);
            ProofOfWork pow = new ProofOfWork(block);
            System.out.println(String.format("Is valid: %s\n", pow.isBlockValid()));
        }
        blockChain.close();
    }

    @ShellMethod("Adds new block with data")
    public void addBlock(String data) {
        BlockChain blockChain = new BlockChain();
        blockChain.add(data);
        blockChain.close();
    }
}
