package Lab4;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Test {


    public static void main(String[] args) throws FileNotFoundException {
        long before = System.currentTimeMillis();
        int n = 5;
        BoundedBufferRegion<Film> films = new BoundedBufferRegion<>(1000);
        BroadcastBufferSemaphore<Ocene> ocene = new BroadcastBufferSemaphore<Ocene>(1000,n);
        BarrierSemaphore consumersDone = new BarrierSemaphore(n);
        ConcurrentHashMap<Long,Integer> st = new ConcurrentHashMap<>();
        BarrierSemaphore signalCombiner = new BarrierSemaphore(n);
        BoundedBufferRegion<HashMap<String,Ocene>> toCom = new BoundedBufferRegion<>(1000);
        BoundedBufferRegion<HashMap<String,Ocene>> toPr = new BoundedBufferRegion<>(1000);
        BarrierSemaphore signalPr = new BarrierSemaphore(1);
        ConcurrentHashMap<String,Film> allFilms = new ConcurrentHashMap<>();
        new Producer(consumersDone,films,ocene).start();
        for(int i = 0; i < n;i++){
            new Consumer(i,consumersDone,films,ocene,st,signalCombiner,toCom,allFilms).start();
        }
        new Combiner(signalCombiner,toCom,toPr,signalPr).start();
        Printer printer =new Printer(2,st,toPr,signalPr,allFilms);
        printer.start();
        try {
            printer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long after = System.currentTimeMillis();
        System.out.println("Time elapsed: " + (after - before)/1000 + "s");

    }

}
