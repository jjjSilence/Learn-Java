package com.example.concurrency;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DeadLockingDiningPhilosophers {
    public static void main(String[] args) throws InterruptedException {
        int ponder = 0;
        int size = 5;
        ExecutorService executorService = Executors.newCachedThreadPool();
        Chopstick[] chopsticks = new Chopstick[size];
        for (int i = 0; i < size; i++) {
            chopsticks[i] = new Chopstick();
        }
        for (int i = 0; i < size; i++) {
            if (i == size) {
                executorService.execute(new Philosopher(chopsticks[(i + 1) % size], chopsticks[i], i, ponder));
            } else {
                executorService.execute(new Philosopher(chopsticks[i], chopsticks[(i + 1) % size], i, ponder));
            }

        }
        TimeUnit.SECONDS.sleep(15);
        executorService.shutdownNow();
    }
}

class Chopstick {
    private boolean taken = false;

    public synchronized void taken(int id, String action) throws InterruptedException {
        while (taken) {
            wait();
        }
        taken = true;
        System.out.println("id=" + id + " grabbing " + action);
    }

    public synchronized void drop() {
        taken = false;
        notifyAll();
    }
}

class Philosopher implements Runnable {
    private Chopstick left;
    private Chopstick right;
    private final int id;
    private final int ponderFactor;
    private Random random = new Random(47);

    private void pause() throws InterruptedException {
        if (ponderFactor == 0) return;
        TimeUnit.MILLISECONDS.sleep(random.nextInt(ponderFactor * 250));
    }

    public Philosopher(Chopstick left, Chopstick right, int ident, int ponder) {
        this.left = left;
        this.right = right;
        this.id = ident;
        this.ponderFactor = ponder;
    }

    @Override
    public void run() {

        try {
            while (!Thread.interrupted()) {
                System.out.println(this + " " + "thinking");
                pause();
//                System.out.println(this + " grabbing right");
                right.taken(id, "right");
//                System.out.println(this + " grabbing left");
                left.taken(id, "left");
                System.out.println(this + " eating");
                pause();
                right.drop();
                left.drop();
            }
        } catch (InterruptedException e) {
            System.out.println(this + " exiting via interrupt");
        }
    }

    @Override
    public String toString() {
        return "concurrency.Philosopher{" +
                "id=" + id +
                '}';
    }
}