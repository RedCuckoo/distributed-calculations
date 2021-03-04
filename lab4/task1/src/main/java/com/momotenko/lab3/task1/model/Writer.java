package com.momotenko.lab3.task1.model;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.momotenko.lab3.task1.entity.Record;

public class Writer extends ReaderWriterAbstract{
    public Writer(ReentrantReadWriteLock lock, FileChannel fileChannel, int bufferSize) {
        super(lock, fileChannel, bufferSize);
    }

    public Thread getThreadToAddRecord(Record record){
        return new Thread(()->{
            lock.writeLock().lock();
            printLocked("Writer", true);
            System.out.println("Adding something");

            ByteBuffer toWrite = ByteBuffer.allocate(bufferSize);
            toWrite.put(ByteBuffer.wrap((record.toString()).getBytes(StandardCharsets.UTF_8)));
            toWrite.position(0);
            try {
                FileLock fileLock = fileChannel.tryLock();
                fileChannel.position(fileChannel.size());
                fileChannel.write(toWrite);
                fileLock.release();
            } catch (IOException e) {
                e.printStackTrace();
            }

            printLocked("Writer", false);
            lock.writeLock().unlock();
        });
    }

    public Thread getThreadToDeleteRecord(Record toDelete){
        return new Thread(()->{
            lock.writeLock().lock();
            printLocked("Writer", true);

            System.out.println("Deleting something");

            ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);

            try {
                FileLock fileLock = fileChannel.tryLock();
                fileChannel.position(0);

                Charset charset = StandardCharsets.UTF_8;

                while(fileChannel.read(byteBuffer) > 0){
                    byteBuffer.rewind();

                    if (toDelete.equals(new Record(charset.decode(byteBuffer).toString()))){
                        fileChannel.transferFrom(fileChannel,fileChannel.position()-bufferSize,fileChannel.size()-fileChannel.position());
                        fileChannel.truncate(fileChannel.size()-bufferSize);

                        System.out.println("Deleted successfully");
                        printLocked("Writer", false);
                        fileLock.release();
                        lock.writeLock().unlock();
                        return;
                    }

                    byteBuffer.flip();
                }
                fileLock.release();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Didn't find what to delete");
            printLocked("Writer", false);
            lock.writeLock().unlock();
        });
    }
}
