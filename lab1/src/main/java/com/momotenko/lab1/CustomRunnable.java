package com.momotenko.lab1;

import javax.swing.*;

public class CustomRunnable implements Runnable {
    int counter = 0;

    JLabel label;
    JSlider slider;
    int sliderSetValue;


    public CustomRunnable(JSlider slider, int sliderSetValue, JLabel label) {
        this.label = label;
        this.slider = slider;
        this.sliderSetValue = sliderSetValue;
    }


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (slider) {
                slider.setValue(sliderSetValue);

                if (label != null) {
                    label.setText(sliderSetValue + " captured # of time: " + ++counter);
                }

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                Thread.yield();
            }
        }
    }
}
