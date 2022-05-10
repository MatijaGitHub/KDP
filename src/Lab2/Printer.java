package Lab2;

import Lab1.BarrierMonitor;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Printer extends  Thread{
    BoundedBufferRegion<HashMap<Integer,Integer>> toPrinter;
    BarrierRegion signalPrinter;
    ConcurrentHashMap<Long,Integer> statistics;
    int N;

    public Printer(int n,BoundedBufferRegion<HashMap<Integer, Integer>> toPrinter, BarrierRegion signalPrinter, ConcurrentHashMap<Long, Integer> statistics) {
        this.toPrinter = toPrinter;
        this.signalPrinter = signalPrinter;
        this.statistics = statistics;
        N = n;
    }
    public void run(){
        try {
            while (!signalPrinter.await(N * 1000L)) {
                for(Long key:statistics.keySet()){
                    System.out.println("Consumer " + key + " has proccessed  " + statistics.get(key) + " people.");
                }
            }
            for(Long key:statistics.keySet()){
                System.out.println("Consumer " + key + " has proccessed  " + statistics.get(key) + " people.");
            }
            HashMap<Integer,Integer> decades = toPrinter.get();
            for(Integer key : decades.keySet()){
                int val = decades.get(key);
                System.out.println("In the " + key + "-" + (key+9) + " ," + val + " people were born." );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
