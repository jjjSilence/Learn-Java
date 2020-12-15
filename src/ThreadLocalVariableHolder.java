import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 21.3.7线程本地存储
 */
public class ThreadLocalVariableHolder {
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        DataGenerator dataGenerator = new DataGenerator();
        for (int i = 0; i < 5; i++) {
            executorService.execute(new Accessor(i, dataGenerator));
        }
        TimeUnit.MILLISECONDS.sleep(3);
        executorService.shutdownNow();
    }
}

class DataGenerator {
    private ThreadLocal<Integer> value = new ThreadLocal<Integer>() {
        private Random random = new Random(47);

        protected synchronized Integer initialValue() {
            return random.nextInt(100);
        }
    };

    public void increment() {
        value.set(value.get() + 1);
    }

    public int get() {
        return value.get();
    }
}

class Accessor implements Runnable {
    private int id;
    private DataGenerator dataGenerator;

    public Accessor(int id, DataGenerator dataGenerator) {
        this.id = id;
        this.dataGenerator = dataGenerator;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            dataGenerator.increment();
            System.out.println(this);
            Thread.yield();
        }
    }

    @Override
    public String toString() {
        return "#" + id + ": " + dataGenerator.get();
    }
}
