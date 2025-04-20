package com.example.magic02.minithreadpool;

public interface RejectHandle {

    /**
     * 拒绝处理
     *
     * @param command           拒绝的任务
     * @param miniThreadPool    线程池
     */
    void reject(Runnable command, MiniThreadPool miniThreadPool);

}
