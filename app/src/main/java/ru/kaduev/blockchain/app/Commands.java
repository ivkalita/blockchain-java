package ru.kaduev.blockchain.app;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.kaduev.blockchain.impl.*;

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
    public void create(String address) {
        BlockChain.createBlockChain(address);
    }

    @ShellMethod("Drops existing blockchain")
    public void drop() {
        BlockChain.dropBlockChain();
    }

    @ShellMethod("Returns address balance")
    public void balance(String address) {
        BlockChain blockChain = BlockChain.openBlockChain();
        long balance = 0;
        for (final TransactionOutput out : new UnspentTransactionsFinder(blockChain, address).findUTXOs()) {
            balance += out.getValue();
        }
        System.out.println(String.format("Balance of %s is %s", address, balance));
    }
}
