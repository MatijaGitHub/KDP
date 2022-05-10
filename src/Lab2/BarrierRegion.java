package Lab2;

public class BarrierRegion {
    private boolean passed;
    private int round;
    private int cap;
    private int count;
    BarrierRegion(int c){
        cap = c;
        passed = false;
        round = 0;
        count = 0;
    }
    public boolean await(long millis) throws InterruptedException {
        synchronized (this){
            if(passed) return true;
            int myRound = round;
            while(myRound == round && !passed){
                long before = System.currentTimeMillis();
                wait(millis);
                long after = System.currentTimeMillis();
                if(after - before >= millis && millis!=0) return false;

            }
            return true;
        }
    }
    public void arrived() throws InterruptedException {
        synchronized (this){

            if(passed) return;
            int myRound = round;
            if(++count == cap){
                passed = true;
                this.notifyAll();
            }
            else{
                while(myRound == round && !passed){
                    this.wait();
                }
            }

        }
    }
    public void reset(){
        synchronized (this){
            round++;
            passed = false;
            count = 0;
        }
    }
}
