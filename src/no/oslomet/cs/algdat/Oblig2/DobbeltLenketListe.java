package no.oslomet.cs.algdat.Oblig2;

/////////// DobbeltLenketListe ////////////////////////////////////
//s305349, Sondre Halvorsen, IT

import java.util.*;

public class DobbeltLenketListe<T> implements Liste<T> {
    private static final class Node<T>{   // en indre
        // instansvariabler
        private T verdi;
        private Node<T> forrige, neste;

        private Node(T verdi, Node<T> forrige, Node<T> neste)  // konstruktør
        {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }
        protected Node(T verdi)  // konstruktør
        {
            this(verdi, null, null);
        }
    } // Node

    // instansvariabler
    private Node<T> hode; // peker til den første i listen
    private Node<T> hale; // peker til den siste i listen
    private int antall; // antall noder i listen
    private int endringer; // antall endringer i listen


    // hjelpemetode
    private Node<T> finnNode(int indeks){
        indeksKontroll(indeks,false);

        if(indeks < (antall / 2)){
            Node<T> current = hode;
            for(int  i = 0; i < indeks; i++){
                current = current.neste;
            }
            return current;
        }
        else {
            Node<T> current = hale;
            for(int i = antall - 1; i > indeks; i--){
                current = current.forrige;
            }
            return current;
        }
    }

    // konstruktør
    public DobbeltLenketListe(){
        hode = hale = null;
        antall = 0;
        endringer = 0;
    }

    // konstruktør
    public DobbeltLenketListe(T[] a){
        Objects.requireNonNull(a);

        // Finner første verdi i arrayet som ikke er null
        if(a.length > 0){
            int tabellPos = 0;
            for (;tabellPos < a.length; tabellPos++){
                if(a[tabellPos] != null){
                    hode = new Node<>(a[tabellPos]);
                    antall++;
                    break;
                }
            }

            hale = hode;
            if(hode != null){
                tabellPos++;
                for(;tabellPos < a.length; tabellPos++){
                    if(a[tabellPos] != null){
                        hale.neste = new Node<>(a[tabellPos], hale, null);
                        hale = hale.neste;
                        antall++;
                    }
                }
            }
        }
    }

    // subliste
    public Liste<T> subliste(int fra, int til){
        fratilKontroll(antall, fra, til);
        int antallElement = til - fra;
        if(antallElement < 1) return new DobbeltLenketListe<>();

        Node<T> current = finnNode(fra);

        DobbeltLenketListe<T> subliste = new DobbeltLenketListe<>();

        while(antallElement > 0) {
            subliste.leggInn(current.verdi);
            current = current.neste;
            antallElement--;
        }
        return subliste;
    }

    @Override
    public int antall(){
        return antall;
    }

    @Override
    public boolean tom(){
        return antall == 0;
    }

    @Override
    public boolean leggInn(T verdi){
        Objects.requireNonNull(verdi);

        if(hode == null){
            hode = new Node<>(verdi);
            hale = hode;
            antall++;
            endringer++;
            return true;
        }
        else {
            hale.neste = new Node<>(verdi, hale, null);
            hale = hale.neste;
            antall++;
            endringer++;
            return true;
        }
    }

    @Override
    public void leggInn(int indeks, T verdi){
        Objects.requireNonNull(verdi);
        indeksKontroll(indeks, true);
        if(indeks == 0 && antall == 0){
            leggInn(verdi);
        }
        else if(indeks == 0){
            hode.forrige = new Node<>(verdi, null, hode);
            hode = hode.forrige;
            antall++;
            endringer++;
        }
        else if(indeks == antall){
            hale.neste = new Node<>(verdi, hale, null);
            hale = hale.neste;
            antall++;
            endringer++;
        }
        else {
            Node<T> oldNode = finnNode(indeks);
            Node<T> newNode = new Node<>(verdi,oldNode.forrige,oldNode);
            oldNode.forrige.neste = newNode;
            oldNode.forrige = newNode;
            antall++;
            endringer++;
        }
    }

