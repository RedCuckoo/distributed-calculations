package com.momotenko.lab2.task3;

import java.util.List;
import java.util.concurrent.RecursiveTask;

public class Competition extends RecursiveTask {
    private List<Integer> monksPower;
    private Integer startIndex, endIndex;

    public Competition(List<Integer> monksPower, Integer startIndex, Integer endIndex) {
        this.monksPower = monksPower;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    private Integer compete(Integer competitorIndex, Integer opponentIndex){
        return (monksPower.get(competitorIndex) < monksPower.get(opponentIndex)) ? opponentIndex : competitorIndex;
    }

    @Override
    protected Integer compute() {
        if (endIndex - startIndex == 1){
            return compete(startIndex,endIndex);
        }else if (endIndex.equals(startIndex)){
            return startIndex;
        }

        int middleIndex = (startIndex + endIndex) / 2;

        Competition leftCompetition = new Competition(monksPower, startIndex,middleIndex);
        Competition rightCompetition = new Competition(monksPower, middleIndex+1,endIndex);
        leftCompetition.fork();
        rightCompetition.fork();

        Integer leftWinner = (Integer) leftCompetition.join();
        Integer rightWinner = (Integer) rightCompetition.join();


        return compete(leftWinner,rightWinner);
    }
}
