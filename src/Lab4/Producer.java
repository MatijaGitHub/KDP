package Lab4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Producer extends Thread{
    BarrierSemaphore consumersDone;
    BoundedBufferRegion<Film> buff1;
    BroadcastBufferSemaphore<Ocene> buff2;
    Scanner sc1;
    Scanner sc2;
    Producer(BarrierSemaphore b,BoundedBufferRegion<Film> d,BroadcastBufferSemaphore<Ocene> s) throws FileNotFoundException {
        consumersDone = b;
        buff1 = d;
        buff2 = s;
        sc1 = new Scanner(new File("titles.tsv"));
        sc2 = new Scanner(new File("ratings.tsv"));

    }


    public void run(){
        sc1.nextLine();
        while (sc1.hasNextLine()){
            String line = sc1.nextLine();
            String[] args = line.split("\t");
            Film film = new Film();
            film.setTconst(args[0]);
            film.setTitleType(args[1]);
            film.setPrimaryTitle(args[2]);
            film.setOriginalTitle(args[3]);
            film.setAdult(Boolean.parseBoolean(args[4]));
            film.setStartYear(args[5]);
            film.setEndYear(args[6]);
            film.setRuntimeMinutes(args[7]);
            film.setGenres(args[8]);
            buff1.put(film);
        }
        buff1.put(null);
        consumersDone.await(0);
        sc2.nextLine();
        while (sc2.hasNextLine()){
            String line = sc2.nextLine();
            String[] args = line.split("\t");
            Ocene oc = new Ocene();
            oc.setNconst(args[0]);
            oc.setRating(Double.parseDouble(args[1]));
            buff2.put(oc);

        }
        buff2.put(null);
        System.out.println("Producer done!");
    }
}
