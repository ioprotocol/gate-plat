package com.github.app.utils;

import java.util.concurrent.*;

public class Test {
    public static void main(String[] args) throws Exception {
        //第一种方式
        ExecutorService executor = Executors.newCachedThreadPool();

        Semaphore semaphore = new Semaphore(5);

        for(int i = 0;i < 10; i++) {
            final int index = i;
            Thread.sleep(1000);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("index:" + index + " wait acquire");
                        semaphore.acquire();
                        System.out.println("index:" + index + " acquired");
                        Thread.sleep(8*1000);
                        semaphore.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executor.shutdown();
    }

}
