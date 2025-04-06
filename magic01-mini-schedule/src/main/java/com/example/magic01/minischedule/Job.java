package com.example.magic01.minischedule;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * 对用户任务的封装，方便内部组件实现
 */
@Data
public class Job implements Comparable<Job> {

    // 用户任务
    private Runnable task;

    // 任务开始时间, startTime - now > 0，表示任务还没到执行时间，需要等待，否则可以执行
    private long startTime;

    private long delay;

    private TimeUnit unit;

    @Override
    public int compareTo(Job o) {
        return Long.compare(this.startTime, o.startTime);
    }
}
