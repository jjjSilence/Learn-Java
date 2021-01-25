package com.example.concurrency;

/**
 * 21.3.6在其他对象上同步
 */
public class SyncObject {
    public static void main(String[] args) {
        DualSync dualSync = new DualSync();
        new Thread(new Runnable() {
            @Override
            public void run() {
                dualSync.f();
            }
        }).start();
        dualSync.g();
    }
}

class DualSync {
    private Object syncObject = new Object();

    public synchronized void f() {
        for (int i = 0; i < 5; i++) {
            System.out.println("f()");
            Thread.yield();
        }
    }

    public void g() {
        synchronized (syncObject) {
            for (int i = 0; i < 5; i++) {
                System.out.println("g()");
                Thread.yield();
            }
        }
    }
}
