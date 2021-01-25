package com.example.concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 21.3.5显示的对象创建临界区
 */
public class ExplicitCriticalSection {
    public static void main(String[] args) {
        PairManager pairManager1 = new ExplicitPairManager1();
        PairManager pairManager2 = new ExplicitPairManager2();
        CriticalSection.testApproaches(pairManager1, pairManager2);
    }
}

class ExplicitPairManager1 extends PairManager {
    private Lock mLock = new ReentrantLock();

    @Override
    public void increment() {
        mLock.lock();
        try {
            pair.incrementX();
            pair.incrementY();
            store(getPair());
        } finally {
            mLock.unlock();
        }
    }
}

class ExplicitPairManager2 extends PairManager {
    private Lock mLock = new ReentrantLock();

    @Override
    public void increment() {
        Pair temp;
        mLock.lock();
        try {
            pair.incrementX();
            pair.incrementY();
            temp = getPair();
        } finally {
            mLock.unlock();
        }
        store(temp);
    }
}
