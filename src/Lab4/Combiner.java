package Lab4;

import java.util.HashMap;

public class Combiner extends  Thread{
    BarrierSemaphore waitForConsumers;
    BoundedBufferRegion<HashMap<String,Ocene>> rezultati;
    HashMap<String,Ocene> globalResult;
    BoundedBufferRegion<HashMap<String,Ocene>> toPrint;
    BarrierSemaphore signalPrint;
    Combiner(BarrierSemaphore w,BoundedBufferRegion<HashMap<String,Ocene>> rez,BoundedBufferRegion<HashMap<String,Ocene>> toPr,BarrierSemaphore signalPr){
        rezultati = rez;
        waitForConsumers = w;
        globalResult = new HashMap<>();
        toPrint = toPr;
        signalPrint = signalPr;
    }
    public void run(){
        waitForConsumers.await(0);
        rezultati.put(null);
        HashMap<String,Ocene> curr;
        while ((curr = rezultati.get())!=null){
            for(String key:curr.keySet()){
                if(!globalResult.containsKey(key)){
                    globalResult.put(key,curr.get(key));
                }
                else {
                    if(curr.get(key).getRating() > globalResult.get(key).getRating()){
                        globalResult.put(key,curr.get(key));
                    }
                }
            }
        }
        toPrint.put(globalResult);
        signalPrint.arrived();
        System.out.println("Combiner done!");
    }
}
