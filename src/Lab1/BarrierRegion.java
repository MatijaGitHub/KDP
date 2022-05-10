package Lab1;

public class BarrierRegion implements Barrier{
    private int arives;
    private int cpc;
    int round;
    boolean passed;

    @Override
    public boolean await(long millis) throws InterruptedException {
        return false;
    }

    @Override
    public void arrived() throws InterruptedException {

    }

    @Override
    public void reset() {

    }
}
