package Lab2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Producer extends Thread{
    Scanner sc;
    BoundedBufferRegion<Osoba> osobaBuffer;

    Producer(BoundedBufferRegion<Osoba> osobaBuf) throws FileNotFoundException {
        File file = new File("data.tsv");
        sc = new Scanner(file);
        osobaBuffer = osobaBuf;
    }

    public void run(){
        sc.nextLine();
        while (sc.hasNextLine()){
            String[] params = sc.nextLine().split("\t");
            Osoba os = new Osoba();
            os.setNconst(params[0]);
            os.setName(params[1]);
            os.setBirthYear(params[2]);
            os.setDeathYear(params[3]);
            os.setProffesions(params[4].split(","));
            os.setKnownTitles(params[5].split(","));
            try {
                osobaBuffer.put(os);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            osobaBuffer.put(null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
