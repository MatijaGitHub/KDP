package Lab3;

import java.util.Random;

public class Producer extends Thread{
    Lab2.BoundedBufferRegion<Integer> buffer;
    Random ran;
    Producer(Lab2.BoundedBufferRegion<Integer> b){
        buffer = b;
        ran = new Random();

    }

    public void run(){
        while (true){
            int rand = ran.nextInt();
            try {
                buffer.put(rand);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
