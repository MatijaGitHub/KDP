package Lab4;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class BarrierSemaphore {
    Semaphore arrived;
    Semaphore leave;
    Semaphore await;
    int numOfArrivals;
    int cnt;
    BarrierSemaphore(int n){
        arrived = new Semaphore(1);
        leave = new Semaphore(0);
        numOfArrivals = n;
        cnt = 0;
        await = new Semaphore(0);

    }
    public void arrived(){
        arrived.acquireUninterruptibly();
        if(++cnt != numOfArrivals){
            arrived.release();
            leave.acquireUninterruptibly();
            arrived.acquireUninterruptibly();
        }
        arrived.release();
        await.release();
        leave.release();

    }
    public boolean await(long millis){
        try {

            arrived.acquireUninterruptibly();
            if (cnt == numOfArrivals) {
                arrived.release();
                return true;
            }
            arrived.release();
            if (millis != 0) {
                long before = System.currentTimeMillis();
                await.tryAcquire(millis, TimeUnit.MILLISECONDS);
                long after = System.currentTimeMillis();
                if( after - before >=millis){
                    arrived.acquireUninterruptibly();
                    boolean b = cnt == numOfArrivals;
                    arrived.release();
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
}
