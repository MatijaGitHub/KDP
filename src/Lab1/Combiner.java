package Lab1;

import java.util.concurrent.ConcurrentHashMap;

public class Combiner extends Thread {
    BoundedBufferRegion<Film> toCombiner;
    BarrierMonitor fromConsumer;
    BarrierMonitor waitForCombiner;
    BarrierMonitor signalPrinter;
    ConcurrentHashMap<Long,Integer> map;
    BoundedBufferRegion<Film> toPrinter;


    public Combiner(BoundedBufferRegion<Film> toCombiner, BarrierMonitor fromConsumer, BarrierMonitor waitForCombiner, ConcurrentHashMap<Long, Integer> map,BoundedBufferRegion<Film> toPr,BarrierMonitor sig) {
        this.toCombiner = toCombiner;
        this.fromConsumer = fromConsumer;
        this.waitForCombiner = waitForCombiner;
        this.map = map;
        this.toPrinter = toPr;
        signalPrinter = sig;
    }
    public void run(){
        try {
            fromConsumer.await(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Consumers done!");
        int globalMax = 0;
        int ids = 0;
        for(Integer i: map.values()){
            if(i > globalMax) {
                globalMax = i;
                ids = 0;
            }
            if(i == globalMax) ids++;
        }
        Consumer.globalMax = globalMax;
        try {
            waitForCombiner.arrived();
            System.out.println("Lab1.Combiner has arrived");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Number of maxes " + ids);
        while(true){
            try {

                Film film = toCombiner.get();
                if(film == null){
                    if(--ids == 0) break;

                }
                else {
                    toPrinter.put(film);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        try {
            toPrinter.put(null);
            signalPrinter.arrived();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }







    }
}
