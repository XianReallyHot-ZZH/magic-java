package com.example.magic01.minischedule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 测试
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        ScheduleService scheduleService = new ScheduleService();

        // 每过100ms打印一个1
        scheduleService.schedule(() -> {
            System.out.println(LocalDateTime.now().format(dateTimeFormatter) + " 100ms一次");
//            System.out.println("1");
        }, 100);

        Thread.sleep(1000);

        // 每过200ms打印一个2
        scheduleService.schedule(() -> {
            System.out.println(LocalDateTime.now().format(dateTimeFormatter) + " 200ms一次");
//            System.out.println("2");
        }, 200);



    }

}
