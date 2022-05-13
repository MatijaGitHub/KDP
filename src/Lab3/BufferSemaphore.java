package Lab3;

import java.util.concurrent.Semaphore;

public class BufferSemaphore<T> implements Buffer<T>{
    int N;
    Semaphore isFull;
    Semaphore isEmpty;
    Semaphore mutex;
    int head;
    int tail;
    int size;
    T[] buffer;
    BufferSemaphore(int n){
        N = n;
        isFull = new Semaphore(N);
        isEmpty = new Semaphore(0);
        mutex = new Semaphore(1);
        head = tail = size = 0;
        buffer = (T[])new Object[N];
    }
    @Override
    public void put(T item) {
        try {
            isFull.acquireUninterruptibly();
            mutex.acquireUninterruptibly();
            buffer[tail] = item;
            tail = (tail + 1) % N;
            size++;
            mutex.release();
            isEmpty.release();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public T get(int id) {
        try {
            isEmpty.acquireUninterruptibly();
            mutex.acquireUninterruptibly();
            T it = buffer[head];
            head = (head + 1) % N;
            mutex.release();
            isFull.release();
            return  it;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
