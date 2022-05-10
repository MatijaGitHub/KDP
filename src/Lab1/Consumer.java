package Lab1;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Consumer extends Thread{
    BoundedBufferMonitor<Film> buff;
    BoundedBufferMonitor<Film> toCombiner;
    BarrierMonitor barr;
    ArrayList<Film> localMaxFilms;
    ConcurrentHashMap<Long,Integer> progress;
    int localMax;
    long id;
    int K;
    int proccessCount;
    static int globalMax;
    BarrierMonitor waitForCombiner;
    ConcurrentHashMap<Long,Integer> map;
    Consumer(int k,BoundedBufferMonitor<Film> buffer, BarrierMonitor bar, ConcurrentHashMap<Long,Integer> localMaxs,BarrierMonitor waitForCombiner,BoundedBufferMonitor<Film> cmb,ConcurrentHashMap<Long,Integer> prog){
        progress = prog;
        toCombiner = cmb;
        buff = buffer;
        localMax = 0;
        localMaxFilms = new ArrayList<>();
        barr = bar;
        map = localMaxs;
        id = this.getId();
        this.waitForCombiner = waitForCombiner;
        K = k;
        proccessCount = 0;
    }
    public void run(){
        Film film = null;

        try {


            while ((film = buff.get()) != null) {
                proccessCount++;
                if(proccessCount % K == 0){
                    progress.put(id,proccessCount);

                }
                if (film.getDirectors().length >= localMax) {
                    if (film.getDirectors().length > localMax) {
                        localMaxFilms.clear();
                        localMax = film.getDirectors().length;
                    }
                    localMaxFilms.add(film);
                }
                //System.out.println(film.getId());
            }
            buff.put(null);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        progress.put(id,proccessCount);

        map.put(id,localMax);
        try {
            barr.arrived();
            waitForCombiner.await(0);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(localMax == globalMax){
            try {

            for(Film film1 : localMaxFilms){

                    toCombiner.put(film1);

            }
            toCombiner.put(null);


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
