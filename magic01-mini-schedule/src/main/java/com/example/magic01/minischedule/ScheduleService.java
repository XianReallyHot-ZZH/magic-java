package com.example.magic01.minischedule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 手搓定时任务-定时任务服务
 */
public class ScheduleService {

    private Trigger trigger = new Trigger();

    // 真正执行任务的线程池，只负责任务的执行，不能负责任务的调度触发
    private ExecutorService executorService = Executors.newFixedThreadPool(10);


    /**
     * 调度任务,每隔delay毫秒循环调度用户提交的任务
     *
     * @param task
     * @param delay
     */
    public void schedule(Runnable task, long delay) {
        schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * 调度任务,每隔delay时间单位循环调度用户提交的任务
     *
     * @param task
     * @param delay
     * @param unit
     */
    public void schedule(Runnable task, long delay, TimeUnit unit) {
        // 1、封装一个任务，放置到队列中
        Job job = new Job();
        job.setStartTime(System.currentTimeMillis() + unit.toMillis(delay));
        job.setTask(task);
        job.setDelay(delay);
        job.setUnit(unit);
        trigger.queue.offer(job);
        // 2、唤醒触发器线程
        trigger.wakeUp();
    }

    /**
     * 触发器，负责触发任务(说人话：等待合适的时间后，将对应的任务扔到线程池中)
     * 职责：将任务的执行和调度触发解耦开
     */
    class Trigger {

        // 优先级队列，这里排序的对象就是job的startTime，越早开始越靠前，在这里这个队列充当了一个有序的容器
        // 排序的好处就是：
        // 1、排在最前面的一定是需要最早执行的任务，触发器就可以以第一个的任务为基准，进行逻辑判断
        // 2、将排序的动作绑定到将任务放置到队列的行为上，避免调度时进行排序，提升性能
        private final PriorityBlockingQueue<Job> queue = new PriorityBlockingQueue<>();

        // 触发器线程，负责触发任务,调度逻辑都在这里
        private final Thread triggerThread = new Thread(() -> {
            while (true) {
                // cpu有虚假唤醒的问题，所以这里要用while，而不是if
                while (queue.isEmpty()) {
                    // 如果为空，那么就一直阻塞在这里
                    LockSupport.park();
                }
                // 走到这里说明队列中肯定有任务了
                // 获取最近的一个任务，peek方法不会删除
                Job latelyJob = queue.peek();
                if (latelyJob.getStartTime() <= System.currentTimeMillis()) {
                    // 走到这里说明最近的任务已经可以执行了
                    // 再获取队列中最近的一个任务，由于并发的问题，有可能在获取到latelyJob后，队列中最近的任务已经发生了变化，所以需要再次获取一下，poll方法会删除任务
                    Job job = queue.poll();
                    executorService.execute(job.getTask());
                    // 重新计算最近一个任务的开始时间，并重新放入队列中
                    Job nextJob = new Job();
                    nextJob.setStartTime(System.currentTimeMillis() + job.getUnit().toMillis(job.getDelay()));
                    nextJob.setTask(job.getTask());
                    nextJob.setDelay(job.getDelay());
                    nextJob.setUnit(job.getUnit());
                    queue.offer(nextJob);
                } else {
                    // 阻塞到最近任务的绝对时间点，避免频繁的cpu调度
                    LockSupport.parkUntil(latelyJob.getStartTime());
                }
            }
        });

        {
            triggerThread.start();
            System.out.println("Trigger触发器线程启动");
        }

        /**
         * 唤醒触发器线程
         * 唤醒时机：当有任务添加到队列的时候（新增周期调度任务），需要唤醒触发器线程，因为新增的任务有可能会成为最近的一个任务，那么需要打破线程的阻塞，进行一次调度逻辑判断
         */
        public void wakeUp() {
            LockSupport.unpark(triggerThread);
        }


    }

}
