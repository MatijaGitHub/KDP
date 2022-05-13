package Lab2;

public class BoundedBufferRegion<T> {
    int N;
    int size;
    int tail;
    int head;
    final T[] buffer;
    public BoundedBufferRegion(int n){
        this.N = n;
        size = 0;
        tail = 0;
        head = 0;
        buffer = (T[])new Object[N];
    }
    public T get() throws InterruptedException {
        synchronized (buffer){
            while(size == 0){
                buffer.wait();
            }
            T ret = buffer[head];
            head = (head + 1) % N;
            size--;
            buffer.notifyAll();
            return ret;
        }
    }
    public void put(T item) throws InterruptedException {
        synchronized (buffer){
            while (size == N){
                buffer.wait();
            }
            buffer[tail] = item;
            tail = (tail + 1) % N;
            size++;
            buffer.notifyAll();
        }
    }
}