    @Override
    public boolean inneholder(T verdi){
        return indeksTil(verdi) >= 0;
    }

    @Override
    public T hent(int indeks){
        return finnNode(indeks).verdi;
    }

    @Override
    public int indeksTil(T verdi){
        Node<T> current = hode;
        int indeks = 0;
        while (current != null){
            if(current.verdi.equals(verdi)) return indeks;
            indeks++;
            current = current.neste;
        }
        return -1;
    }

    @Override
    public T oppdater(int indeks, T nyverdi){
        indeksKontroll(indeks, false);
        Objects.requireNonNull(nyverdi);

        Node<T> node = finnNode(indeks);
        T tmp = node.verdi;
        node.verdi = nyverdi;
        endringer++;
        return tmp;
    }

    @Override
    public boolean fjern(T verdi) {
        if(verdi == null) return false;

        Node<T> current = hode;
        while (current != null){
            if(verdi.equals(current.verdi)){
                antall--;
                endringer++;
                unlinkNode(current);
                return true;
            }
            current = current.neste;
        }
        return false;
    }

    @Override
    public T fjern(int indeks){
        indeksKontroll(indeks, false);
        Node<T> node;
        if(indeks < (antall / 2)){
            node = hode;
            for(int  i = 0; i < indeks; i++){
                node = node.neste;
            }
        }
        else {
            node = hale;
            for(int i = antall - 1; i > indeks; i--){
                node = node.forrige;
            }
        }
        unlinkNode(node);
        antall--;
        endringer++;
        return node.verdi;
    }

    /**
     *  Løsning 1.
     *  14 - 27 ms
     *
     *  Løsning 2.
     *  6432 ms
     */
    @Override
    public void nullstill(){
        Node<T> current = hode;
        Node<T> tmp;
        while (current != null){
            tmp = current.neste;
            current.forrige = null;
            current.verdi = null;
            current.neste = null;
            current = tmp;
        }
        antall = 0;
        endringer++;
        hode = null;
        hale = null;
    }

    @Override
    public String toString(){
        Node<T> current = hode;
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        if(current != null){
            builder.append(current.verdi);
            current = current.neste;
        }

        while(current != null){
            builder.append(',');
            builder.append(' ');
            builder.append(current.verdi);
            current = current.neste;
        }

        builder.append(']');
        return builder.toString();
    }

