package com.github.app.utils;


import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComputeFutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<String> shops = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        long t1 = System.nanoTime();
//        List<String> results = shops.parallelStream().map(ComputeFutureTest::caculate).collect(Collectors.toList());
//        List<CompletableFuture<String>> results = shops.stream().map(m -> CompletableFuture.supplyAsync(() -> ComputeFutureTest.caculate(m), executor)).collect(Collectors.toList());
//        results.stream().map(CompletableFuture::join).collect(Collectors.toList());
         CompletableFuture.supplyAsync(() -> ComputeFutureTest.caculate("123"), executor).thenAccept(System.out::println);
//        CompletableFuture<String> res2 = CompletableFuture.supplyAsync(() -> ComputeFutureTest.del("123"), executor);
//        CompletableFuture<String> str = res1.thenCombine(res2, (a, b) -> a + b);
//        System.out.println(str.get());
        long t2 = System.nanoTime();
        System.out.println(t2-t1);
        executor.shutdown();
    }

    public static String del(String s) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hellow";
    }

    public static String caculate(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return s;
    }
}
