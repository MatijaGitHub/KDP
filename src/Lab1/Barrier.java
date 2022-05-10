package Lab1;

public interface Barrier {
    public boolean await(long millis) throws InterruptedException;
    public void arrived() throws InterruptedException;
    public void reset();
}
