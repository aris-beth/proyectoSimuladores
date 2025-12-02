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


public class SCAN implements Runnable{
    
    private Peticion[] juego_de_peticiones;
    private JFrame jframe;
    
    private boolean abajo = true;
    
    private int siguiente = 0;
    
    private Peticion ultima_servida;
    
    public SCAN(Peticion[] juego_de_peticiones) {
        this.juego_de_peticiones = juego_de_peticiones;
    }

    SCAN(Peticion[] juego_peticiones, javax.swing.JFrame jframe) {
        this.juego_de_peticiones = juego_peticiones;
        this.jframe = jframe;
    }
    
    @Override
    public void run() {
        Peticion p = null;
        while(hayaPeticionesFormadas()){
            p = getSiguiente();
            if(p!=null){
                p.setStatus(Peticion.ACTIVA);
                moverPeticiones();
            }else{
                moverPeticiones();
            }
        }
        while(hayPeticionesSiendoAtendidas()){
            moverPeticiones();
        }
    }
    
    public Peticion getSiguiente(){
        
        
        Peticion p = null;
        
        
        for(Peticion peticion : this.juego_de_peticiones){
        
            if(peticion.getStatus()==Peticion.CREADA){
                
                if(ultima_servida==null){
                    if(abajo){
                        if(hayHaciaAbajo(Cabezal.getPista_actual())){
                            //sacar la siguiente haca abajo
                            p = siguienteHaciaAbajo(Cabezal.getPista_actual());
                        }else{
                            abajo = false;
                        }
                    }else{
                        if(hayHaciaArribaDe(Cabezal.getPista_actual())){
                            //sacar la siguiente hacia arriba
                            p = masProximaHaciaArriba(Cabezal.getPista_actual());
                        }else{
                            abajo = true;
                        }
                    }
                }else{
                    if(abajo){
                        if(hayHaciaAbajo(ultima_servida.getPista())){
                            //sacar la siguiente haca abajo
                            p = siguienteHaciaAbajo(ultima_servida.getPista());
                        }else{
                            abajo = false;
                        }
                    }else{
                        if(hayHaciaArribaDe(ultima_servida.getPista())){
                            //sacar la siguiente hacia arriba
                            p = masProximaHaciaArriba(ultima_servida.getPista());
                        }else{
                            abajo = true;
                        }
                    }
                }
            }
        }
        
        ultima_servida = p;
        return p;
        
        
    }
    
    public boolean hayHaciaAbajo(int pista){
        boolean band = false;
        for(Peticion peticion : this.juego_de_peticiones){
            if(peticion.getPista() < pista){
                band = true;
                break;
            }
        }
        return band;
    }
    
    public boolean hayHaciaArribaDe(int pista){
        boolean band = false;
        for(Peticion peticion : this.juego_de_peticiones){
            if(peticion.getPista() > pista){
                band = true;
            }
        }
        return band;
    }
     
     public Peticion masProximaHaciaArriba(int pista){
        
        Peticion p = null;
        int n = 0;
        
        boolean no_asignada = true;
        
        for(Peticion peticion : this.juego_de_peticiones){
        
            if(peticion.getStatus()==Peticion.CREADA){
                
               if(peticion.getPista() < pista ){
                    if(no_asignada){
                        n = Math.abs( pista - peticion.getPista() );
                        p = peticion;
                        no_asignada = false;   
                    }else{
                        int m = Math.abs( pista  - peticion.getPista() );
                        if(m<n){
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
     
//    public Peticion siguienteHaciaArriba(int pista){
//        for(Peticion peticion : this.juego_de_peticiones){
//            if(peticion.getStatus()==Peticion.CREADA){
//                if(peticion.getPista() > pista){
//                   return peticion;
//               }   
//            }
//        }
//        return null;
//    }
    
    public Peticion siguienteHaciaAbajo(int pista){
        Peticion anterior = null;
        Peticion p = null;
        for(Peticion peticion : this.juego_de_peticiones){
            if(peticion.getStatus()==Peticion.CREADA){
                if(peticion.getPista() < pista){
                    if(anterior==null){
                        anterior = peticion;
                        p = peticion;
                    }else{
                        if(peticion.getPista() < anterior.getPista()){
                            anterior = peticion;
                            p = peticion;
                        }
                    }
               }   
            }
        }
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
            Logger.getLogger(SCAN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
