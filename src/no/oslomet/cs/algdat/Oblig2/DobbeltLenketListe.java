package no.oslomet.cs.algdat.Oblig2;

/////////// DobbeltLenketListe ////////////////////////////////////

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

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

    @Override
    public void nullstill(){
        throw new UnsupportedOperationException("Ikke laget ennå!");
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
        throw new UnsupportedOperationException("Ikke laget ennå!");
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
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }

    public Iterator<T> iterator(int indeks){
        throw new UnsupportedOperationException("Ikke laget ennå!");
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
            throw new UnsupportedOperationException("Ikke laget ennå!");
        }
        @Override
        public boolean hasNext(){
            return denne != null;  // denne koden skal ikke endres!
        }
        @Override
        public T next(){
            throw new UnsupportedOperationException("Ikke laget ennå!");
        }
        @Override
        public void remove(){
            throw new UnsupportedOperationException("Ikke laget ennå!");
        }


    } // DobbeltLenketListeIterator
} // DobbeltLenketListe
