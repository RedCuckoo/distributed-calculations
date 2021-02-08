package com.momotenko.lab1.task1;

import com.momotenko.lab1.CustomRunnable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GraphicalInterface {
    JFrame frame;
    JButton button;
    JSlider slider;

    Thread thread1;
    Thread thread2;

    JSpinner spinner1;
    JSpinner spinner2;

    JLabel label1;
    JLabel label2;

    boolean started = false;

    public GraphicalInterface() {
        frame = new JFrame("Lab 1 | A");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        button = new JButton("Start");

        button.addActionListener(e -> {
            onButtonClick();
        });

        slider = new JSlider();

        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        spinner1 = new JSpinner();
        spinner2 = new JSpinner();

        spinner1.setValue(1);
        spinner1.addChangeListener(e -> {
            int val = (Integer) spinner1.getValue();
            if (val < 1) {
                spinner1.setValue(1);
            } else if (val > 10) {
                spinner1.setValue(10);
            }

            thread1.setPriority((Integer) spinner1.getValue());
        });

        spinner2.setValue(1);
        spinner2.addChangeListener(e -> {
            int val = (Integer) spinner2.getValue();
            if (val < 1) {
                spinner2.setValue(1);
            } else if (val > 10) {
                spinner2.setValue(10);
            }

            thread2.setPriority((Integer) spinner2.getValue());
        });

        label1 = new JLabel();
        label2 = new JLabel();
        label1.setHorizontalAlignment(JLabel.RIGHT);
        label2.setHorizontalAlignment(JLabel.RIGHT);

        JPanel panel = new JPanel();
        panel.add(label1);
        panel.add(label2);
        panel.add(spinner1);
        panel.add(spinner2);
        panel.setLayout(new GridLayout(3, 4));

        frame.add(slider, BorderLayout.NORTH);
        frame.add(button, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);
        frame.setSize(400, 400);
        frame.setVisible(true);

        thread1 = new Thread(new CustomRunnable(slider,10,label1));
        thread2 = new Thread(new CustomRunnable(slider,90,label2));
        thread1.setDaemon(true);
        thread2.setDaemon(true);
        thread1.setPriority(1);
        thread2.setPriority(1);
    }

    private void onButtonClick() {
        if (!started) {
            thread1.start();
            thread2.start();
            started = true;
        }
    }
}
