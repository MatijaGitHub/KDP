package Lab2;

import Lab1.BarrierMonitor;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Combiner extends Thread{
    BarrierRegion syncWithConsumers;
    BoundedBufferRegion<List<Integer>> bufferYears;
    HashMap<Integer,Integer> stats;
    BoundedBufferRegion<HashMap<Integer,Integer>> toPrinter;
    BarrierRegion signalPrinter;
    Combiner(BoundedBufferRegion<List<Integer>> b,BarrierRegion s,BoundedBufferRegion<HashMap<Integer,Integer>> toPr,BarrierRegion signalPr){
        toPrinter =toPr;
        signalPrinter = signalPr;
        syncWithConsumers = s;
        bufferYears = b;
        stats = new HashMap<>();
    }
    public void run(){
        try {
            while (!syncWithConsumers.await(1)){
                combinerWork();
            }
            combinerWork();
            toPrinter.put(stats);
            signalPrinter.arrived();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void combinerWork() throws InterruptedException {
        bufferYears.put(null);
        List<Integer> curr = null;
        while((curr = bufferYears.get())!=null){
            for(Integer i : curr){
                int key = i -(i%10);
                if(stats.containsKey(key)){
                    stats.put(key,stats.get(key)+1);
                }
                else{
                    stats.put(key,1);
                }
            }
        }
    }

}
