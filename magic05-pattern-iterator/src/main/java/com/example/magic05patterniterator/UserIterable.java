package com.example.magic05patterniterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 案例一：让User变得可迭代
 */
public class UserIterable implements Iterable<String> {

    private String name;

    private int age;

    public UserIterable(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public Iterator<String> iterator() {
        return new UserIterator();
    }


    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * 迭代器：具体迭代行为是去迭代自己的成员变量
     */
    class UserIterator implements Iterator<String> {

        int count = 2;

        @Override
        public boolean hasNext() {
            return count > 0;
        }

        @Override
        public String next() {
            count--;
            if (count == 1) {
                return UserIterable.this.name;
            }
            if (count == 0) {
                return String.valueOf(UserIterable.this.age);
            }
            throw new NoSuchElementException();
        }
    }


}
