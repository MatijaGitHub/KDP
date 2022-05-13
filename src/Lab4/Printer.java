package Lab4;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class Printer extends Thread{
    int seconds;
    ConcurrentHashMap<Long,Integer> stats;
    BoundedBufferRegion<HashMap<String,Ocene>> toPrint;
    BarrierSemaphore signalPrint;
    ConcurrentHashMap<String,Film> allFilms;
    Printer(int s,ConcurrentHashMap<Long,Integer> st,BoundedBufferRegion<HashMap<String,Ocene>> toPr,BarrierSemaphore signalPr,ConcurrentHashMap<String,Film> allF){
        stats = st;
        seconds = s;
        toPrint = toPr;
        signalPrint = signalPr;
        allFilms = allF;
    }


    public void run(){
        while (!signalPrint.await(seconds*1000L)){
            for(Long thread:stats.keySet()){
                System.out.println("Thread " + thread + " has consumed " + stats.get(thread) + " films.");
            }
        }
        for(Long thread:stats.keySet()){
            System.out.println("Thread " + thread + " has consumed " + stats.get(thread) + " films.");
        }
        toPrint.put(null);
        HashMap<String,Ocene> res;
        while ((res = toPrint.get())!=null){
            String last = "";
            for(String f : new TreeSet<String>(res.keySet())){
                String[] args = f.split(" ");
                if(!args[0].equals(last)){
                    last  = args[0];
                    System.out.println("For decade " + args[0]);
                }
                System.out.println("\tGenre " + args[1] + " best rated film is " + allFilms.get(res.get(f).getNconst()).getPrimaryTitle() + " " + res.get(f).getRating());
            }
        }

    }
}
