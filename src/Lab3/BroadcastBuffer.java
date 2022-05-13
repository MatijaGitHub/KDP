package Lab3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BroadcastBuffer<T> implements Buffer<T>{
    Lock lock;
    Condition isEmpty;
    Condition[] isFull;
    int numOfConsumers;
    int[] consumed;
    int[] indexes;
    int N;
    T[] buffer;
    int head;
    int tail;
    int size;
    int[] sizes;

    BroadcastBuffer(int n,int numOfC){
        lock = new ReentrantLock();
        N = n;
        head = 0;
        tail = 0;
        size = 0;
        consumed = new int[N];
        numOfConsumers = numOfC;
        indexes = new int[numOfConsumers];
        buffer = (T[])new Object[N];
        isEmpty = lock.newCondition();
        isFull = new Condition[numOfConsumers];
        for(int i = 0; i < numOfConsumers;i++){
            isFull[i] = lock.newCondition();
        }
        sizes = new int[numOfConsumers];
    }
    @Override
    public void put(T item) {
        lock.lock();
        try {

            while (size == N){
                isEmpty.await();
            }
            buffer[tail] = item;
            tail = (tail + 1) % N;
            size++;
            for(int i = 0; i < numOfConsumers; i ++){
                sizes[i]++;
                isFull[i].signal();

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public T get(int id) {
        lock.lock();
        T ret = null;
        try {

            while (sizes[id] == 0){
                isFull[id].await();
            }
            ret = buffer[indexes[id]];
            if(++consumed[indexes[id]] == numOfConsumers){

                consumed[indexes[id]] = 0;
                size--;
                isEmpty.signal();
            }
            indexes[id] = (indexes[id] + 1)%N;
            sizes[id]--;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();

        }
        return ret;
    }
}
