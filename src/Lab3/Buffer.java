package Lab3;

public interface Buffer<T> {
    void put(T item);
    T get(int id);
}
