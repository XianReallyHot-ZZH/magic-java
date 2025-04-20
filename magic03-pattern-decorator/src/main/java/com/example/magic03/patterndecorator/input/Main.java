package com.example.magic03.patterndecorator.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;

public class Main {

    /**
     * 案例说明：TestDemoFile.pdf为20M左右的测试文件，FileInputStream单字节读取耗时在20秒左右，BufferedFileInputStream单字节读取耗时在20多毫秒左右
     *
     * @param args
     */
    public static void main(String[] args) {

        File file = new File(Main.class.getClassLoader().getResource("TestDemoFile.pdf").getFile());
        long l = Instant.now().toEpochMilli();
        try (InputStream inputStream = new FileInputStream(file)) {
            while (true) {
                // 读取下一个字节（一个字节一个字节的读），每次读取，都需要进行io操作
                int bufferRead = inputStream.read();
                if (bufferRead == -1) {
                    break;
                }
            }
            System.out.println("用时:" + (Instant.now().toEpochMilli() - l) + "毫秒");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 使用装饰者模式，优化单字节读取的性能，减少io操作次数，提高性能
        l = Instant.now().toEpochMilli();
        try (BufferedFileInputStream inputStream = new BufferedFileInputStream(new FileInputStream(file))) {
            while (true) {
                // 读取下一个字节（一个字节一个字节的读），每次读取，先从缓存中读取
                int bufferRead = inputStream.read();
                if (bufferRead == -1) {
                    break;
                }
            }
            System.out.println("用时:" + (Instant.now().toEpochMilli() - l) + "毫秒");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
