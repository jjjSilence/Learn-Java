import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;

public class TestBlockingQueues {
    public static void main(String[] args) throws InterruptedException {
        test("LinkedBlockingDeque", new LinkedBlockingDeque<>());
        test("ArrayBlockingQueue", new ArrayBlockingQueue<>(3));
        test("SynchronousQueue", new SynchronousQueue<>());
    }

    static void getKey() {
        try {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void getKey(String message) {
        System.out.println(message);
        getKey();
    }

    static void test(String message, BlockingQueue<LiftOff> blockingQueue) throws InterruptedException {
        System.out.println(message);
        LiftOffRunnable liftOffRunnable = new LiftOffRunnable(blockingQueue);
        Thread thread = new Thread(liftOffRunnable);
        thread.start();
        for (int i = 0; i < 5; i++) {
            liftOffRunnable.add(new LiftOff(i));
        }
        TimeUnit.SECONDS.sleep(3);
        getKey("Press 'Enter' (" + message + ")");
        thread.interrupt();
        System.out.println("Finished " + message + " test");
    }
}

class LiftOff implements Runnable {

    private int id;
    public LiftOff(int id) {
        this.id = id;
        System.out.println("LiftOff " + id);
    }

    @Override
    public void run() {
        System.out.println("LiftOff run " + id);
    }
}

class LiftOffRunnable implements Runnable {

    private BlockingQueue<LiftOff> blockingQueue;

    public LiftOffRunnable(BlockingQueue<LiftOff> queue) {
        this.blockingQueue = queue;
    }

    public void add(LiftOff liftOff) {
        try {
            blockingQueue.put(liftOff);
        } catch (InterruptedException e) {
            System.out.println("Interrupt during put()");
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                LiftOff liftOff = blockingQueue.take();
                liftOff.run();
            }
        } catch (InterruptedException e) {
            System.out.println("Waking from take()");
        }
        System.out.println("Exiting LiftOffRunner");
    }
}
