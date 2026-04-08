package threadrelay;

public class Runner implements Runnable {
    private int identificativo, velocita, posizione = 0;
    private Runner prossimoRunner;
    private volatile boolean inPausa, fermato;
    private RunnerListener listener;

    public Runner(int identificativo, int velocita, RunnerListener listener) {
        this.identificativo = identificativo;
        this.velocita = velocita;
        this.listener = listener;
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
}