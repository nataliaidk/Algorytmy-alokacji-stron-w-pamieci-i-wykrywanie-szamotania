
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;


public class Algorythms {



    public static int FRAME_SIZE;
    public static int Pages;
    public int Processes;
    public int interval;
    public static  int thrashingThreshold = 0; // Próg błędów strony dla wykrycia szamotania
    public static int thrashingCount; // Licznik wystąpień szamotania

    public static boolean isThrashing; // Flaga wskazująca, czy występuje szamotanie


    public Process[] ProcessesTable;

    ArrayList<Page> PageReferences = new ArrayList<Page>();
    ArrayList<Page> Frame = new ArrayList<Page>();
    public int errors = 0;

    /**
     * constructor for algorithms
     * @param FRAME_SIZE
     * @param PagesNr
     * @param interval
     * @param Processes
     */
    public Algorythms(int FRAME_SIZE, int PagesNr, int interval, int Processes) {
        this.FRAME_SIZE = FRAME_SIZE;
        this.Pages = PagesNr;
        this.Processes = Processes;
        this.interval = interval;
        ProcessesTable = new Process[Processes];


        for (int a = 0; a < PagesNr; a++) {
            int k = (int) (Math.random() * Processes);
            int r = (int) (Math.random() * interval);
            while (k == 0){k = (int) (Math.random() * Processes);}
            //creating page references
            PageReferences.add(new Page(r, 0, k));

        }

        for (int w = 0; w < Processes; w++) {
            ProcessesTable[w] = new Process(new ArrayList(), 0);
            for (int s = 0; s < PageReferences.size(); s++) {
                if ((PageReferences.get(s)).process == w) {
                    Process a = ProcessesTable[w];
                    a.process.add(PageReferences.get(s));
                }
            }
        }


    }

    /**
     * Metoda wykonująca algorytm EQUAL.
     * Dla każdego procesu w tablicy ProcessesTable tworzy kopię procesu i wykonuje na nim algorytm LRU.
     * Zwraca liczbę błędów stron.
     */
    public int EQUAL()
    {


        Process[] ProcessesCopy = new Process[Processes];
        int errors = 0;
        int size = FRAME_SIZE/ (ProcessesTable.length);
        for (int i = 0; i < ProcessesTable.length; i ++ )
        {
            ProcessesCopy[i] = new Process(ProcessesTable[i]);
            errors += LRU(ProcessesCopy[i].process, size);
        }
        return errors;

    }

    /**
     * Metoda wykonująca algorytm PROPORTIONAL.
     * Dla każdego procesu w tablicy ProcessesTable tworzy kopię procesu i ustala rozmiar ramki proporcjonalny do rozmiaru procesu.
     * Następnie wykonuje algorytm LRU na danym procesie z nowym rozmiarem ramki.
     * Zwraca liczbę błędów stron.
     */

    public int PROPORTIONAL()
    {
        thrashingCount = 0;
        Process[] ProcessesCopy = new Process[Processes];
        int errors = 0;
        int size = FRAME_SIZE/ (ProcessesTable.length);
        for (int i = 0; i < ProcessesTable.length; i ++ ) {
            ProcessesCopy[i] = new Process(ProcessesTable[i]);
            ProcessesCopy[i].setFrame(size);
        }
        for (int j = 0; j < ProcessesCopy.length; j ++) {
            size = ProcessesCopy[j].process.size() * size / Pages;

            if (size == 0) size = 3;
            errors += LRU(ProcessesCopy[j].process, size);
        }
        return errors;
    }
    /**
     * Metoda wykonująca algorytm PPF (Prepaging with Process Frames).
     * Dla każdego procesu w tablicy ProcessesTable tworzy kopię procesu i ustala rozmiar ramki.
     * Następnie iteruje, dopóki liczba procesów nie spadnie do zera.
     * W każdej iteracji znajduje proces, który generuje minimalną i maksymalną liczbę błędów stron.
     * Usuwa pierwszą stronę z procesu o minimalnej liczbie błędów stron i dodaje ją do błędów ogólnych.
     * Jeśli liczba procesów spadnie do 1, zwiększa rozmiar ramki procesu o pozostałej liczbie wolnych ramek.
     * Zwraca liczbę błędów stron.
     */

