package com.example.concurrency;

public class Joining {
    public static void main(String[] args) {
        Sleeper sleeper1 = new Sleeper("Sleeper1", 1500);
        Sleeper sleeper2 = new Sleeper("Sleeper2", 1500);

        Joiner joiner1 = new Joiner("Joiner1", sleeper1);
        Joiner joiner2 = new Joiner("Joiner2", sleeper2);

        sleeper1.interrupt();

    }
}

class Sleeper extends Thread {
    private int duration;

    public Sleeper(String name, int sleepTime) {
        super(name);
        duration = sleepTime;
        start();
    }

    @Override
    public void run() {
        try {
            sleep(duration);
        } catch (InterruptedException e) {
            System.out.println(getName() + " was interrupted." +
                    "isInterrupted:" + isInterrupted());
            return;
        }
        System.out.println(getName() + " has awakened");
    }
}

class Joiner extends Thread {
    private Sleeper mSleeper;

    public Joiner(String name, Sleeper sleeper) {
        super(name);
        this.mSleeper = sleeper;
        start();
    }

    @Override
    public void run() {
        super.run();
        try {
            mSleeper.join();
        } catch (InterruptedException e) {
            System.out.println("interrupted");
        }
        System.out.println(getName() + " join completed");
    }
}