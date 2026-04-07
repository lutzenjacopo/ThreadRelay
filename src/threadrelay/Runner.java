package threadrelay;

public class Runner implements Runnable {
    private int identificativo, velocita, posizione = 0;
    private Runner prossimoRunner;
    private volatile boolean inPausa, fermato;
    private RunnerListener listener;

    // 1. Aggiunto il Costruttore
    public Runner(int identificativo, int velocita, RunnerListener listener) {
        this.identificativo = identificativo;
        this.velocita = velocita;
        this.listener = listener;
    }

    // 2. Aggiunto il Setter per il prossimo runner
    public void setProssimoRunner(Runner prossimoRunner) {
        this.prossimoRunner = prossimoRunner;
    }

    @Override
    public void run() {
        for (int i = 0; i <= 99; i++) { // Modificato da < 100 a <= 99 per arrivare esattamente a 99
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
            posizione = i; // Aggiorniamo la posizione con i
            
            // 3. Corretto mioId con identificativo
            listener.aggiornaPosizione(identificativo, posizione);
            
            if (prossimoRunner != null && posizione == 90) {
                Thread t = new Thread(prossimoRunner);
                t.start(); // 4. Mancava lo start()!
            }
            
            try {
                Thread.sleep(velocita);
            } catch (InterruptedException ex) {
                System.getLogger(Runner.class.getName()).log(System.Logger.Level.ERROR, "Errore:", ex);
            }
        }
        // 5. Corretto mioId con identificativo
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
        // 6. notifyAll() DEVE stare in un blocco synchronized
        synchronized (this) {
            notifyAll();
        }
    }
}