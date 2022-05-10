package Lab2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Consumer extends Thread{
    int N;
    int osobasConsumed;
    ArrayList<Integer> localBirthYears;
    BoundedBufferRegion<Osoba> fromProducer;
    BoundedBufferRegion<List<Integer>> toCombiner;
    BarrierRegion syncWithCombiner;
    ConcurrentHashMap<Long,Integer> statistics;
    Consumer(int n,BoundedBufferRegion<Osoba> fr,BoundedBufferRegion<List<Integer>> list,BarrierRegion sync,ConcurrentHashMap<Long,Integer> stats){
        N = n;
        toCombiner = list;
        syncWithCombiner = sync;
        fromProducer = fr;
        statistics = stats;
        localBirthYears = new ArrayList<>();
    }
    public void run(){
        Osoba osoba = null;
        try {
            while ((osoba = fromProducer.get()) != null) {

                if(osoba.getDeathYear().equals("\\N")){

                    for(String prof : osoba.getProffesions()){

                        if(prof.equals("actor") || prof.equals("actress")){
                            if(!osoba.getBirthYear().equals("\\N"))
                                localBirthYears.add(Integer.parseInt(osoba.getBirthYear()));
                            break;
                        }

                    }

                }
                if(++osobasConsumed % N == 0){
                    toCombiner.put(localBirthYears);
                    localBirthYears = new ArrayList<Integer>();
                    statistics.put(this.getId(),osobasConsumed);
                }
            }
            toCombiner.put(localBirthYears);
            statistics.put(this.getId(),osobasConsumed);
            fromProducer.put(null);
            syncWithCombiner.arrived();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
