package ru.kaduev.blockchain.impl;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static org.fusesource.leveldbjni.JniDBFactory.factory;

public class BlockChainStorage {
    private final static byte[] TIP = "tip".getBytes();
    private final static String DEFAULT_DATABASE = "blockchain_database";
    private String dbName;
    private DB database;
    private boolean opened = false;

    private BlockChainStorage(String dbName) {
        this.dbName = dbName;
        this.database = null;
    }

    BlockChainStorage() {
        this(DEFAULT_DATABASE);
    }

    boolean blockChainExists() {
        return getTip() != null;
    }

    byte[] getTip() {
        return get(TIP);
    }

    void put(byte[] key, byte[] value) {
        openDatabase();
        database.put(key, value);
        closeDatabase();
    }

    byte[] get(byte[] key) {
        openDatabase();
        byte[] value = database.get(key);
        closeDatabase();
        return value;
    }

    void setTip(byte[] tip) {
        put(TIP, tip);
    }

    void drop() {
        try {
            Path directory = Paths.get(dbName);
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openDatabase() {
        if (opened) {
            return;
        }
        Options options = new Options();
        options.createIfMissing(true);
        try {
            database = factory.open(new File(dbName), options);
            opened = true;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void closeDatabase() {
        if (!opened) {
            return;
        }
        try {
            database.close();
            opened = false;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
