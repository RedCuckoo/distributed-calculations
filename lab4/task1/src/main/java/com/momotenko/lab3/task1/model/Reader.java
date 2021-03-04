package com.momotenko.lab3.task1.model;

import com.momotenko.lab3.task1.entity.Record;
import com.momotenko.lab3.task1.entity.RecordEnum;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Reader extends ReaderWriterAbstract{
    public Reader(ReentrantReadWriteLock lock, FileChannel fileChannel, int bufferSize) {
        super(lock, fileChannel, bufferSize);
    }

    public boolean compareRecordAndString(RecordEnum recordEnum, Record record, String toFind){
        switch (recordEnum){
            case FAMILY_NAME:{
                return record.getFamilyName().replaceAll("\\P{Print}","").equals(toFind);
            }
            case PHONE_NUMBER:{
                return record.getPhoneNumber().replaceAll("\\P{Print}","").equals(toFind);
            }
            case FIRST_NAME:{
                return record.getFirstName().replaceAll("\\P{Print}","").equals(toFind);
            }
            case LAST_NAME:{
                return record.getLastName().replaceAll("\\P{Print}","").equals(toFind);
            }
            default:{
                return false;
            }
        }
    }

    private Thread createThread(RecordEnum recordEnum, String toFind) {
        return new Thread(() -> {
            lock.readLock().lock();

            printLocked("Reader", true);
            System.out.println("Reading something");

            List<Record> namesAndNumbersList = new LinkedList<>();

            ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
            Charset charset = StandardCharsets.UTF_8;

            try {
                fileChannel.position(0);

                while (fileChannel.read(byteBuffer) > 0) {
                    byteBuffer.rewind();
                    namesAndNumbersList.add(new Record(charset.decode(byteBuffer).toString()));
                    byteBuffer.flip();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }

            for (Record r : namesAndNumbersList) {
                if (compareRecordAndString(recordEnum, r, toFind)){
                    lock.readLock().unlock();
                    System.out.println("Found successfully");
                    printLocked("Reader", false);
                    return;
                }
            }

            System.out.println("Could not find such prompt");

            printLocked("Reader", false);
            lock.readLock().unlock();

        });
    }

    public Thread getThreadToFindByFamilyName(String familyName){
        return createThread(RecordEnum.FAMILY_NAME, familyName);
    }

    public Thread getThreadToFindByPhone(String phoneNumber){
        return createThread(RecordEnum.PHONE_NUMBER, phoneNumber);
    }
}
