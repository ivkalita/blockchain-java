package ru.kaduev.blockchain.app;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.kaduev.blockchain.impl.Block;
import ru.kaduev.blockchain.impl.BlockChain;
import ru.kaduev.blockchain.impl.ProofOfWork;

@ShellComponent
public class Commands {

    @ShellMethod("Prints block chain")
    public void print() {
        BlockChain blockChain = BlockChain.openBlockChain();
        for (final Block block : blockChain) {
            System.out.println(block);
            ProofOfWork pow = new ProofOfWork(block);
            System.out.println(String.format("Is valid: %s\n", pow.isBlockValid()));
        }
    }

    @ShellMethod("Creates new blockchain")
    public void create(String minerAddress) {
        BlockChain.createBlockChain(minerAddress);
    }

    @ShellMethod("Drops existing blockchain")
    public void drop() {
        BlockChain.dropBlockChain();
    }
}
