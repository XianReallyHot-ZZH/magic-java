package com.example.magic05patterniterator;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * 赋予文件具备迭代文件内容的能力
 */
public class UserFile implements Iterable<User> {

    private final File file;

    public UserFile(File file) {
        this.file = file;
    }

    @Override
    public Iterator<User> iterator() {
        return new UserFileIterator();
    }

    /**
     * 迭代的对象为user
     */
    class UserFileIterator implements Iterator<User> {

        List<User> users = loadUsersFromFile();

        int cursor = 0;

        private List<User> loadUsersFromFile() {
            try {
                return Files.readAllLines(file.toPath()).stream().map(line -> {
                    String midString = line.substring(1, line.length() - 1);
                    String[] split = midString.split(",");
                    return new User(split[0], Integer.parseInt(split[1]));
                }).collect(Collectors.toList());
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }

        @Override
        public boolean hasNext() {
            return cursor < users.size();
        }

        @Override
        public User next() {
            if (cursor >= users.size()) {
                throw new NoSuchElementException();
            }
            return users.get(cursor++);
        }
    }

}
