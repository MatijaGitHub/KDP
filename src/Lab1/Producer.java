package Lab1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Producer extends Thread{
    Scanner sc;
    BoundedBufferRegion<Film> buff;
    Producer(BoundedBufferRegion<Film> buffer) throws FileNotFoundException {
        File input = new File("data1.tsv");
        sc = new Scanner(input);
        buff = buffer;
    }
    public void run(){
        int films = 0;
        sc.nextLine();
        while (sc.hasNextLine()){

            String line = sc.nextLine();
            String[] args = line.split("\t");
            if(args[1].equals("\\N")){
                continue;
            }
            films++;
            String[] directors = args[1].split(",");
            String[] writers = null;
            if(!args[2].equals("\\N")){
                writers = args[2].split(",");
            }
            Film film = new Film();
            film.setId(args[0]);
            film.setDirectors(directors);
            film.setWriters(writers);
            try {
                buff.put(film);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            buff.put(null);
            System.out.println("Produced films: " +films);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
