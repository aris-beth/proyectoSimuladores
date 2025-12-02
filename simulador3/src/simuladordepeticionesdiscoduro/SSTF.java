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
 *
 */
public class SSTF implements Runnable{
    
    private Peticion[] juego_de_peticiones;
    private JFrame jframe;
    
    private Peticion ultima_servida;
    
    private int siguiente = 0;
    
    public SSTF(Peticion[] juego_de_peticiones) {
        this.juego_de_peticiones = juego_de_peticiones;
    }

    SSTF(Peticion[] juego_peticiones, javax.swing.JFrame jframe) {
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
        
        Peticion p = null;
        int n = 0;
        boolean no_asignada = true;
        
        for(Peticion peticion : this.juego_de_peticiones){
        
            if(peticion.getStatus()==Peticion.CREADA){
                if(ultima_servida==null){
                    //comienza el algoritmo no se ha atendido ninguna peticion
                    // y se toma cojmo parte inicial la posicion del cabezal
                    if(no_asignada){
                        n = Math.abs( Cabezal.getPista_actual() - peticion.getPista() );
                        p = peticion;
                        no_asignada = false;
                    }else{
                        int m = Math.abs(Cabezal.getPista_actual() - peticion.getPista() );
                        if(m<=n){
                            p = peticion;
                            n = m;
                        }
                    }
                }else{
                    if(no_asignada){
                        n = Math.abs( ultima_servida.getPista() - peticion.getPista() );
                        p = peticion;
                        no_asignada = false;
                    }else{
                        int m = Math.abs( ultima_servida.getPista() - peticion.getPista() );
                        if(m<=n){
                            p = peticion;
                            n = m;
                        }
                    }
                }
            }
        }
        ultima_servida = p;
        return p;
        
        
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
            Logger.getLogger(SSTF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
