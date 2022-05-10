package Lab1;

import java.util.concurrent.Semaphore;

public class BoundedBufferSemaphore<T> implements BoundedBuffer<T>{
    Semaphore put;
    Semaphore get;
    Semaphore mutex;
    public static int N = 5;
    final T[] buffer;
    private int curr;

    public BoundedBufferSemaphore() {
        this.put = new Semaphore(N);
        this.get = new Semaphore(0);
        this.mutex = new Semaphore(1);
        this.buffer = (T[])new Object[N];
        this.curr = curr = 0;
    }

    @Override
    public T get() throws InterruptedException {

        get.acquireUninterruptibly();
        mutex.acquireUninterruptibly();
        T item = buffer[--curr];
        mutex.release();
        put.release();
        return item;
    }

    @Override
    public void put(T o) throws InterruptedException {

        put.acquireUninterruptibly();
        mutex.acquireUninterruptibly();
        buffer[curr++] = o;
        mutex.release();
        get.release();


    }

    @Override
    public int size() {
        return curr;
    }
}
