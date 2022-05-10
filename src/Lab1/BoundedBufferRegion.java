package Lab1;

import java.util.concurrent.locks.ReentrantLock;

public class BoundedBufferRegion<T> implements BoundedBuffer<T>{

    public static int N = 5;
    final T[] buffer;
    private int curr;
    ReentrantLock lock = new ReentrantLock();

    public BoundedBufferRegion() {
        this.buffer = (T[])new Object[N];
        this.curr = 0;
    }

    @Override
    public T get() throws InterruptedException {
        synchronized (buffer){
            while(curr == 0){
                buffer.wait();
            }
            T item = buffer[--curr];
            buffer.notifyAll();
            return item;
        }
    }

    @Override
    public void put(T o) throws InterruptedException {
        synchronized (buffer){
            while(curr == N){
                buffer.wait();
            }
            buffer[curr++] = o;
            buffer.notifyAll();

        }

    }

    @Override
    public int size() {
        return curr;
    }
}
