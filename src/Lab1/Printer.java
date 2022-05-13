package Lab1;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Printer extends Thread{
    int N;
    ConcurrentHashMap<Long,Integer> stats;
    BoundedBufferRegion<Film> toPrinter;
    BarrierMonitor signalPrinter;

    public Printer(int n, BoundedBufferRegion<Film> toPrinter, BarrierMonitor signalPrinter,ConcurrentHashMap<Long,Integer> s) {
        N = n;
        this.toPrinter = toPrinter;
        this.signalPrinter = signalPrinter;
        this.stats = s;
    }
    public void run(){
        try {
            while (!signalPrinter.await(N* 1000L)){
                for(Long key: stats.keySet()){
                    //System.out.println("Lab1.Consumer " + key + " has consumed " + stats.get(key) + " films.");
                }
            }
            int n = 0;
            for(Long key: stats.keySet()){
                n+=stats.get(key);
            }
            System.out.println("Proccessed films = " + n);

            Film film = null;
            HashMap<String,Integer> result = new HashMap<>();
            while ((film = toPrinter.get())!= null){
                result.put(film.getId(),film.getDirectors().length);
                System.out.println(film.getId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
