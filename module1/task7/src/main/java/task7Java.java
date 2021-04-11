import java.util.*;

public class task7Java {
    final static int TRAIN_AMOUNT = 10;
    final static int MAX_WAIT_TIME = 100;
    final static int TUNNEL_TRAVEL_TIME = 20;

    static boolean tunnel1 = false, tunnel2 = false;
    static boolean tunnel1Finished = false, tunnel2Finished = false;

    static class ThreadTrain extends Thread {
        volatile int waitTime = 0;
        boolean side;

        ThreadTrain(boolean side){
            this.side = side;
        }

        private boolean canChangeTunnel() {
            return waitTime >= MAX_WAIT_TIME;
        }

        public boolean changeTunnel(){
            if (canChangeTunnel()){
                side = !side;
            }
        }

        public void increaseWaitTime() {
            synchronized (this) {
                ++waitTime;
            }
        }

        @Override
        public void run() {
            int startWaitTime = waitTime;

            while (waitTime - startWaitTime < TUNNEL_TRAVEL_TIME){}

            synchronized (task7Java.class) {
                tunnel1 = false;
                tunnel1Finished = true;
            }
        }
    }

    public static void main(String[] args) {

        List<Integer> arriveTime = new ArrayList<>();

        for (int i = 0; i < TRAIN_AMOUNT; ++i) {
            arriveTime.add(new Random().nextInt());
        }

        int currentTime = 0;

        List<List<ThreadTrain>> queues1 = new LinkedList<>();
        List<List<ThreadTrain>> queues2 = new LinkedList<>();

        queues1.add(new LinkedList<>());
        queues1.add(new LinkedList<>());
        queues2.add(new LinkedList<>());
        queues2.add(new LinkedList<>());

        int tunnelSide1 = 0;
        int tunnelSide2 = 0;

        while (true) {
            if (arriveTime.contains(currentTime)) {
                boolean side = new Random().nextBoolean();
                List<List<ThreadTrain>> queueToAdd = (side) ? queues1 : queues2;

                queueToAdd.sort((o1, o2) -> {
                    if (o1.size() < o2.size()) {
                        return -1;
                    } else if (o1.size() == o2.size()) {
                        return 0;
                    } else {
                        return 1;
                    }
                });

                queueToAdd.get(0).add(new ThreadTrain(side));
            }

            for (List<ThreadTrain> q : queues1) {
                for (ThreadTrain t : q) {
                    t.increaseWaitTime();
                }
            }

            if (tunnel1Finished){
                tunnel1Finished = false;
                queues1.get(tunnelSide1).remove(queues1.get(tunnelSide1).size() - 1);
            }

            if (tunnel2Finished){
                tunnel2Finished = false;
                queues2.get(tunnelSide2).remove(queues2.get(tunnelSide2).size() - 1);
            }

            if (!tunnel1){
                boolean side = new Random().nextBoolean();
                int sideInt = (side) ? 0 : 1;

                tunnelSide1 = sideInt;
                tunnel1 = true;

                queues1.get(sideInt).get(queues1.get(sideInt).size() - 1).start();
            }

            if (!tunnel2){
                boolean side = new Random().nextBoolean();
                int sideInt = (side) ? 0 : 1;

                tunnelSide2 = sideInt;
                tunnel2 = true;

                queues2.get(sideInt).get(queues2.get(sideInt).size() - 1).start();
            }

            currentTime++;
        }


    }
}
