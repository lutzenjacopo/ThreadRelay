/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package threadrelay;

/**
 *
 * @author samue
 */
public class Runner implements Runnable{
    private int identificativo=0,velocita=10,posizione=0;
    private Runner prossimoRunner;
    private volatile boolean inPausa,fermato;
    @Override
    public void run() {
    for(int i=0;i<100;i++){
        if(fermato){
            break;
        }
        synchronized(this){while(inPausa){
            try {
                wait();
            } catch (InterruptedException ex) {
                System.getLogger(Runner.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }}
        posizione++;
            if(prossimoRunner!=null&&posizione==90){
                Thread t = new Thread(prossimoRunner);
            }
            try {
                Thread.sleep(velocita);
            } catch (InterruptedException ex) {
                System.getLogger(Runner.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
    }
    }
    public void sospendi(){
        inPausa=true;
        
    }
    
    public void riprendi(){
        inPausa=false;
        synchronized(this){notifyAll();}
    }
    
    public void ferma(){
        fermato=true;
        notifyAll();
    }
}
