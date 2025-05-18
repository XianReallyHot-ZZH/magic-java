package com.example.magic05patterniterator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 案例2：读取文件
 */
public class Main3 {

//    public static void main(String[] args) throws IOException {
//        List<User> userList = new ArrayList<>();
//
//        readUsers((User user) -> {
//            System.out.println(user);
//            userList.add(user);
//        });
//
//    }

    // 封装读取文件的具体实现：读取文件，展示文件，基础写法
    private static void readUsers(Consumer<User> consumer) throws IOException {
        List<String> lines = Files.readAllLines(new File(Main.class.getClassLoader().getResource("demo.user").getFile()).toPath());
        for (String line : lines) {
            String midString = line.substring(1, line.length() - 1);
            String[] split = midString.split(",");
            User user = new User(split[0], Integer.parseInt(split[1]));
            consumer.accept(user);
        }
    }

    /**
     * 重构后可以这么使用
     * @param args
     */
    public static void main(String[] args) {
        File file = new File(Main.class.getClassLoader().getResource("demo.user").getFile());
        UserFile userFile = new UserFile(file);
        for (User user : userFile) {
            System.out.println(user);
        }

    }


}
