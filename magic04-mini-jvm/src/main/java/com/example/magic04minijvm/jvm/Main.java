package com.example.magic04minijvm.jvm;

public class Main {

    public static void main(String[] args) throws Exception {
        // 模拟JVM启动,如果classPathString内含多个路径,则用;分隔（linux下;分割，windows下用:分割）
        Hotspot hotspot = new Hotspot("com.example.magic04minijvm.demo.Demo", "D:\\Developer\\Github\\my-projects\\magic-java\\magic04-mini-jvm\\target\\classes");
        hotspot.start();
    }

}
