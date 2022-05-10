package Lab1;

import java.util.concurrent.Semaphore;

public class BarrierSemaphore implements Barrier{
    private int arives;
    private int cpc;
    int round;
    boolean passed;
    Semaphore sem1;
    Semaphore sem2;
    Semaphore mutex;


    public BarrierSemaphore(int cpc) {
        this.arives = 0;
        this.cpc = cpc;
        this.round = 0;
        this.passed = false;
        this.sem1 = new Semaphore(0);
        this.sem2 = new Semaphore(0);

        this.mutex = new Semaphore(1);

    }

    @Override
    public boolean await(long millis) throws InterruptedException {
        mutex.acquireUninterruptibly();
        int myRound = round;
        if(!passed){
            System.out.println("blocked " + round);
            while(myRound==round && !passed){
                mutex.release();

                sem2.acquireUninterruptibly();

                //mutex.acquireUninterruptibly();
            }
             return true;

        }
        mutex.release();
        return false;
    }

    @Override
    public void arrived() throws InterruptedException {
        mutex.acquireUninterruptibly();
        int myRound = round;
        System.out.println("arived " + (arives+1));
        if(passed){

            mutex.release();

            return;
        }
        if(++arives == cpc){

            passed = true;
            System.out.println("Done with group " + round);
            for(int i = 0 ; i < cpc-1;i++) {
                sem1.release();
            }

            sem2.release();


        }
        else{

            while(!passed && round == myRound){
                mutex.release();
                sem1.acquireUninterruptibly();
               // mutex.acquireUninterruptibly();

            }
            return;

        }
        mutex.release();

    }

    @Override
    public void reset() {
        mutex.acquireUninterruptibly();
        passed = false;
        arives = 0;
        round++;
        mutex.release();
    }
}
