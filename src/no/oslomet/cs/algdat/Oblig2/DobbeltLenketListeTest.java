package no.oslomet.cs.algdat.Oblig2;

import static org.junit.jupiter.api.Assertions.*;

class DobbeltLenketListeTest {
    DobbeltLenketListe<String> testListe;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        testListe = new DobbeltLenketListe<>();
    }

    @org.junit.jupiter.api.Test
    void subliste() {
    }

    @org.junit.jupiter.api.Test
    void antall() {
    }

    @org.junit.jupiter.api.Test
    void tom() {
    }

    @org.junit.jupiter.api.Test
    void leggInn() {
    }

    @org.junit.jupiter.api.Test
    void leggInn1() {
        testListe.leggInn(0, "A");
        testListe.leggInn(1, "B");
        System.out.println( testListe + " antall: " + testListe.antall());
    }

    @org.junit.jupiter.api.Test
    void inneholder() {
    }

    @org.junit.jupiter.api.Test
    void hent() {
    }

    @org.junit.jupiter.api.Test
    void indeksTil() {
    }

    @org.junit.jupiter.api.Test
    void oppdater() {
    }

    @org.junit.jupiter.api.Test
    void fjern() {
        testListe.leggInn(0, "A");
        testListe.leggInn(1, "B");
        testListe.leggInn(1, "C");
        testListe.fjern("B");
        System.out.println(testListe);
    }

    @org.junit.jupiter.api.Test
    void fjern1() {
        testListe.leggInn(0, "C");
        testListe.leggInn(0, "B");
        testListe.leggInn(0, "A");
        System.out.println(testListe);
        testListe.fjern(0);
        System.out.println(testListe);
        testListe.fjern(0);
        System.out.println(testListe);
        testListe.fjern(0);
        System.out.println(testListe);
    }

    @org.junit.jupiter.api.Test
    void fjernTidtakerTest(){
        Liste<Integer> nyliste = new DobbeltLenketListe<>();

        for (int i = 1; i <= 100_000; i++) nyliste.leggInn(i);
        long tid1 = System.currentTimeMillis();
        for (int i = 40000; i <= 50000; i++) nyliste.fjern(new Integer(i));
        tid1 = System.currentTimeMillis() - tid1;
        System.out.println("fjern(T verdi) tid: " + tid1);


        for (int i = 1; i <= 100_000; i++) nyliste.leggInn(i);
        long tid2 = System.currentTimeMillis();
        for (int i = 40000; i <= 50000; i++) nyliste.fjern(i);
        tid2 = System.currentTimeMillis() - tid2;
        System.out.println("fjern(int index) tid: " +tid2);

        long maks = Math.max(tid1, tid2);
        long min = Math.min(tid1, tid2);
        System.out.println("maks: " + maks);
        System.out.println("min: " + min);
    }

    @org.junit.jupiter.api.Test
    void nullstill() {
    }

    @org.junit.jupiter.api.Test
    void testToString() {
    }

    @org.junit.jupiter.api.Test
    void omvendtString() {
    }

    @org.junit.jupiter.api.Test
    void sorter() {
    }

    @org.junit.jupiter.api.Test
    void fratilKontroll() {
    }

    @org.junit.jupiter.api.Test
    void iterator() {
    }

    @org.junit.jupiter.api.Test
    void iterator1() {
    }
}