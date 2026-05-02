package threadrelay;

/**
 * Interfaccia Subject (Osservabile).
 * Gestisce la registrazione degli Observer e l'invio delle notifiche.
 */
public interface Subject {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(int posizione);
    void notifyFineCorsa();
}
