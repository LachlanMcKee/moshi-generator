package gsonpath.internal;

import java.util.Collection;
import java.util.Iterator;

public abstract class GsonPathElementCollection<E> implements Collection<E> {

    protected abstract Collection<E> getList();

    @Override
    public int size() {
        return getList().size();
    }

    @Override
    public boolean isEmpty() {
        return getList().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return getList().contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return getList().iterator();
    }

    @Override
    public Object[] toArray() {
        return getList().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return getList().toArray(a);
    }

    @Override
    public boolean add(E item) {
        return getList().add(item);
    }

    @Override
    public boolean remove(Object o) {
        return getList().remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return getList().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return getList().addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return getList().removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return getList().retainAll(c);
    }

    @Override
    public void clear() {
        getList().clear();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || getList().equals(obj);
    }

    @Override
    public int hashCode() {
        return getList().hashCode();
    }

    @Override
    public String toString() {
        return getList().toString();
    }
}
