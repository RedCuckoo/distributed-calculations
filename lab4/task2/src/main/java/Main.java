import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    List<List<Boolean>> garden;
    int MAX_GARDEN_SIZE = 10;
    Random random;
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    {
        random = new Random();
        random.setSeed(System.nanoTime());
    }

    public Main() {
        garden = new ArrayList<>();
        int gardenSize = random.nextInt(MAX_GARDEN_SIZE - 1) + 1;
        for (int i = 0; i < gardenSize; ++i) {
            garden.add(new ArrayList<>());
            for (int j = 0; j < gardenSize; ++j) {
                garden.get(i).add(random.nextBoolean());
            }
        }
    }

    public void printGarden() {
        for (int i = 0, gardenSize = garden.size(); i < gardenSize; ++i) {
            for (int j = 0; j < gardenSize; ++j) {
                System.out.print(((garden.get(i).get(j)) ? "1" : "0") + " ");
            }
            System.out.println();
        }
    }

    public Thread getThreadGardener() {
        return new Thread(() -> {
            while (true) {
                lock.readLock().lock();
                for (int i = 0, gardenSize = garden.size(); i < gardenSize; ++i) {
                    for (int j = 0; j < gardenSize; ++j) {
                        if (!garden.get(i).get(j)) {
                            lock.readLock().unlock();
                            lock.writeLock().lock();

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            System.out.println("Watered plant");
                            garden.get(i).set(j, true);
                            lock.writeLock().unlock();
                            lock.readLock().lock();
                        }

                    }
                }
                lock.readLock().unlock();
            }
        });
    }

    public Thread getThreadNature() {
        return new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    lock.writeLock().lock();

                    int gardenSize = garden.size();
                    garden.get(random.nextInt(gardenSize)).set(random.nextInt(gardenSize), random.nextBoolean());
                    System.out.println("Nature changed");

                    lock.writeLock().unlock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Thread getThreadMonitorToFile() {
        return new Thread(() -> {
            try {
                FileWriter fileWriter = new FileWriter("out.txt", false);
                fileWriter.close();
                while (true) {
                    Thread.sleep(3000);
                    fileWriter = new FileWriter("out.txt", true);
                    System.out.println("Writing to file");
                    lock.readLock().lock();
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    for (int i = 0, gardenSize = garden.size(); i < gardenSize; ++i) {
                        for (int j = 0; j < gardenSize; ++j) {
                            bufferedWriter.write(((garden.get(i).get(j)) ? "1" : "0") + " ");
                        }
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.newLine();
                    bufferedWriter.close();
                    lock.readLock().unlock();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public Thread getThreadMonitorToConsole() {
        return new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(4000);
                    lock.readLock().lock();
                    printGarden();
                    lock.readLock().unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.printGarden();
        Thread gardener = main.getThreadGardener();
        Thread nature = main.getThreadNature();
        Thread fileMonitor = main.getThreadMonitorToFile();
        Thread consoleMonitor = main.getThreadMonitorToConsole();
        gardener.start();
        nature.start();
        fileMonitor.start();
        consoleMonitor.start();
    }
}
