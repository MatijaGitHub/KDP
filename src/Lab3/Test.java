package Lab3;
import Lab2.*;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Test {







    public static void main(String[] args){

        Lab2.BoundedBufferRegion<Integer> buffer = new Lab2.BoundedBufferRegion<>(1000);
        SemaphoreBarrier barrier = new SemaphoreBarrier(5);
        new Producer(buffer).start();
        for(int i = 0;i<5;i++){
            new Consumer(i,buffer,barrier).start();
        }
        boolean f = barrier.await(3);
        ReadWriteLock lock = new ReentrantReadWriteLock();

        lock.readLock();
        //read

    }
}
