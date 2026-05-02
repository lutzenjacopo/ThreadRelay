package threadrelay;

/**
 * Interfaccia Observer.
 * Ogni classe che vuole ricevere notifiche dal Runner deve implementarla.
 */
public interface Observer {
    /**
     * Chiamato dal Subject (Runner) ogni volta che la posizione cambia.
     * @param idRunner id del runner che notifica
     * @param posizione il nuovo valore della posizione
     */
    void update(int idRunner, int posizione);

    /**
     * Chiamato dal Subject (Runner) quando la sua corsa è terminata.
     * @param idRunner id del runner che ha finito
     */
    void corsaFinita(int idRunner);
}
