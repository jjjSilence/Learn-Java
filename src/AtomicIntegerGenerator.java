import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerGenerator extends IntGenerator {
    private AtomicInteger i = new AtomicInteger(0);

    public static void main(String[] args) {
        EvenChecker.test(new AtomicIntegerGenerator());
    }

    @Override
    public int next() {
        return i.addAndGet(2);
    }
}
