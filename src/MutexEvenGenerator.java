import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MutexEvenGenerator extends IntGenerator {
    private Lock mLock = new ReentrantLock();
    private int currentEvenValue = 0;

    public static void main(String[] args) {
        EvenChecker.test(new MutexEvenGenerator());
    }

    @Override
    public int next() {
        mLock.lock();
        try {
            currentEvenValue++;
            currentEvenValue++;
            return currentEvenValue;
        } finally {
            mLock.unlock();
        }
    }
}
