import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Process {

    ArrayList process;
    int PFrame;
    int PPF = 0;
    int FRAME_SIZE;
    public static  int thrashingThreshold; // Próg błędów strony dla wykrycia szamotania
    public static int thrashingCountFinal = 0; // Licznik wystąpień szamotania
    public static int thrashingCount; // Licznik wystąpień szamotania
    public static boolean isThrashing; // Flaga wskazująca, czy występuje szamotanie
    ArrayList<Page> Frame = new ArrayList<Page>();
    public Process(ArrayList proces, int PFrame)
    {
        this.PFrame = PFrame;
        this.process = proces;
        this.PPF = PPF;

    }
    public Process(Process p)
    {
        this.process = p.process;
        this.PFrame = p.PFrame;
    }
    public void setFrame(int frame) {
        FRAME_SIZE = frame;
    }

    public void setPPF(int PPF) {
        this.PPF = PPF;
    }

    public void setProcess(ArrayList process) {
        this.process = process;
    }


    public HashMap<String, Integer> LRU(ArrayList<Page> PageRef ) {
        HashMap<String, Integer> result = new HashMap<>();
        thrashingCount = 0;
        thrashingThreshold = FRAME_SIZE/2;
        int errors = 0;
        ArrayList<Page> Pages2 = new ArrayList<>();
        for (Page p : PageRef) {

            Pages2.add(new Page(p));
        }

        Page n;

        n = Pages2.get(0);
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
        result.put("errors", errors);
        result.put("thrashingCount", thrashingCount);

        setPPF(PPF+errors);
        return  result;
    }
}