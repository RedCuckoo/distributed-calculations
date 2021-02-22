package com.momotenko.lab3.task3;

import java.util.*;

public class Mediator {
    static final int SMOKE_TIME = 2000;
    private final Random random;

    HashSet<SmokingAccessories> checkedTable = new HashSet<>();
    Boolean canChoose = true;
    Boolean waitForSmokingAccessories = false;
    Map.Entry<SmokingAccessories, SmokingAccessories> currentSmokingAccessory;

    {
        random = new Random();
        random.setSeed(System.nanoTime());
    }

    public Mediator() {
        setRandomAccessories();
    }

    private void setRandomAccessories(){
        List<SmokingAccessories> smokingAccessoriesList = new LinkedList<>(Arrays.asList(SmokingAccessories.values()));
        int smokingAccessoriesListSize = smokingAccessoriesList.size();
        SmokingAccessories first = SmokingAccessories.values()[random.nextInt(smokingAccessoriesListSize)];
        smokingAccessoriesList.remove(first);
        SmokingAccessories second = smokingAccessoriesList.get(random.nextInt(smokingAccessoriesListSize-1));

        currentSmokingAccessory = Map.entry(first, second);

        System.out.println("Mediator placed new items on the table: " +
                currentSmokingAccessory.getValue() + " and " +
                currentSmokingAccessory.getKey());
    }

    public void run(){
        Thread smokerTobacco = new Smoker(SmokingAccessories.TOBACCO,this);
        Thread smokerPaper = new Smoker(SmokingAccessories.PAPER,this);
        Thread smokerMatches = new Smoker(SmokingAccessories.MATCHES,this);

        smokerTobacco.start();
        smokerPaper.start();
        smokerMatches.start();

        while (true) {
            synchronized (this) {
                if (checkedTable.size() == 3 && canChoose || waitForSmokingAccessories){
                    checkedTable = new HashSet<>();
                    setRandomAccessories();
                    waitForSmokingAccessories = false;
                }else{
//                    System.out.println("Mediator waits");
                }
            }

            Thread.yield();
        }
    }
}
