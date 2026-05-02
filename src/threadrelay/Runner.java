package threadrelay;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un corridore della staffetta.
 * Implementa Runnable per l'esecuzione su thread separato e
 * Subject per notificare gli Observer dei cambiamenti di stato.
 *
 * La catena staffetta funziona come nell'implementazione originale:
 * ogni Runner conosce il suo successore tramite setProssimoRunner()
 * e lo avvia direttamente quando raggiunge posizione 90.
 */
public class Runner implements Runnable, Subject {

    private final int identificativo;
    private int velocita;
    private int posizione = 0;
    private Runner prossimoRunner;
    private volatile boolean inPausa, fermato;
    private final ArrayList<Observer> observers;

    public Runner(int identificativo, int velocita) {
        this.identificativo = identificativo;
        this.velocita = velocita;
        this.observers = new ArrayList<>();
    }

    public void setProssimoRunner(Runner prossimoRunner) {
        this.prossimoRunner = prossimoRunner;
    }

    // ------------------------------------------------------------------ Subject

    @Override
    public synchronized void addObserver(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public synchronized void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public synchronized void notifyObservers(int posizione) {
        List<Observer> copia = new ArrayList<>(observers);
        for (Observer o : copia) {
            o.update(identificativo, posizione);
        }
    }

    @Override
    public synchronized void notifyFineCorsa() {
        List<Observer> copia = new ArrayList<>(observers);
        for (Observer o : copia) {
            o.corsaFinita(identificativo);
        }
    }

    // ------------------------------------------------------------------ Runnable

    @Override
    public void run() {
        for (int i = 0; i <= 99; i++) {

            if (fermato) break;

            synchronized (this) {
                while (inPausa) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        if (fermato) return;
                    }
                }
            }

            posizione = i;
            notifyObservers(posizione);

            // Passaggio del testimone: avvia il prossimo runner a posizione 90
            if (prossimoRunner != null && posizione == 90) {
                new Thread(prossimoRunner).start();
            }

            try {
                Thread.sleep(velocita);
            } catch (InterruptedException ex) {
                System.getLogger(Runner.class.getName())
                        .log(System.Logger.Level.ERROR, "Errore:", ex);
            }
        }

        notifyFineCorsa();
    }

    // ------------------------------------------------------------------ Controlli

    public void sospendi() {
        inPausa = true;
    }

    public synchronized void riprendi() {
        inPausa = false;
        notifyAll();
    }

    public synchronized void ferma() {
        fermato = true;
        inPausa = false;
        notifyAll();
    }
}
