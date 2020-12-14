import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class AttemptLocking {
    private ReentrantLock mLock = new ReentrantLock();

    public static void main(String[] args) {
        final AttemptLocking attemptLocking = new AttemptLocking();
        attemptLocking.untimed();
        attemptLocking.timed();
        new Thread() {
            {
                setDaemon(true);
            }

            @Override
            public void run() {
                attemptLocking.mLock.lock();
                System.out.println("acquired");
            }
        }.start();
        double temp = 0;
        for (int i = 0; i < 100000; i++) {
            temp += i / 2.0D;
        }
        Thread.yield();
        attemptLocking.untimed();
        attemptLocking.timed();
    }

    public void untimed() {
        boolean captured = mLock.tryLock();
        try {
            System.out.println("tryLock()=" + captured);
        } finally {
            if (captured) {
                mLock.unlock();
            }
        }
    }

    public void timed() {
        boolean captured = false;
        try {
            captured = mLock.tryLock(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            System.out.println("tryLock(2, TimeUnit.SECONDS):" + captured);
        } finally {
            if (captured) {
                mLock.unlock();
            }
        }
    }
}
