package Lab3;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreBarrier implements Barrier{
    int N;
    int arrived;

    Semaphore arrive;
    Semaphore passed;
    Semaphore await;
    SemaphoreBarrier(int n){
        N = n;
        arrive = new Semaphore(1);

        arrived = 0;
        passed = new Semaphore(0);
        await = new Semaphore(0);
    }
    @Override
    public boolean await(long millis) {
        try {
            arrive.acquireUninterruptibly();
            if (arrived == N) {
                arrive.release();
                return true;
            }
            arrive.release();
            if(millis!=0) {
                long before = System.currentTimeMillis();
                await.tryAcquire(millis, TimeUnit.SECONDS);
                long now = System.currentTimeMillis();
                if (now - before >= millis) {
                    arrive.acquireUninterruptibly();
                    boolean b = arrived == N;
                    arrive.release();
                    return b;
                }
            }
            else {
                await.acquireUninterruptibly();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void arrived() {
        try {

            arrive.acquireUninterruptibly();
            if(++arrived != N){
                arrive.release();
                passed.acquireUninterruptibly();
                arrive.acquireUninterruptibly();
            }
            else {
                await.release();
            }
            arrive.release();
            passed.release();



        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void reset() {

    }
}
