package com.github.app.utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownDemo {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(3);

        executor.submit(new Woker(countDownLatch));
        Thread.sleep(1000);
        executor.submit(new Woker(countDownLatch));
        Thread.sleep(1000);
        executor.submit(new Woker(countDownLatch));

        System.out.println("await");
        countDownLatch.await();
        System.out.println("okkk");
        executor.shutdown();
    }

    public static class Woker implements Runnable {
        private CountDownLatch countDownLatch;

        public Woker(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("finish:" + countDownLatch.getCount());
            } finally {
                countDownLatch.countDown();
            }
        }
    }
}
