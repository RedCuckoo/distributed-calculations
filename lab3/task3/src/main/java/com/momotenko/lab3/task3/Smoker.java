package com.momotenko.lab3.task3;

import java.util.HashSet;
import java.util.Map;

public class Smoker extends Thread {
    final SmokingAccessories smokingAccessory;
    final Mediator mutex;

    public Smoker(SmokingAccessories smokingAccessory, Mediator mediator) {
        this.smokingAccessory = smokingAccessory;
        this.mutex = mediator;
    }


    @Override
    public void run() {
        boolean smoke = false;

        while (true) {
            synchronized (mutex) {
                if (mutex.canChoose && !mutex.waitForSmokingAccessories) {
                    if (mutex.currentSmokingAccessory.getKey() != smokingAccessory
                            && mutex.currentSmokingAccessory.getValue() != smokingAccessory) {
                        System.out.println("Smoker " + smokingAccessory + " took items from the table");

                        mutex.canChoose = false;
                        smoke = true;
                    }

                    //System.out.println("Smoker " + smokingAccessory + " checked in");

                    mutex.checkedTable.add(smokingAccessory);
                }
            }

            Thread.yield();

            if (smoke) {
                try {
                    System.out.println("Smoker " + smokingAccessory + " started smoking");

                    Thread.sleep(Mediator.SMOKE_TIME);

                    System.out.println("Smoker " + smokingAccessory + " finished smoking");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                smoke = false;

                synchronized (mutex) {
                    mutex.canChoose = true;
                    mutex.waitForSmokingAccessories = true;
                }

                Thread.yield();
            }
        }
    }
}