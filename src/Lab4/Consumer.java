package Lab4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Consumer extends Thread{
    int id;
    BarrierSemaphore consumed;
    BoundedBufferRegion<Film> filmovi;
    BroadcastBufferSemaphore<Ocene> ocene;
    HashMap<String,Film> mojiFilmovi;
    HashMap<String,Ocene> maxFilmovi;
    ConcurrentHashMap<String,Film> allFilms;

    ConcurrentHashMap<Long,Integer> stats;
    BarrierSemaphore signalCombiner;
    BoundedBufferRegion<HashMap<String,Ocene>> toCombiner;
    Consumer(int i,BarrierSemaphore b,BoundedBufferRegion<Film> f, BroadcastBufferSemaphore<Ocene> o,ConcurrentHashMap<Long,Integer> st,BarrierSemaphore signal,BoundedBufferRegion<HashMap<String,Ocene>> toCom,ConcurrentHashMap<String,Film> allF){
        stats = st;
        filmovi = f;
        ocene = o;
        id = i;
        consumed = b;
        mojiFilmovi = new HashMap<>();
        allFilms = allF;

        maxFilmovi = new HashMap<>();
        signalCombiner = signal;
        toCombiner = toCom;
    }
    public void run(){
        Film film;
        while ((film = filmovi.get())!=null){

            mojiFilmovi.put(film.getTconst(),film);
        }
        filmovi.put(null);
        consumed.arrived();
        Ocene ocena;
        while ((ocena = ocene.get(id))!=null){
            if(!mojiFilmovi.containsKey(ocena.getNconst())) continue;
            if(stats.containsKey(this.getId()))
                stats.put(this.getId(),stats.get(this.getId()) + 1);
            else
                stats.put(this.getId(),1);
            Film film1 = mojiFilmovi.get(ocena.getNconst());
            if(!film1.getTitleType().equals("movie")) continue;

            if(film1.getGenres().equals("\\N")) continue;
            String[] genres = film1.getGenres().split(",");
            String decade = film1.getStartYear();
            if(decade.equals("\\N") ) continue;

            Integer dec =((Integer.parseInt(decade))-(Integer.parseInt(decade)%10));
            decade = dec.toString();
            allFilms.put(film1.getTconst(),film1);
            for(String s: genres){
                if(maxFilmovi.containsKey(decade + " " +s)){
                    if(ocena.getRating() > maxFilmovi.get(decade + " " +s).getRating()){
                        maxFilmovi.put(decade + " " + s,ocena);
                    }
                }
                else {
                    maxFilmovi.put(decade + " " + s,ocena);
                }
            }
        }
        ocene.put(null);

        toCombiner.put(maxFilmovi);
        signalCombiner.arrived();


    }
}
