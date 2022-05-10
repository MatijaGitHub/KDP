package Lab2;

import Lab1.BarrierMonitor;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Test {

    public static void main(String[] args) throws FileNotFoundException {
        int numOfConsumers = 20;
        BoundedBufferRegion<Osoba> osobe = new BoundedBufferRegion<>(1000);
        BoundedBufferRegion<List<Integer>> toCombiner = new BoundedBufferRegion<>(1000);
        BarrierRegion signalCombiner = new BarrierRegion(numOfConsumers);
        BarrierRegion signalPrinter = new BarrierRegion(1);
        ConcurrentHashMap<Long,Integer> statistics = new ConcurrentHashMap<>();
        BoundedBufferRegion<HashMap<Integer,Integer>> toPrinter = new BoundedBufferRegion<>(10);
        new Producer(osobe).start();
        for(int i = 0; i < numOfConsumers;i++){
            new Consumer(2000,osobe,toCombiner,signalCombiner,statistics).start();
        }
        new Combiner(toCombiner,signalCombiner,toPrinter,signalPrinter).start();
        new Printer(4,toPrinter,signalPrinter,statistics).start();

    }
}