    public int PPF() {
        int errorMax = (int) 0.6 * Pages;
        int errors = 0;
        thrashingCount = 0;
        Process[] ProcessesCopy = new Process[Processes];
        int size = FRAME_SIZE / (ProcessesTable.length);
        for (int i = 0; i < ProcessesTable.length; i++) {
            ProcessesCopy[i] = new Process(ProcessesTable[i]);
            ProcessesCopy[i].setFrame(size);
        }
        int freeFrames = 0;
        int ProcessSize = Processes;
        while (ProcessSize != 0) {
            int min = interval;
            int max = 0;
            //index ktory generuje najmniej bledow
            int minIndex = 0;
            //index ktory generuje najwiecej bledow
            int maxIndex = 0;

            for (int j = 0; j < ProcessesCopy.length; j++) {
                Process p = ProcessesCopy[j];
                if (p != null && p.process.size() != 0) {
                    if (ProcessSize == 1) {
                        ProcessesCopy[j].setFrame(ProcessesCopy[j].FRAME_SIZE + freeFrames);
                        freeFrames = 0;
                    }
                    int pageFault = p.PPF;
                    HashMap<Integer, Integer> result = p.LRU(p.process);
                    int pageFaultSingle = result.get("errors");

                    if (pageFault > max) {
                        max = pageFault;
                        maxIndex = j;
                    }
                    if (pageFault < min) {
                        min = pageFault;
                        minIndex = j;
                    }
                    p.process.remove(0);
                    errors += pageFaultSingle;
                    thrashingCount += result.get("thrashingCount");
                } else if (p != null) {
                    if (ProcessesCopy[maxIndex] != null && maxIndex != j)
                        ProcessesCopy[maxIndex].setFrame(ProcessesCopy[maxIndex].FRAME_SIZE + ProcessesCopy[j].FRAME_SIZE);
                    else
                        freeFrames += ProcessesCopy[j].FRAME_SIZE;

                    ProcessesCopy[j] = null;
                    ProcessSize--;
                }

            }

        if (ProcessesCopy[minIndex] != null && ProcessesCopy[maxIndex] != null && ProcessesCopy[minIndex].PFrame != 1 && max > errorMax) {
            if (ProcessesCopy[minIndex].FRAME_SIZE > 3) {
                //za malo bledow - odejmujemt
                ProcessesCopy[minIndex].setFrame(ProcessesCopy[minIndex].FRAME_SIZE - 1);
                //za duzo bledow - dodajemy
                ProcessesCopy[maxIndex].setFrame(ProcessesCopy[maxIndex].FRAME_SIZE + 1 + freeFrames);
                freeFrames = 0;
            }
        }

        }
        return errors;
    }

    /**
     * Metoda wykonująca algorytm WSS (Working Set Size).
     * Dla każdego procesu w tablicy ProcessesTable tworzy kopię procesu i ustala rozmiar ramki na podstawie liczby duplikatów stron w określonej strefie.
     * Następnie przetwarza procesy w kolejności, w której zostały dodane, przydzielając wolne ramki.
     * Wykonuje algorytm LRU na danym procesie z przypisanym rozmiarem ramki.
     * Zwraca liczbę błędów stron.
     */
    public int WSS(int zone)
    {
        thrashingCount = 0;
        int freeFrames = FRAME_SIZE;
        int done = -1;
        Process[] ProcessesCopy = new Process[Processes];
        int errors = 0;

        for (int i = 0; i < ProcessesTable.length; i ++ ) {
            ProcessesCopy[i] = new Process(ProcessesTable[i]);
            int size = numberOfDuplications(ProcessesCopy[i].process, zone);
            ProcessesCopy[i].setFrame(size);
        }


        //czekamy na proces
        do {
            for (int j = done+1; j < ProcessesTable.length; j++) {
                //sprawdzamy czy sa wolne
                if (freeFrames > ProcessesCopy[j].FRAME_SIZE) {
                    done++;

                    //odejmowanie zajetychj juz
                    freeFrames -= ProcessesCopy[j].FRAME_SIZE;
                    if (ProcessesCopy[j].FRAME_SIZE != 0) {
                        int h = LRU(ProcessesCopy[j].process, ProcessesCopy[j].FRAME_SIZE);

                        errors += h;
                    }

                }


            }
            freeFrames = FRAME_SIZE;
            if (done == Processes -1)break;
        }
        while (done != Processes - 1);
        return errors;


    }


    public int LRU(ArrayList<Page> PagesRef, int FRAME_SIZE) {
        errors = 0;
        thrashingCount = 0;
        thrashingThreshold = PagesRef.size()/2;
        ArrayList<Page> lastUsedPages = new ArrayList<>();

        for (Page p : PagesRef) {
            lastUsedPages.add(new Page(p));
        }
        if (lastUsedPages.size() == 0) {
            return 0;
        }
        Page n;
        for (int i = 0; i < lastUsedPages.size(); i++) {
            n = lastUsedPages.get(i);
            loop:
            if (Frame.size() < FRAME_SIZE) {
                for (Page p : Frame) {
                    if (p.nr == n.nr) {
                        p.setRef(p.ref + 1);
                        break loop;
                    }
                }
                errors++;
                if (errors > thrashingThreshold) {
                    thrashingCount++;

                }
                Frame.add(n);
            } else {
                for (Page p : Frame) {
                    if (p.nr == n.nr) {
                        p.setRef(p.ref + 1);
                        break loop;
                    }
                }

                Collections.sort(Frame, Page.refComparator);

                Frame.remove(0);
                Frame.add(n);
                errors++;
                if (errors > thrashingThreshold) {
                    thrashingCount++;

                }

            }
        }
        Frame.clear();
        return errors;
    }


    public int numberOfDuplications(ArrayList<Page> a, int zone)   {
        if (a == null){ return 0;}
        HashSet<Integer> h = new HashSet<>();
        if(zone>a.size())
        {
            zone = a.size();
        }
        for(int i =0; i<zone; i++)
        {
            h.add(a.get(i).nr);
        }
        return h.size();
    }

}


