package com.momotenko.lab3.task1.model;

import java.nio.channels.FileChannel;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class ReaderWriterAbstract {
    protected ReentrantReadWriteLock lock;
    protected FileChannel fileChannel;
    protected int bufferSize;

    protected void printLocked(String name, boolean b){
        System.out.println(name +" "+ ((b) ? "" : "un") + "locked the lock");
    }

    public ReaderWriterAbstract(ReentrantReadWriteLock lock, FileChannel fileChannel, int bufferSize) {
        this.lock = lock;
        this.fileChannel = fileChannel;
        this.bufferSize = bufferSize;
    }
}
