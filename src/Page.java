import java.util.Comparator;

public class Page {
    public int nr;
    public int process;
    public int ref;
    public Page(int nr, int ref, int process)
    {
        this.nr = nr;
        this.process = process;
        this.ref = ref;
    }
    public Page(Page p)
    {
        this.nr = p.nr;
        this.process = p.process;
        this.ref = p.ref;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }

    public static Comparator<Page> refComparator = new Comparator<Page>() {
        @Override
        public int compare(Page o1, Page o2) {
            return o1.ref - o2.ref;
        }
    };

    public String toString() {
        return nr + " ";
    }
}