package task2;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GraphicalInterface {
    JFrame frame;

    JButton buttonStart1;
    JButton buttonStart2;
    JButton buttonStop1;
    JButton buttonStop2;

    JSlider slider;

    Thread thread1;
    Thread thread2;

    JLabel label = new JLabel("Blocked by other thread");

    boolean started = false;

    Semaphore semaphore = new Semaphore();

    public GraphicalInterface() {
        frame = new JFrame("Lab 1");

        buttonStart1 = new JButton("Start1");
        buttonStart2 = new JButton("Start2");
        buttonStop1 = new JButton("Stop1");
        buttonStop2 = new JButton("Stop2");

        buttonStart1.addActionListener(e -> {
            onButtonStart1Click();
        });
        buttonStart2.addActionListener(e -> {
            onButtonStart2Click();
        });
        buttonStop1.addActionListener(e -> {
            onButtonStop1Click();
        });
        buttonStop2.addActionListener(e -> {
            onButtonStop2Click();
        });

        slider = new JSlider();

        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        label.setHorizontalAlignment(JLabel.CENTER);

        JPanel panel = new JPanel();
        panel.add(buttonStart1);
        panel.add(buttonStart2);
        panel.add(buttonStop1);
        panel.add(buttonStop2);
        panel.add(label);
        panel.setLayout(new GridLayout(3, 2));

        label.setVisible(false);

        frame.add(slider, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.setSize(400, 400);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void onButtonStart1Click() {
        if (semaphore.setTaken()){
            thread1 = new Thread(()->{
                while(!Thread.currentThread().isInterrupted()){
                    synchronized (slider){
                        slider.setValue(10);

                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        Thread.yield();
                    }
                }
            });
            thread1.setPriority(1);
            thread1.setDaemon(true);
            thread1.start();

            buttonStop2.setEnabled(false);
            label.setVisible(false);
        }
        else{
            label.setVisible(true);
        }
    }

    private void onButtonStart2Click() {
        if (semaphore.setTaken()){
            thread2 = new Thread(()->{
                while(!Thread.currentThread().isInterrupted()){
                    synchronized (slider){
                        slider.setValue(90);

                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        Thread.yield();
                    }
                }
            });
            thread2.setPriority(10);
            thread1.setDaemon(false);
            thread2.start();

            buttonStop1.setEnabled(false);
            label.setVisible(false);
        }
        else{
            label.setVisible(true);
        }
    }

    private void onButtonStop1Click() {
        thread1.interrupt();
        buttonStop2.setEnabled(true);
        semaphore.setFree();
        label.setVisible(false);
    }
    private void onButtonStop2Click() {
        thread2.interrupt();
        buttonStop1.setEnabled(true);
        semaphore.setFree();
        label.setVisible(false);
    }
}
