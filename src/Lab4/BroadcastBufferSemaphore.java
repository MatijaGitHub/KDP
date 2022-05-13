package Lab4;

import java.util.concurrent.Semaphore;

public class BroadcastBufferSemaphore<T> {
    Semaphore putS;
    Semaphore[] getS;
    Semaphore mutex;
    int[] heads;
    int[] takens;
    int N;
    int tail;

    int numOfConsumers;

    T[] buffer;
    BroadcastBufferSemaphore(int n,int numOfCons){
        numOfConsumers = numOfCons;
        N = n;
        putS = new Semaphore(N);
        mutex = new Semaphore(1);
        tail =0;

        getS = new Semaphore[numOfConsumers];
        heads = new int[numOfConsumers];
        for (int i = 0; i < numOfConsumers;i++){
            getS[i] = new Semaphore(0);
            heads[i] = 0;
        }
        takens = new int[N];
        buffer = (T[])new Object[N];

    }
    public void put(T item){
        putS.acquireUninterruptibly();
        mutex.acquireUninterruptibly();
        buffer[tail] = item;
        tail = (tail + 1) % N;
        mutex.release();
        for(int i = 0; i < numOfConsumers;i++){
            getS[i].release();
        }
    }
    public T get(int id){
        getS[id].acquireUninterruptibly();
        mutex.acquireUninterruptibly();
        T item = buffer[heads[id]];
        if(++takens[heads[id]] == numOfConsumers){
            takens[heads[id]] = 0;
            putS.release();
        }
        heads[id] = (heads[id] + 1) % N;
        mutex.release();
        return  item;
    }

}
