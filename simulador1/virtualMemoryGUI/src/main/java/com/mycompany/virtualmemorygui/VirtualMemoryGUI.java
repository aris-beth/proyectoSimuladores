/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.virtualmemorygui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

public class VirtualMemoryGUI extends JFrame {
    private JTextField referenceStringField;
    private JTextField framesField;
    private JComboBox<String> algorithmBox;
    private JTextArea outputArea;
    private JButton runButton;

    private ArrayList<Integer> referenceString;
    private int numFrames;
    private int[] frames;
    private int pageFaults = 0;
    private int hits = 0;

    public VirtualMemoryGUI() {
        setTitle("Simulador de Gestión de Memoria Virtual");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de configuración (Entrada del usuario)
        JPanel configPanel = new JPanel();
        configPanel.setLayout(new GridLayout(4, 2));

        JLabel referenceStringLabel = new JLabel("Cadena de referencia:");
        referenceStringField = new JTextField();

        JLabel framesLabel = new JLabel("Número de marcos:");
        framesField = new JTextField();

        JLabel algorithmLabel = new JLabel("Algoritmo:");
        String[] algorithms = {"FIFO", "LRU"};
        algorithmBox = new JComboBox<>(algorithms);

        configPanel.add(referenceStringLabel);
        configPanel.add(referenceStringField);
        configPanel.add(framesLabel);
        configPanel.add(framesField);
        configPanel.add(algorithmLabel);
        configPanel.add(algorithmBox);

        add(configPanel, BorderLayout.NORTH);

        // Área de salida (mostrar los resultados)
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Botón de ejecución
        runButton = new JButton("Ejecutar");
        add(runButton, BorderLayout.SOUTH);

        // Acción del botón
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeSimulation();
            }
        });
    }

    private void executeSimulation() {
        // Resetear contadores
        pageFaults = 0;
        hits = 0;

        // Obtener la cadena de referencia
        referenceString = new ArrayList<>();
        String[] inputString = referenceStringField.getText().split(",");
        for (String s : inputString) {
            referenceString.add(Integer.parseInt(s.trim()));
        }

        // Obtener el número de marcos
        numFrames = Integer.parseInt(framesField.getText());
        frames = new int[numFrames];
        for (int i = 0; i < numFrames; i++) {
            frames[i] = -1;  // Inicializar marcos vacíos
        }

        // Limpiar el área de salida
        outputArea.setText("");

        // Determinar qué algoritmo usar
        String selectedAlgorithm = (String) algorithmBox.getSelectedItem();
        if (selectedAlgorithm.equals("FIFO")) {
            simulateFIFO();
        } else if (selectedAlgorithm.equals("LRU")) {
            simulateLRU();
        }

        // Mostrar estadísticas finales
        int totalReferences = referenceString.size();
        double pageFaultRatio = (double) pageFaults / totalReferences;
        double hitRatio = (double) hits / totalReferences;

        outputArea.append("\nPage faults: " + pageFaults);
        outputArea.append("\nPage fault Ratio: " + pageFaultRatio);
        outputArea.append("\nHits: " + hits);
        outputArea.append("\nHit Ratio: " + hitRatio);
    }

    public void simulateFIFO() {
        int index = 0;
        outputArea.append("Incoming \t Pages\n");
        for (int page : referenceString) {
            if (!isPageInFrame(page)) {
                frames[index] = page;
                index = (index + 1) % numFrames;
                pageFaults++;
                outputArea.append(page + "\t\t" + displayFrames() + "\n");
            } else {
                hits++;
                outputArea.append(page + "\t\t" + displayFrames() + " (hit)\n");
            }
        }
    }

    public void simulateLRU() {
        LinkedList<Integer> lruList = new LinkedList<>();
        outputArea.append("Incoming \t Pages\n");
        for (int page : referenceString) {
            if (!isPageInFrame(page)) {
                if (lruList.size() < numFrames) {
                    lruList.add(page);  // Agregar la nueva página si hay espacio
                    frames[lruList.size() - 1] = page;
                } else {
                    int lruPage = lruList.removeFirst();  // Eliminar la menos recientemente usada
                    replacePage(lruPage, page);
                    lruList.add(page);  // Agregar la nueva página
                }
                pageFaults++;
                outputArea.append(page + "\t\t" + displayFrames() + "\n");
            } else {
                hits++;
                lruList.remove((Integer) page);  // Actualizar LRU
                lruList.add(page);
                outputArea.append(page + "\t\t" + displayFrames() + " (hit)\n");
            }
        }
    }

    public boolean isPageInFrame(int page) {
        for (int frame : frames) {
            if (frame == page) {
                return true;
            }
        }
        return false;
    }

    public void replacePage(int oldPage, int newPage) {
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] == oldPage) {
                frames[i] = newPage;
                break;
            }
        }
    }

    public String displayFrames() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] != -1) {
                sb.append(frames[i]);
            } else {
                sb.append(" ");
            }
            if (i < frames.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VirtualMemoryGUI().setVisible(true);
            }
        });
    }
}
