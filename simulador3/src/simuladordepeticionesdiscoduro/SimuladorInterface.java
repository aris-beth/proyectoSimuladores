/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.


falta LOOK
CLOOK
FCFS
 */
package simuladordepeticionesdiscoduro;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JOptionPane;
import static simuladordepeticionesdiscoduro.SimuladorInterface.graphics;

public class SimuladorInterface extends javax.swing.JFrame {

    
    static Peticion[] juego_peticiones;
    static Graphics graphics;
    
    Cabezal cabezal;
       
    //largo de la peticion (px)
    static final int WIDTH = 100;
    
    private int cabeza;
    //this.cabezal=cabeza;
    
    public SimuladorInterface() {
        initComponents();
        cabezal = new Cabezal(40, 10, 15);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jSeparator1 = new javax.swing.JSeparator();
        jComboBox1 = new javax.swing.JComboBox<>();
        javax.swing.JButton jButton1 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FCFS", "SSTF", "SCAN", "C-SCAN", "C-LOOK", "LOOK", " ", " " }));
        jComboBox1.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                jComboBox1ComponentAdded(evt);
            }
        });
        jComboBox1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                jComboBox1AncestorMoved(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton1.setText("COMENZAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("PETICIONES:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(80, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(956, 956, 956)
                .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(jButton1)
                .addGap(47, 47, 47)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 649, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 490, Short.MAX_VALUE)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        int n = Integer.parseInt(JOptionPane.showInputDialog(null,"Introdusca la longitud para las peticiones de disco"));
        juego_peticiones = new Peticion[n];
        for(int i = 0; i < n; i++){
            juego_peticiones[i] = generarPeticion();
        }
        
        jLabel1.setText("Peticiones: ");
        for(int i = 0; i < n; i++){
            jLabel1.setText( jLabel1.getText() + juego_peticiones[i].getPista() + " - ");
        }
        
        switch( String.valueOf(jComboBox1.getSelectedItem()) ){
            case "FCFS":
                Thread fifo = new Thread(new FCFS(juego_peticiones,this));
                fifo.start();
                break;
            case "SSTF":
                Thread sstf = new Thread(new SSTF(juego_peticiones,this));
                sstf.start();
                break;
            case "SCAN":  
                Thread scan = new Thread(new SCAN(juego_peticiones,this));
                scan.start();
                break;
            case "C-Scan":
                Thread cscan = new Thread(new CSCAN(juego_peticiones,this));
                cscan.start();
                break;
            case "CLOOK":
                Thread clook= new Thread(new CLOOKK(juego_peticiones,cabezal,false,this));
                clook.start();
                break;
            case "LOOK":
                //int cabezal2;
                Thread look= new Thread(new LOOK(juego_peticiones,cabezal,this));
                look.start();
                break;
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox1ComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jComboBox1ComponentAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ComponentAdded

    private void jComboBox1AncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jComboBox1AncestorMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1AncestorMoved

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SimuladorInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SimuladorInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SimuladorInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SimuladorInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SimuladorInterface().setVisible(true);
            }
        });
    }
    
    @Override
    public void paint (Graphics g){
        //Referenciamos el objeto graphics del frame
        super.paint(g);
        graphics = g;  
        
        pintarFondo();
        pintarPeticiones();
        
        cabezal.pintarCabezal(g);
        
    }
    
    //metodo para generar una peticion en pista aleatoria
    public  Peticion generarPeticion(){
        int npista = ThreadLocalRandom.current().nextInt(1, 30);
        return new Peticion(this.getWidth()-20, 170+(10*(npista-1)), npista);
    }
    
    //metodo para generar una peticion en pista indicada
    public  Peticion generarPeticion(int pista){
        return new Peticion(this.getWidth()-20, 170+(10*(pista-1)), pista);
    }

    //Pintar Fondo y arreglar el panel de la pista
    private void pintarFondo() {
       graphics.setColor(this.getBackground());
        graphics.fillRect(0, 170, this.getWidth(), this.getHeight());
        
        //Pintamos las 30 pistas
        for(int i = 0; i<=1024; i++){
            //pintamos la pista
            graphics.setColor(Color.blue);
            graphics.drawLine(25, 170+(10*(i)), this.getWidth(), 170+(10*i));
            
            //Solo para pintar la ultima linea
            if (i == 1024)
                continue;
            
            //pintamos el numero de pista
            graphics.setColor(Color.black);
            graphics.drawString(String.valueOf(i+1), 10, 180+(10*i) );
        }
        
        //Generamos barra cafe del brazo
        graphics.setColor(new Color(117, 75, 13));
        graphics.fillRect(45, 160, 20, this.getHeight());   
        
    }
      
    //pintar peticiones
    public void pintarPeticiones() {
        if(juego_peticiones!=null){
         for(Peticion peticion : juego_peticiones){
              if(peticion.getStatus()==Peticion.ACTIVA){
                  peticion.pintarPeticion(graphics);
                  repaint(0,0,this.getWidth(),this.getWidth());
             }
          }
        }
    }
    
     //Verificar si ya se atendieron todas las peticiones
    public boolean seHanAtendidoTodasLasPeticiones(){
        boolean band = true;
        if(juego_peticiones!=null){
            for(Peticion peticion : juego_peticiones){
            if(peticion.getStatus()==Peticion.CREADA || peticion.getStatus()==Peticion.ACTIVA ){
                band = false;
                break;
            }
        }
        }
        return band;
    }
    
    
    //dibuja la peticion
    public void dibujarPeticion(Peticion peticion){
        Color before = graphics.getColor();
        graphics.setColor(peticion.getColor());
        graphics.fillRect(peticion.getX(), peticion.getY(), WIDTH, 10);
        graphics.setColor(before);
    }
    
    
    




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    // End of variables declaration//GEN-END:variables

    private String string;

    /**
     * Get the value of string
     *
     * @return the value of string
     */
    public String getString() {
        return string;
    }

    /**
     * Set the value of string
     *
     * @param string new value of string
     */
    public void setString(String string) {
        this.string = string;
    }
    
    
    
}
