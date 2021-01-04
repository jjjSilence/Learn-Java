package concurrency.waxomatic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WaxOMatic {
    public static void main(String[] args) throws InterruptedException {
        Car car = new Car();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new WaxOff(car, 1));
        executorService.execute(new WaxOff(car, 2));
        executorService.execute(new WaxOff(car, 3));
        executorService.execute(new WaxOn(car, 1));
        executorService.execute(new WaxOn(car, 2));
        TimeUnit.SECONDS.sleep(150);
        executorService.shutdownNow();
    }
}

class Car {
    public volatile boolean waxOn = false;

    public synchronized void waxed() {
        waxOn = true;
        notifyAll();
    }

    public synchronized void buffed() {
        waxOn = false;
        notifyAll();
    }

    public synchronized void waitingForWax() throws InterruptedException {
        System.out.println("wait");
        while (waxOn == false) {
            System.out.println("wait==false");
            wait();
        }
    }

    public synchronized void waitingForBuffing() throws InterruptedException {
        while (waxOn == true) {
            wait();
        }
    }
}

class WaxOn implements Runnable {
    private Car car;
    private int id;

    public WaxOn(Car car, int id) {
        this.car = car;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (car) {
                    car.waitingForBuffing();
                    System.out.println("Wax on! " + id);

                    TimeUnit.MILLISECONDS.sleep(100);
                    car.waxed();
                    car.waitingForBuffing();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting via interrupt");
        }
        System.out.println("Ending Wax On task");
    }
}

class WaxOff implements Runnable {

    private Car car;
    private int id;

    public WaxOff(Car car, int ident) {
        this.car = car;
        this.id = ident;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (car) {
                    car.waitingForWax();
                    System.out.println("Wax Off! " + id + " " + car.waxOn);
                    TimeUnit.MILLISECONDS.sleep(1000);
                    car.buffed();
                    car.waitingForWax();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting via interrupt");
        }
        System.out.println("Ending Wax Off task");
    }
}
