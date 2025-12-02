/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladordepeticionesdiscoduro;

import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author elias
 */
public class FCFS implements Runnable{
    
    private Peticion[] juego_de_peticiones;
    private JFrame jframe;
    
    private int siguiente = 0;
    
    public FCFS(Peticion[] juego_de_peticiones) {
        this.juego_de_peticiones = juego_de_peticiones;
    }

    FCFS(Peticion[] juego_peticiones, javax.swing.JFrame jframe) {
        this.juego_de_peticiones = juego_peticiones;
        this.jframe = jframe;
    }
    
    @Override
    public void run() {
        while(hayaPeticionesFormadas()){
            Peticion p = getSiguiente();
            p.setStatus(Peticion.ACTIVA);
            moverPeticiones();
        }
        while(hayPeticionesSiendoAtendidas()){
            moverPeticiones();
        }
    }
    
    public Peticion getSiguiente(){
        siguiente+=1;
        return juego_de_peticiones[siguiente-1];
    }
    
    public boolean hayaPeticionesFormadas(){
        boolean band = false;
        for(Peticion peticion : this.juego_de_peticiones){
            if(peticion.getStatus()==Peticion.CREADA){
                band = true;
                break;
            }
        }
        return band;
    }
    
    
    private boolean hayPeticionesSiendoAtendidas() {
        boolean band = false;
        for(Peticion peticion : this.juego_de_peticiones){
            if(peticion.getStatus()==Peticion.ACTIVA){
                band = true;
                break;
            }
        }
        return band;
    }

    private void moverPeticiones() {
        for (Peticion peticion : juego_de_peticiones) {
            if(peticion.getStatus()==Peticion.ACTIVA){
                
                if(peticion.getX()<=80){
                    Cabezal.moverAPista(peticion.getPista());
                    peticion.setX( ( peticion.getX() - 30  ) );
                    jframe.repaint(0,0,jframe.getWidth(),jframe.getHeight());   
                    peticion.setStatus(Peticion.TERMINADA);
                }else{
                    peticion.setX( ( peticion.getX() - 30  ) );
                }
                jframe.repaint(0,0,jframe.getWidth(),jframe.getHeight());   
            }
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(FCFS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
