package com.example.magic08patternflyweight;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用享元的思想模拟实现一个kafka的内存池bufferPool，实现对内存的分配与释放
 * 1、slot为可复用的缓存单元
 * 2、slot在必要的条件下是可以释放的，比如用户申请了一块内存，但是当前剩余的空闲内存不够了，但是加上部分释放的slot后又够了，那么该情况下缓存池就可以释放部分slot
 */
public class BufferPool {

    /**
     * 缓存池总大小
     */
    private final int totalSize;

    /**
     * 可复用的每个slot的大小
     */
    private final int slotSize;

    /**
     * 剩余可分配的空闲缓存大小，totalSize - slotSize * slotNum
     */
    private int free;

    /**
     * 存储缓存池中可复用的slot的容器
     */
    private final Deque<ByteBuffer> slotQueue = new ArrayDeque<>();

    /**
     * 正在等着分配资源的线程（请求）的condition对象容器
     */
    private final Deque<Condition> waiters = new ArrayDeque<>();

    /**
     * 锁,控制缓存池的并发分配和释放
     */
    private final Lock lock = new ReentrantLock();


    public BufferPool(int totalSize, int slotSize) {
        this.totalSize = totalSize;
        this.slotSize = slotSize;
        this.free = totalSize;
    }


    /**
     * 申请分配内存
     *
     * @param size          内存大小
     * @param timeout       超时时间
     * @return
     */
    public ByteBuffer allocate(int size, long timeout) throws InterruptedException {
        lock.lock();
        try {
            // 边界条件
            if (size > totalSize || size <= 0) {
                throw new IllegalArgumentException("你的申请容量异常：" + size);
            }

            // 复用的场景：申请的内存大小等于slot的块大小，并且刚好有可以复用的slot块
            if (size == slotSize && !slotQueue.isEmpty()) {
                return slotQueue.pollFirst();
            }

            // 剩余的全部空闲容量能够支持分配
            if ((free + slotQueue.size() * slotSize) >= size) {
                // 必要的话，尝试释放掉slot队列中的slot
                freeUp(size);
                // 走到这，说明当前的free肯定是大于等于size的，那么就从free中分配
                free -= size;
                // 成功分配
                return ByteBuffer.allocate(size);
            }

            // 剩余的全部空闲容量不能支持分配，那么我们需要在这进行等待，等待其他已申请内存的释放，直到有空闲容量够分配了，或者超时
            Condition condition = lock.newCondition();
            waiters.add(condition);
            long remainTime = timeout;
            try {
                while (true) {
                    long start = System.currentTimeMillis();
                    // 等待超时，返回false, 被唤醒，返回true
                    boolean wakeUp = condition.await(remainTime, TimeUnit.MILLISECONDS);
                    if (!wakeUp) {
                        throw new RuntimeException("未能在固定的时间内分配到内存");
                    }
                    // 走到这里表示是被唤醒了，那么尝试进行内存分配
                    if (size == slotSize && !slotQueue.isEmpty()) {
                        return slotQueue.pollFirst();
                    }
                    if ((free + slotQueue.size() * slotSize) >= size) {
                        freeUp(size);
                        free -= size;
                        return ByteBuffer.allocate(size);
                    }
                    // 走到这里，说明尝试获取分配内存失败，进行下一轮等待,更新剩余等待时间,
                    // 注意：这里remainTime有可能是会小于0的，不过不需要额外处理了，因为在condition.await(remainTime, TimeUnit.MILLISECONDS)的源码里对负数做了处理，负数都当做0处理了
                    remainTime = System.currentTimeMillis() - start;
                }
            } finally {
                // 不管成功与否，最终将自己的从等待队列中移除
                waiters.remove(condition);
            }
        } finally {
            // 如果还有线程等着分配内存，并且内存池中还有剩余的空闲内存，那么就唤醒一个线程去尝试获取分配
            if (!waiters.isEmpty() && !(free == 0 && slotQueue.isEmpty())) {
                waiters.peekFirst().signal();
            }
            lock.unlock();
        }
    }

    /**
     * 尝试释放出size大小的空闲内存
     * 注：这个方法是有一个前置的条件的，就是当前内存池中的剩余全部空闲内存能够支持分配size大小的内存
     * 即：free + slotQueue.size() * slotSize >= size
     *
     * @param size
     */
    private void freeUp(int size) {
        // 当前的free不够用，那么尝试释放slot，直到够用或者全部释放完
        while (free < size && !slotQueue.isEmpty()) {
            free += slotQueue.pollFirst().capacity();
        }
    }

    /**
     * 释放内存
     *
     * @param buffer
     */
    public void deallocate(ByteBuffer buffer) {
        lock.lock();
        try {
            // 符合复用的slot释放
            if (buffer.capacity() == this.slotSize) {
                buffer.clear();
                this.slotQueue.add(buffer);
            } else {
                // 普通内存的释放
                free += buffer.capacity();
            }
        } finally {
            // 如果存在正在等待分配内存的线程，则唤醒
            if (!waiters.isEmpty()) {
                waiters.peekFirst().signal();
            }
            lock.unlock();
        }
    }

}
