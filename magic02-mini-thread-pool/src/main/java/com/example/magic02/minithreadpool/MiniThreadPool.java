package com.example.magic02.minithreadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 一款mini 线程池
 * 提供了线程池的基本能力：
 * 1、支持设置核心线程数、最大线程数、拒绝策略
 * 2、支持任务提交+自动调度
 */
public class MiniThreadPool {

    // 核心线程数
    private final int corePoolSize;
    // 最大线程数
    private final int maximumPoolSize;
    // 空闲时间
    private final long keepAliveTime;
    // 空闲时间单位
    private final TimeUnit unit;
    // 任务队列
    private final BlockingQueue<Runnable> workQueue;
    // 拒绝策略
    private final RejectHandle rejectHandle;

    // 核心线程集合
    private final List<CoreThread> coreThreads = new ArrayList<>();

    // 辅助线程集合
    private final List<SupportThread> supportThreads = new ArrayList<>();

    public MiniThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectHandle rejectHandle) {
        if (corePoolSize < 0 || maximumPoolSize <= 0 || maximumPoolSize < corePoolSize || keepAliveTime < 0) {
            throw new IllegalArgumentException();
        }
        if (workQueue == null || rejectHandle == null) {
            throw new NullPointerException();
        }
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.unit = unit;
        this.workQueue = workQueue;
        this.rejectHandle = rejectHandle;
    }

    /**
     * 核心方法：接受任务
     *
     * @param command
     */
    public void execute(Runnable command) {
        // 判断核心线程数，如果小于核心线程数，那么就创建核心线程
        if (coreThreads.size() < corePoolSize) {
            CoreThread coreThread = new CoreThread(command);
            coreThreads.add(coreThread);
            coreThread.start();
            return;
        }
        // 如果队列没有满，那么就加入队列
        if (workQueue.offer(command)) {
            return;
        }
        // 走到这，说明队列满了，那么就要判断一下是否需要创建辅助线程
        if (supportThreads.size() + corePoolSize < maximumPoolSize) {
            SupportThread supportThread = new SupportThread(command);
            supportThreads.add(supportThread);
            supportThread.start();
            return;
        }
        // 走到这，说明队列也满了，辅助线程也满了，那么就要执行拒绝策略
        if (!workQueue.offer(command)) {
            rejectHandle.reject(command, this);
        }
    }

    /**
     * 丢弃最早的任务
     */
    public void discardEarlyTask() {
        workQueue.poll();
    }


    /**
     * 核心线程
     */
    class CoreThread extends Thread {

        private final Runnable firstTask;

        public CoreThread(Runnable firstTask) {
            this.firstTask = firstTask;
        }

        /**
         * 线程逻辑
         */
        @Override
        public void run() {
            firstTask.run();
            // 死循环,为了复用线程
            while (true) {
                // 核心线程：死循环，一直获取队列中的任务，然后执行，如果队列中没有任务，那么核心线程需要阻塞
                try {
                    workQueue.take().run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 辅助线程：达到核心线程数的时候，会创建辅助线程，辅助线程会去获取队列中的任务，然后执行，如果辅助线程空闲时间达到设置的时长，那么辅助线程需要被销毁
     */
    class SupportThread extends Thread {

        private final Runnable firstTask;

        public SupportThread(Runnable firstTask) {
            this.firstTask = firstTask;
        }

        @Override
        public void run() {
            firstTask.run();
            while (true) {
                try {
                    Runnable task = workQueue.poll(keepAliveTime, unit);
                    if (task != null) {
                        // 有任务，那么就执行任务，然后继续死循环
                        task.run();
                    } else {
                        // 达到指定的空闲时间还没有获取到任务，跳出循环逻辑，逻辑执行完毕后，线程会被jvm自动回收
                        System.out.println(Thread.currentThread().getName() + "辅助线程空闲了，销毁！");
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }







}
