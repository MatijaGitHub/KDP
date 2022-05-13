package Lab3;

public interface Barrier {
    public boolean await(long millis);
    public void arrived();
    public void reset();
}
