package concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SerialNumberChecker {
    private static int SIZE = 10;
    private static CircularSet circularSet = new CircularSet(1000);
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < SIZE; i++) {
            executorService.execute(new SerialChecker());
        }
        if (args.length > 0) {
            TimeUnit.SECONDS.sleep(new Integer(args[0]));
            System.out.println("No duplicates detched");
            System.exit(0);
        }
    }

    static class SerialChecker implements Runnable {

        @Override
        public void run() {
            while (true) {
                int serialNumber = SerialNumberGenerator.nextSerialNumber();
                if (circularSet.contains(serialNumber)) {
                    System.out.println("Duplicate:" + serialNumber);
                    System.exit(0);
                }
                circularSet.add(serialNumber);
            }
        }
    }
}

class SerialNumberGenerator {
    private static volatile int serialNumber = 0;

    public synchronized static int nextSerialNumber() {
        return serialNumber++;
    }
}

class CircularSet {
    private int[] array;
    private int length;
    private int index;

    public CircularSet(int size) {
        array = new int[size];
        length = size;
        for (int i = 0; i < size; i++) {
            array[i] = -1;
        }
    }

    public synchronized void add(int value) {
        array[index] = value;
        index = ++index % length;
    }

    public synchronized boolean contains(int value) {
        for (int i = 0; i < length; i++) {
            if (array[i] == value) {
                return true;
            }
        }
        return false;
    }
}

