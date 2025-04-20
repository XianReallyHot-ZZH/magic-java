package com.example.magic03.patterndecorator.set;

import java.util.*;

/**
 * 这是一个具有历史删除记录的set类，使用了装饰器设计模式，可以为入参的集合set类添加历史删除记录的能力
 * 装饰器设计模式的优点就是：不需要继承，基于组合的方式实现，有更高的灵活性，在实现功能任意组合、扩展、追加等需求场景下，该设计模式很合适
 *
 * @param <E>
 */
public class HistorySet<E> implements Set<E> {

    // 被装饰的集合
    private final Set<E> delegate;

    // 删除记录,被删除的元素都会被添加到该集合中
    private List<E> removeList = new ArrayList<>();

    public HistorySet(Set<E> delegate) {
        this.delegate = delegate;
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return delegate.iterator();
    }

    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return delegate.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return delegate.add(e);
    }

    /**
     * 删除元素的时候，需要记录删除的元素，所以重写了remove方法
     *
     * @param o object to be removed from this set, if present
     * @return
     */
    @Override
    public boolean remove(Object o) {
        if (delegate.remove(o)) {
            removeList.add((E) o);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return delegate.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return delegate.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return delegate.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return delegate.removeAll(c);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public String toString() {
        return delegate.toString() + " remove list : " + removeList;
    }
}
