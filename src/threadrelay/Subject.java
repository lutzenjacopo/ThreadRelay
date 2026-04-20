/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package threadrelay;

/**
 *
 * @author lutzen.jacopo
 */
/**
 * Interfaccia Subject (Osservabile).
 * Gestisce la lista degli Observer e le notifiche.
 */
public interface Subject {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}
