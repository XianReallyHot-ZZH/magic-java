package com.example.magic02.minithreadpool;

/**
 * 直接抛出异常
 */
public class ThrowRejectHandle implements RejectHandle {
    @Override
    public void reject(Runnable command, MiniThreadPool miniThreadPool) {
        System.out.println("触发ThrowRejectHandle拒绝策略：直接抛出异常");
        throw new RuntimeException("队列已满");
    }

}
