package com.momotenko.lab2.task3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1){
            System.out.println("Please, enter monks amount as a program argument");
            return;
        }

        List<Integer> monksPower = new ArrayList<>();

        int monksAmount = Integer.parseInt(args[0]);
        int maxPower = 0;

        for (int i = 0; i < monksAmount; ++i){
            monksPower.add(new Random().nextInt(100));
            if (monksPower.get(i) > maxPower){
                maxPower = monksPower.get(i);
            }
        }

        Competition competition = new Competition(monksPower,0,monksAmount -1);
        competition.fork();
        Integer winner = monksPower.get((Integer) competition.join());

        if (winner == maxPower){
            System.out.println("The winner is the monk with the highest power");
        }
        else{
            System.out.println("Predicted winner was "+maxPower+" but winner is "+ winner);
        }
    }
}
