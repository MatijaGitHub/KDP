package Lab3;

public class Consumer extends Thread{
    int id;
    int count;
    SemaphoreBarrier bar;
    Lab2.BoundedBufferRegion<Integer> buffer;
    Consumer(int i,Lab2.BoundedBufferRegion<Integer> buf,SemaphoreBarrier barrier){
        id = i;
        buffer = buf;
        count = 0;
        bar = barrier;
    }
    public void run(){
        bar.arrived();
        System.out.println("Im out :)");
    }
}
