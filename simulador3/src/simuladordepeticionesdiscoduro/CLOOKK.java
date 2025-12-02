/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simuladordepeticionesdiscoduro;
//package Peticion;

/**
 *
 * 
 */
import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class CLOOKK implements Runnable {
    public Peticion[] peticiones; // Arreglo de peticiones
    //private int cabezal; // Pista actual del cabezal
    private boolean direccionAscendente; // Dirección del movimiento
    private int delay = 100; // Tiempo entre movimientos (ms)
    private JFrame jframe; // Ventana para visualización
    public Cabezal cabeza;
    private Peticion peticion; 
    // Constructor principal
    public CLOOKK(Peticion[] peticiones, Cabezal cabeza, boolean direccionAscendente, JFrame jframe) {
        this.peticiones = peticiones;
        this.cabeza = cabeza;
        this.direccionAscendente = direccionAscendente;
        this.delay = delay;
        this.jframe = jframe;
        this.peticion=peticion;
    }

    @Override
    public void run() {
        //Cabezal cabeza;
        
        while (true) {
            Peticion siguiente = encontrarSiguiente();

            if (siguiente == null) {
                System.out.println("No quedan peticiones por atender. Simulación completada.");
                break;
            }else{
                //HayPeticionesSiendoAtendidas();
                quedanPeticiones();
            }

            siguiente.setStatus(Peticion.ACTIVA);
            moverCabezalHacia(siguiente);

            siguiente.setStatus(Peticion.TERMINADA);
            cabeza.getPista_actual();
            
            //System.out.println("Atendida la pista: " + siguiente.getPista());
        }
    }

    private Peticion encontrarSiguiente() {
        
        Peticion objetivo = null;
        int distanciaMinima = Peticion.ACTIVA;

        for (Peticion peticion : peticiones) {
            if (peticion.getStatus() == Peticion.CREADA) {
                int distancia = peticion.getX() - cabeza.getPista_actual();
                if(distancia <=80)
                if ((direccionAscendente && distancia >= 0) || (!direccionAscendente && distancia < 0)) {
                    if (Math.abs(distancia) < distanciaMinima) {
                        distanciaMinima = Math.abs(distancia);
                        objetivo = peticion;
                    }
                }
            }
        }

        // Si no hay peticiones en la dirección actual, cambiar dirección
        if (objetivo == null) {
            direccionAscendente = !direccionAscendente;
        }

        return objetivo;
    }

    private void moverCabezalHacia(Peticion peticion) {
        Cabezal cabezal = null;
        Graphics g =null;
        while (Math.abs(peticion.getX() - cabeza.getPista_actual()) <  80) {
            if (peticion.getX() < cabeza.getPista_actual() || peticion.getX()>cabeza.getPista_actual()) {
                cabeza.moverAPista(peticion.getPista());
                peticion.getX();
                peticion.pintarPeticion(g);
                peticion.setColor(Color.yellow);
                this.delay += 1;
                 jframe.repaint(0,0,jframe.getWidth(),jframe.getHeight()); // Redibujar la interfaz gráfica
            } else {
                //cabezal -= 30;
                cabeza.moverAPista(peticion.getPista());
                peticion.getY();
                peticion.pintarPeticion(g);
                cabeza.pintarCabezal(g);
                 jframe.repaint(0,0,jframe.getWidth(),jframe.getHeight()); // Redibujar la interfaz gráfica
                 peticion.setStatus(Peticion.TERMINADA);
            }

            if (jframe != null) {
                jframe.repaint(0,0,jframe.getWidth(),jframe.getHeight()); // Redibujar la interfaz gráfica
            }

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Logger.getLogger(CLOOKK.class.getName()).log(Level.SEVERE, null, e);
                return;
            }
        }
    }

    private boolean quedanPeticiones() {
        for (Peticion peticion : peticiones) {
            if (peticion.getStatus() == Peticion.CREADA) {
                return true;
            }
        }
        return false;
    }
    
      private boolean hayPeticionesSiendoAtendidas() {
        boolean band = false;
        for(Peticion peticion : this.peticiones){
            if(peticion.getStatus()==Peticion.ACTIVA){
                band = true;
                break;
            }
        }
        return band;
    }
      
      
}