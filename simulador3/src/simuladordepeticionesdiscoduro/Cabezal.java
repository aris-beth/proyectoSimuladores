/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladordepeticionesdiscoduro;

import java.awt.Color;
import java.awt.Graphics;
import static simuladordepeticionesdiscoduro.SimuladorInterface.graphics;

/**
 *
 *
 *///manejo de cabeza principal donde va almacenar las peticiones en cabezal 
public class Cabezal {
    
    private static int x;//linea vertical 
    private static int y;//linea horinzontal
    
    public static int pista_actual;
    public static int pistaobjetivo;
    public static int velocidad=1;
    
    public Cabezal(int x, int y, int pista) {
        this.x = x;
        this.y = y;
        this.pista_actual = pista;
        this.pistaobjetivo=pista;
        moverAPista(pista);
    }
    
    public static void moverAPista(int p){
        pista_actual = p;
      
        y = 160+(10*p);
    }
    
    public static void pintarCabezal(Graphics g){
        graphics.setColor(Color.red);
        graphics.fillRect(x, y, 35, 10);//movimiento del cabezal -rectangilo
        if(pista_actual != pistaobjetivo){
            if(pista_actual<pistaobjetivo){
                //pista_actual++;
                pista_actual+=velocidad;
            }
            else if(pista_actual>pistaobjetivo)
            {
                pista_actual--;
            }
             y=160+(10*pista_actual);
        }
    }

    public static int getPista_actual() {
        return pista_actual;
    }


    
    
}
