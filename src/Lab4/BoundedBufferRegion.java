package Lab4;

public class BoundedBufferRegion<T> {
    int size;
    int N;
    int head;
    int tail;
    final T[] buffer;
    BoundedBufferRegion(int s){
        N = s;
        buffer = (T[]) new Object[N];
        head = size = tail = 0;

    }
    public void put(T item){
        try {
            synchronized (buffer) {
                while (size == N) {
                    buffer.wait();
                }
                buffer[tail] = item;
                tail = (tail + 1) % N;
                size++;
                buffer.notifyAll();

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public T get(){
        try {
            synchronized (buffer) {
                while (size == 0) {
                    buffer.wait();
                }
                T item = buffer[head];
                head = (head + 1) % N;
                size--;
                buffer.notifyAll();
                return item;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
}
