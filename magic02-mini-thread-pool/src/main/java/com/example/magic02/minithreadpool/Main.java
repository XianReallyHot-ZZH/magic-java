package com.example.magic02.minithreadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        // jdk提供的线程池
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.AbortPolicy());
//        for (int i = 0; i < 8; i++) {
//            final int fi = i;
//            threadPoolExecutor.execute(() -> {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                System.out.println(Thread.currentThread().getName() + " " + fi);
//            });
//        }
//        System.out.println("主线程没有被阻塞");


        // 用自己写的的线程池
        RejectHandle rejectHandle = new ThrowRejectHandle();
//        RejectHandle rejectHandle = new DiscardRejectHandle();
        MiniThreadPool miniThreadPool = new MiniThreadPool(
                2,
                4,
                1000, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(5),
                rejectHandle);
        for (int i = 0; i < 20; i++) {
            final int fi = i;
            try {
                miniThreadPool.execute(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(Thread.currentThread().getName() + " " + fi);
                });
            } catch (Exception e) {
                System.out.println("任务提交执行出错，error：" + e.toString());
            }

        }
        System.out.println("主线程没有被阻塞");
    }

}
