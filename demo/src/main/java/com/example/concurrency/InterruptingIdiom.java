package com.example.concurrency;

import java.util.concurrent.TimeUnit;

public class InterruptingIdiom {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Blocked3());
        thread.start();
        TimeUnit.MILLISECONDS.sleep(2000);
        thread.interrupt();
    }
}

class NeedsCleanUp {
    private final int id;

    public NeedsCleanUp(int ident) {
        id = ident;
        System.out.println("NeedCleanUp " + id);
    }

    public void cleanup() {
        System.out.println("Clean up " + id);
    }
}

class Blocked3 implements Runnable {
    private volatile double d = 0.0;

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                //point1
                NeedsCleanUp n1 = new NeedsCleanUp(1);
                try {
                    TimeUnit.SECONDS.sleep(1);

                    //point2
                    NeedsCleanUp n2 = new NeedsCleanUp(2);

                    try {
                        System.out.println("Calculating");
                        for (int i = 1; i < 250000000; i++) {
                            d = d + (Math.PI + Math.E) / i;
                        }
                        System.out.println("Finish time-consuming operation");
                    } finally {
                        n2.cleanup();
                    }
                } finally {
                    n1.cleanup();
                }
            }
            System.out.println("Exiting via while() test");
        } catch (InterruptedException e) {
            System.out.println("Exiting via InterruptedException: " + Thread.interrupted());
        }
    }
}
