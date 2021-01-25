package com.example.concurrency;

public class MultiLock {
    public static void main(String[] args) {
        final MultiLock multiLock = new MultiLock();
        new Thread(new Runnable() {
            @Override
            public void run() {
                multiLock.f1(10);
            }
        }).start();
    }

    public synchronized void f1(int count) {
        if (count-- > 0) {
            System.out.println("f1() call f2() with count " + count);
            f2(count);
        }
    }

    public synchronized void f2(int count) {
        if (count-- > 0) {
            System.out.println("f2() call f1() with count " + count);
            f1(count);
        }
    }
}
