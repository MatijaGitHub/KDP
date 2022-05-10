package Lab1;

import java.util.ArrayList;

public interface BoundedBuffer<T> {
    public T get() throws InterruptedException;
    public void put(T o) throws InterruptedException;
    public int size();
}
