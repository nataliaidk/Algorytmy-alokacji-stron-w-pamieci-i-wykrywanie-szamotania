import java.util.Scanner;

public class Main {
    static Scanner scan;
    public static void main (String [] args)
    {
        System.out.println("Do you wanna run tests?");
        scan=new Scanner(System.in);
        String line=scan.nextLine();
        if (line.equals("yes")){
            for (int i = 1; i < 20; i++){
                System.out.println("--------------------------------------------------------------------------------------");
                System.out.println("frame size: " + i*100 + " pages: " + i*100 + " time interval: " + i*30+ " processes: " + i*10);
                System.out.println("--------------------------------------------------------------------------------------");
                Algorythms a = new Algorythms(i*10000, i*1000, i*3, i*7);

                System.out.println("Equal: " + a.EQUAL());
                int isThrashing = Algorythms.thrashingCount;
                if (isThrashing > 0) {
                    System.out.println("Szamotanie wystąpiło");
                    System.out.println("Liczba wystąpień szamotania: " + isThrashing);
                }
                System.out.println("--------------------------------------------------------------------------------------");
                System.out.println("Proportional: " + a.PROPORTIONAL());
                 isThrashing = Algorythms.thrashingCount;
                if (isThrashing > 0) {
                    System.out.println("Szamotanie wystąpiło");
                    System.out.println("Liczba wystąpień szamotania: " + isThrashing);
                }
                System.out.println("--------------------------------------------------------------------------------------");
                System.out.println("Zone Model: " + a.WSS(30));
                isThrashing = Algorythms.thrashingCount;
                if (isThrashing > 0) {
                    System.out.println("Szamotanie wystąpiło");
                    System.out.println("Liczba wystąpień szamotania: " + isThrashing);
                }
                System.out.println("--------------------------------------------------------------------------------------");
                System.out.println("Steering PF: " + a.PPF());
                isThrashing =Algorythms.thrashingCount;
                if (isThrashing > 0) {
                    System.out.println("Szamotanie wystąpiło");
                    System.out.println("Liczba wystąpień szamotania: " + isThrashing);
                }
                System.out.println("--------------------------------------------------------------------------------------");
            }
        }
        else {
            scan=new Scanner(System.in);
            System.out.println("Put in frame size");
            int framesize= Integer.parseInt(scan.nextLine());
            System.out.println("Put in amount of pages");
            int pagenr= Integer.parseInt(scan.nextLine());
            System.out.println("Put in interval");
            int inte= Integer.parseInt(scan.nextLine());
            System.out.println("Put in amount of processes");
            int proc= Integer.parseInt(scan.nextLine());

            Algorythms a = new Algorythms(framesize, pagenr, inte, proc);

            System.out.println("Equal: " + a.EQUAL());
            int isThrashing = Algorythms.thrashingCount;
            if (isThrashing > 0) {
                System.out.println("Szamotanie wystąpiło");
                System.out.println("Liczba wystąpień szamotania: " + isThrashing);
            }
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println("Proportional: " + a.PROPORTIONAL());
            isThrashing = Algorythms.thrashingCount;
            if (isThrashing > 0) {
                System.out.println("Szamotanie wystąpiło");
                System.out.println("Liczba wystąpień szamotania: " + isThrashing);
            }
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println("Zone Model: " + a.WSS(30));
            isThrashing = Algorythms.thrashingCount;
            if (isThrashing > 0) {
                System.out.println("Szamotanie wystąpiło");
                System.out.println("Liczba wystąpień szamotania: " + isThrashing);
            }
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println("Steering PF: " + a.PPF());
            isThrashing =Algorythms.thrashingCount;
            if (isThrashing > 0) {
                System.out.println("Szamotanie wystąpiło");
                System.out.println("Liczba wystąpień szamotania: " + isThrashing);
            }
            System.out.println("--------------------------------------------------------------------------------------");
        }
    }

}

