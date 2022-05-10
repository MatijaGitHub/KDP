package Lab1;

public class BarrierMonitor implements Barrier{
    private int arives;
    private int cpc;
    int round;
    boolean passed;

    public BarrierMonitor(int cpc) {
        this.cpc = cpc;
        this.arives = 0;
        round = 0;
        passed = false;
    }

    @Override
    public synchronized boolean await(long millis) throws InterruptedException {
        int myRound = round;
        if(!passed){
            //System.out.println("blocked " + round);
            while(myRound==round && !passed){
                long before = System.currentTimeMillis();
                wait(millis);
                long now = System.currentTimeMillis();
                if(millis!=0 && now - before >= millis) break;
            }

        }
        return true;
    }

    @Override
    public synchronized void arrived() throws InterruptedException {

        int myRound = round;
        //System.out.println("arived " + (arives+1));
        if(passed) return;
        if(++arives == cpc){
            passed = true;
            //System.out.println("Done with group " + round);
            notifyAll();

        }
        else{
            while (round == myRound && !passed)
                wait();



        }
    }

    @Override
    public synchronized void reset() {
        round++;
        passed = false;
        arives = 0;

    }
}
