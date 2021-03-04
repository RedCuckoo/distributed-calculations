package com.momotenko.lab3.task1.controller;

import com.momotenko.lab3.task1.entity.Record;
import com.momotenko.lab3.task1.model.Reader;
import com.momotenko.lab3.task1.model.Writer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Controller {
    private static final int BUFFER_SIZE = 64;

    public static void run() throws IOException {
        Path path = Paths.get("C:/Test/temp.txt");
        RandomAccessFile file = new RandomAccessFile(path.toFile(),"rw");
        FileChannel fileChannel = file.getChannel();
        fileChannel.truncate(0);

        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        Record record1 = new Record(
                        "Momotenko",
                        "Yuriy",
                        "Batkovych",
                        "+8798465466569");
        Record record2 = new Record(
                        "Pupkin",
                        "Vasya",
                        "Hryhorovych",
                        "+546548987546");
        Record record3 = new Record(
                        "Ivanov",
                        "Ivan",
                        "Ivanovych",
                        "+5465487981545");

        Writer writer = new Writer(lock,fileChannel,BUFFER_SIZE);
        Reader reader = new Reader(lock, fileChannel, BUFFER_SIZE);

        List<Thread> threadList = new LinkedList<>(Arrays.asList(
                writer.getThreadToAddRecord(record1),
                writer.getThreadToAddRecord(record2),
                writer.getThreadToAddRecord(record3),
                reader.getThreadToFindByPhone("+5465487981545"),
                reader.getThreadToFindByFamilyName("Pupkin"),
                writer.getThreadToDeleteRecord(record2)
        ));

        for (Thread t : threadList){
            t.start();
        }

        try {
            for (Thread t : threadList) {
                t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
