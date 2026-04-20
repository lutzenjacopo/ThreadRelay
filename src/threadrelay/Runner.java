package threadrelay;

import java.awt.*;
import java.util.*;

public class Runner implements Subject, Runnable  {
    private int identificativo, velocita, posizione = 0;
    private Runner prossimoRunner;
    private volatile boolean inPausa, fermato;
      // ── Lista degli osservatori (sincronizzata per accesso multi-thread) ──
    private final ArrayList<Observer> observers = new ArrayList<>();
    

    public Runner(int identificativo, int velocita) {
        this.identificativo = identificativo;
        this.velocita = velocita;
    }

    public void setProssimoRunner(Runner prossimoRunner) {
        this.prossimoRunner = prossimoRunner;
    }

    @Override
    public void run() {
        for (int i = 0; i <= 99; i++) { 
            if (fermato) {
                break;
            }
            synchronized (this) {
                while (inPausa) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        System.getLogger(Runner.class.getName()).log(System.Logger.Level.ERROR, "Errore:", ex);
                    }
                }
            }
            posizione = i; 
            
            listener.aggiornaPosizione(identificativo, posizione);
            
            if (prossimoRunner != null && posizione == 90) {
                Thread t = new Thread(prossimoRunner);
                t.start(); 
            }
            
            try {
                Thread.sleep(velocita);
            } catch (InterruptedException ex) {
                System.getLogger(Runner.class.getName()).log(System.Logger.Level.ERROR, "Errore:", ex);
            }
        }
        listener.corsaFinita(identificativo);
    }

    public void sospendi() {
        inPausa = true;
    }

    public void riprendi() {
        inPausa = false;
        synchronized (this) {
            notifyAll();
        }
    }

    public void ferma() {
        fermato = true;
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public void addObserver(Observer o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void removeObserver(Observer o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void notifyObservers() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}