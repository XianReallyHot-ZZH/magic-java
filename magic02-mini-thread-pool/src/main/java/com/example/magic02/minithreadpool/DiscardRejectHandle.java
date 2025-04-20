package com.example.magic02.minithreadpool;

/**
 * 丢弃最早的任务
 */
public class DiscardRejectHandle implements RejectHandle {
    @Override
    public void reject(Runnable command, MiniThreadPool miniThreadPool) {
        System.out.println("触发DiscardRejectHandle拒绝策略：丢弃最早的任务");
        // 丢弃最早的任务
        miniThreadPool.discardEarlyTask();
        // 再次执行任务
        miniThreadPool.execute(command);
    }
}
