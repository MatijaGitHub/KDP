package Lab1;

public class BoundedBufferMonitor<T> implements BoundedBuffer<T>{
    public static int N = 1000;
    final T[] buffer;
    private int curr;
    private int head;
    private int cap;



    public BoundedBufferMonitor() {
        buffer = (T[])new Object[N];
        curr = 0;
        head = 0;
        cap = 0;


    }

    @Override
    public synchronized T get() throws InterruptedException {
        while(cap == 0)
            wait();
        T item = buffer[head];
        head = (head + 1) % N;
        cap--;
        notifyAll();
        return item;
    }

    @Override
    public synchronized void put(T o) throws InterruptedException {
        while(cap == N){
            wait();
        }
        buffer[curr] = o;
        curr = (curr + 1)%N;
        cap++;
        notifyAll();
    }

    @Override
    public synchronized int size() {
        return curr;
    }
}
