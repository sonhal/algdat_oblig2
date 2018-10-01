package no.oslomet.cs.algdat.Oblig2;

public class Main {

    public static void main(String[] args){
/*        Liste<String> liste = new DobbeltLenketListe<>();
        System.out.println(liste.antall() + " " + liste.tom());
        // Utskrift: 0 true

        String[] s = {"Ole", null, "Per", "Kari", null};
        Liste<String> liste2 = new DobbeltLenketListe<>(s);
        System.out.println(liste2.antall() + " " + liste2.tom());
        // Utskrift: 3 false

        String[] s1 = {}, s2 = {"A"}, s3 = {null,"A",null,"B",null};
        DobbeltLenketListe<String> l1 = new DobbeltLenketListe<>(s1);
        DobbeltLenketListe<String> l2 = new DobbeltLenketListe<>(s2);
        DobbeltLenketListe<String> l3 = new DobbeltLenketListe<>(s3);
        System.out.println(l1.toString() + " " + l2.toString()     + " " + l3.toString() + " " + l1.omvendtString() + " "     + l2.omvendtString() + " " + l3.omvendtString());
        // Utskrift: [] [A] [A, B] [] [A] [B, A]


        DobbeltLenketListe<Integer> liste3 = new DobbeltLenketListe<>();
        System.out.println(liste3.toString() + " " + liste3.omvendtString());

        for (int i = 1; i <= 3; i++){
            liste3.leggInn(i);
            System.out.println(liste3.toString() + " " + liste3.omvendtString());
        }
        // Utskrift:   // [] []   // [1] [1]   // [1, 2] [2, 1]   // [1, 2, 3] [3, 2, 1]*/

        DobbeltLenketListe<String> liste =     new DobbeltLenketListe<>(new String[]       {"Birger","Lars","Anders","Bodil","Kari","Per","Berit"});
        liste.fjernHvis(navn -> navn.charAt(0) == 'B'); // fjerner navn som starter med B
        System.out.println(liste + " " + liste.omvendtString());
        // Utskrift: [Lars, Anders, Kari, Per] [Per, Kari, Anders, Lars]


    }
}
