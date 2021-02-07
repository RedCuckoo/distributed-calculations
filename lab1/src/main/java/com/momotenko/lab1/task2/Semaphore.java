package com.momotenko.lab1.task2;

public class Semaphore {
    private int semaphore = 0;

    public boolean isTaken() {
        return (semaphore == 1);
    }

    public boolean setTaken() {
        if (isTaken()){
            return false;
        }else{
            semaphore = 1;
        }

        return true;
    }

    public void setFree(){
        semaphore = 0;
    }
}
