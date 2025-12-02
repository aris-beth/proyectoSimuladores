/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simuladordepeticionesdiscoduro;

/**
 *
 * @author crish
 */
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Color;
import java.awt.Graphics;
import static simuladordepeticionesdiscoduro.SimuladorInterface.graphics;
import javax.swing.JFrame;

public class LOOK implements Runnable {

    private Peticion[] peticiones;   // Arreglo de peticiones
    private Cabezal cabezal;
    private int direccion;  // 1 para hacia la derecha, -1 para hacia la izquierda
    private int numPeticiones;
    

    public LOOK(Peticion[] peticiones, Cabezal cabezal,JFrame jframe) {
        this.peticiones = peticiones;
        this.cabezal = cabezal;
        this.numPeticiones = numPeticiones;
        this.direccion = direccion;
    }

    @Override
    public void run() {
        // Ordenamos las peticiones por pista
        ordenarPeticiones();
        
        // Mover el cabezal según la dirección inicial
        while (!todasLasPeticionesProcesadas()) {
            // Filtrar las peticiones según la dirección
            for (int i = 0; i < numPeticiones; i++) {
                if (peticiones[i].getStatus() == Peticion.CREADA) {
                    if ((direccion == 1 && peticiones[i].getPista() >= cabezal.getPista_actual()) ||
                        (direccion == -1 && peticiones[i].getPista() <= cabezal.getPista_actual())) {
                        // Mover el cabezal hacia la pista de la petición
                        cabezal.moverAPista(peticiones[i].getPista());
                        peticiones[i].setStatus(Peticion.ACTIVA);

                        // Simular el procesamiento de la petición
                        try {
                            Thread.sleep(100);  // Simulamos el tiempo que toma procesar la petición
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Marcar la petición como terminada
                        peticiones[i].setStatus(Peticion.TERMINADA);
                    }
                }
            }

            // Si hemos procesado todas las peticiones en la dirección actual, invertimos la dirección
            if (direccion == 1) {
                direccion = -1;
            } else {
                direccion = 1;
            }
        }
    }

    // Método para ordenar las peticiones por pista (de menor a mayor)
    private void ordenarPeticiones() {
        for (int i = 0; i < numPeticiones - 1; i++) {
            for (int j = i + 1; j < numPeticiones; j++) {
                if (peticiones[i].getPista() > peticiones[j].getPista()) {
                    // Intercambiar las peticiones
                    Peticion temp = peticiones[i];
                    peticiones[i] = peticiones[j];
                    peticiones[j] = temp;
                }
            }
        }
    }

    // Método para verificar si todas las peticiones han sido procesadas
    private boolean todasLasPeticionesProcesadas() {
        for (int i = 0; i < numPeticiones; i++) {
            if (peticiones[i].getStatus() != Peticion.TERMINADA) {
                return false;
            }
        }
        return true;
    }
}