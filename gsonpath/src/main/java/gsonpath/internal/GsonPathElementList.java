package gsonpath.internal;

import java.util.*;

public abstract class GsonPathElementList<E> extends GsonPathElementCollection<E>
        implements List<E> {

    protected abstract List<E> getList();

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return getList().addAll(index, c);
    }

    @Override
    public E get(int index) {
        return getList().get(index);
    }

    @Override
    public E set(int index, E element) {
        return getList().set(index, element);
    }

    @Override
    public void add(int index, E element) {
        getList().add(index, element);
    }

    @Override
    public E remove(int index) {
        return getList().remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return getList().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return getList().lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return getList().listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return getList().listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return getList().subList(fromIndex, toIndex);
    }
}