    public String omvendtString(){
        Node<T> current = hale;
        StringBuilder builder = new StringBuilder();
        builder.append('[');

        if(current != null){
            builder.append(current.verdi);
            current = current.forrige;
        }

        while(current != null){
            builder.append(',');
            builder.append(' ');
            builder.append(current.verdi);
            current = current.forrige;
        }
        builder.append(']');
        return builder.toString();
    }

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c){
        kvikksortering(liste, c);

    }

    private static <T> void kvikksortering0(Liste<T> liste, int v, int h, Comparator<? super T> c)  // en privat metode
    {
        if (v >= h) return;  // a[v:h] er tomt eller har maks ett element
        int k = sParter0(liste, v, h, (v + h)/2, c);  // bruker midtverdien
        kvikksortering0(liste, v, k - 1, c);     // sorterer intervallet a[v:k-1]
        kvikksortering0(liste, k + 1, h, c);     // sorterer intervallet a[k+1:h]
    }

    public static <T> void kvikksortering(Liste<T> liste, int fra, int til, Comparator<? super T> c) // a[fra:til>
    {
        fratilKontroll(liste.antall(), fra, til);  // sjekker når metoden er offentlig
        kvikksortering0(liste, fra, til - 1, c);  // v = fra, h = til - 1
    }

    public static <T> void kvikksortering(Liste<T> liste, Comparator<? super T> c)   // sorterer hele tabellen
    {
        kvikksortering0(liste, 0, liste.antall() - 1, c);
    }

    private static <T> int sParter0(Liste<T> liste, int v, int h, int indeks, Comparator<? super T> c)
    {
        bytt(liste, indeks, h);           // skilleverdi a[indeks] flyttes bakerst
        int pos = parter(liste, v, h - 1, liste.hent(h), c);  // partisjonerer a[v:h − 1]
        bytt(liste, pos, h);              // bytter for å få skilleverdien på rett plass
        return pos;                   // returnerer posisjonen til skilleverdien
    }

    private static <T> int parter(Liste<T> liste, int v, int h, T skilleverdi, Comparator<? super T> c)
    {
        while (true)                                  // stopper når v > h
        {
            while (v <= h && c.compare(liste.hent(v), skilleverdi) < 0) v++;   // h er stoppverdi for v
            while (v <= h && c.compare(liste.hent(h), skilleverdi) < 0) h--;  // v er stoppverdi for h

            if (v < h) bytt(liste,v++,h--);                 // bytter om a[v] og a[h]
            else  return v;  // a[v] er nåden første som ikke er mindre enn skilleverdi
        }
    }

    private static <T> void bytt(Liste<T> liste, int v, int h){
        T venstre = liste.hent(v);
        T høyre = liste.hent(h);
        liste.oppdater(v, høyre);
        liste.oppdater(h, venstre);
    }


    public static void fratilKontroll(int lengde, int fra, int til)
    {
        if (fra < 0)                                  // fra er negativ
            throw new IndexOutOfBoundsException
                    ("fra(" + fra + ") er negativ!");

        if (til > lengde)                          // til er utenfor tabellen
            throw new IndexOutOfBoundsException
                    ("til(" + til + ") > antall(" + lengde + ")");

        if (fra > til)                                // fra er større enn til
            throw new IllegalArgumentException
                    ("fra(" + fra + ") > til(" + til + ") - illegalt intervall!");
    }

    private void connectNodes(Node<T> left, Node<T> right){
        left.neste = right;
        right.forrige = left;
    }

    private void unlinkNode(Node<T> node){
        if(node == hode){
            if(hode.neste == null){
                hode = null;
                hale = null;
            }
            else {
                hode = hode.neste;
                hode.forrige = null;
            }
        }
        else if(node == hale){
            if(hale.forrige == null){
               hale = null;
               hode = null;
            }
            else {
                hale = hale.forrige;
                hale.neste = null;
            }
        }
        else {
            connectNodes(node.forrige, node.neste);
        }
    }

    @Override
    public Iterator<T> iterator(){
        return new DobbeltLenketListeIterator();
    }

    public Iterator<T> iterator(int indeks){
        indeksKontroll(indeks, false);
        return new DobbeltLenketListeIterator(indeks);
    }

    private class DobbeltLenketListeIterator implements Iterator<T>   {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;
        private DobbeltLenketListeIterator(){
            denne = hode;     // denne starter på den første i listen
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer     }
        }

        private DobbeltLenketListeIterator(int indeks){
            this();
            while(denne != null && indeks > 0){
                denne = denne.neste;
                indeks--;
            }
        }
        @Override
        public boolean hasNext(){
            return denne != null;  // denne koden skal ikke endres!
        }
        @Override
        public T next(){
            if(!hasNext()) throw new NoSuchElementException("Iteratoren er tom");
            if(iteratorendringer != endringer) throw new ConcurrentModificationException("Ugylding endring");

            fjernOK = true;
            T tmp = denne.verdi;
            denne = denne.neste;
            return tmp;
        }
        @Override
        public void remove(){
            if(!fjernOK) throw new IllegalStateException("Kan ikke slette");
            if(iteratorendringer != endringer) throw new ConcurrentModificationException("Kan ikke endre");

            if(antall == 1){
                hale = null;
                hode = null;
                antall = 0;
            }
            else if(denne == null){
                hale = hale.forrige;
                hale.neste = null;
            }
            else if(denne.forrige == hode){
                hode = hode.neste;
                hode.forrige = null;
            }
            else{
                Node<T> left = denne.forrige.forrige;
                Node<T> right = denne;
                connectNodes(left,right);
            }
            fjernOK = false;
            antall--;
            endringer++;
            iteratorendringer++;
        }


    } // DobbeltLenketListeIterator
} // DobbeltLenketListe
