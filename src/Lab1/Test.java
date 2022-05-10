package Lab1;

import java.io.FileNotFoundException;
import java.util.concurrent.ConcurrentHashMap;

public class Test {


    public static void main(String[] args) throws FileNotFoundException {
        BoundedBufferMonitor<Film> produced = new BoundedBufferMonitor<>();
        BoundedBufferMonitor<Film> toCombiner = new BoundedBufferMonitor<>();
        BoundedBufferMonitor<Film> toPrinter = new BoundedBufferMonitor<>();
        BarrierMonitor consumerBarr = new BarrierMonitor(10);
        BarrierMonitor combinerBarr = new BarrierMonitor(1);
        BarrierMonitor signalPrinter = new BarrierMonitor(1);
        ConcurrentHashMap<Long,Integer> stats = new ConcurrentHashMap<>();
        int k = 4000;
        ConcurrentHashMap<Long,Integer> map = new ConcurrentHashMap<>();
        new Producer(produced).start();
        for(int i = 0;i<10; i++){
            new Consumer(k,produced,consumerBarr,map,combinerBarr,toCombiner,stats).start();
        }
        new Combiner(toCombiner,consumerBarr,combinerBarr,map,toPrinter,signalPrinter).start();
        new Printer(5,toPrinter,signalPrinter,stats).start();
    }
}
