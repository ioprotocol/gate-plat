package com.github.app.utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        ReentrantLock reentrantLock = new ReentrantLock();

        executor.submit(new Woker("123", reentrantLock));
        Thread.sleep(1000);
        executor.submit(new Woker("456", reentrantLock));

        executor.shutdown();
    }

    public static class Woker implements Runnable {
        private ReentrantLock reentrantLock;
        private String name;

        public Woker(String name, ReentrantLock reentrantLock) {
            this.name = name;
            this.reentrantLock = reentrantLock;
        }

        @Override
        public void run() {
            try {
                reentrantLock.lock();
                System.out.println(name + " getLock:");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                reentrantLock.unlock();
                System.out.println(name + " reltLock:");
            }
        }
    }
}
