package com.mycompany.memorysimulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MemorySimulator extends JFrame {

    private JTextField osSizeField, numPartitionsField, numProcessesField;
    private JPanel inputPanel, memoryPanel, partitionsPanel, processesPanel;
    private JButton generatePartitionsButton, generateProcessesButton, startButton;
    private JComboBox<String> algorithmSelector;
    private JTextField[] partitionSizeFields, processSizeFields;

    private int osSize;
    private int numPartitions, numProcesses;
    private int[] partitionSizes, processSizes;
    private String[] processAssignment; // Para almacenar qué proceso ocupa qué partición

    public MemorySimulator() {
        // Configuración de la ventana principal
        setTitle("Simulador de Asignación de Memoria");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de entrada (lado izquierdo)
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(20, 1)); // Diseño en columna
        inputPanel.add(new JLabel("Tamaño del Sistema Operativo:"));
        osSizeField = new JTextField();
        inputPanel.add(osSizeField);

        inputPanel.add(new JLabel("Número de Particiones:"));
        numPartitionsField = new JTextField();
        inputPanel.add(numPartitionsField);

        // Botón para generar campos de particiones dinámicamente
        generatePartitionsButton = new JButton("Generar Particiones");
        inputPanel.add(generatePartitionsButton);

        // Panel para los campos dinámicos de las particiones
        partitionsPanel = new JPanel();
        partitionsPanel.setLayout(new GridLayout(0, 1));
        inputPanel.add(partitionsPanel);

        inputPanel.add(new JLabel("Número de Procesos:"));
        numProcessesField = new JTextField();
        inputPanel.add(numProcessesField);

        // Botón para generar campos de procesos dinámicamente
        generateProcessesButton = new JButton("Generar Procesos");
        inputPanel.add(generateProcessesButton);

        // Panel para los campos dinámicos de los procesos
        processesPanel = new JPanel();
        processesPanel.setLayout(new GridLayout(0, 1));
        inputPanel.add(processesPanel);

        inputPanel.add(new JLabel("Seleccionar Algoritmo:"));
        algorithmSelector = new JComboBox<>(new String[]{"First Fit", "Next Fit", "Best Fit", "Worst Fit"});
        inputPanel.add(algorithmSelector);

        // Botón para iniciar la simulación
        startButton = new JButton("Iniciar Simulación");
        inputPanel.add(startButton);
        
        add(inputPanel, BorderLayout.WEST);

        // Panel de visualización de memoria (lado derecho)
        memoryPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMemory(g);
            }
        };
        memoryPanel.setBackground(Color.WHITE);
        add(memoryPanel, BorderLayout.CENTER);

        // Acción del botón para generar los campos de particiones
        generatePartitionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatePartitionFields();
            }
        });

        // Acción del botón para generar los campos de procesos
        generateProcessesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateProcessFields();
            }
        });

        // Acción del botón para iniciar la simulación
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allocateMemory();
                memoryPanel.repaint();
            }
        });
    }

    // Método para generar dinámicamente los campos de entrada de las particiones
    private void generatePartitionFields() {
        partitionsPanel.removeAll();  // Limpiar panel antes de agregar nuevos campos
        try {
            numPartitions = Integer.parseInt(numPartitionsField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un número válido de particiones.");
            return;
        }

        partitionSizeFields = new JTextField[numPartitions];
        for (int i = 0; i < numPartitions; i++) {
            partitionsPanel.add(new JLabel("Tamaño de Partición " + (i + 1) + ":"));
            partitionSizeFields[i] = new JTextField();
            partitionsPanel.add(partitionSizeFields[i]);
        }

        inputPanel.revalidate();
        inputPanel.repaint();
    }

    // Método para generar dinámicamente los campos de entrada de los procesos
    private void generateProcessFields() {
        processesPanel.removeAll();  // Limpiar panel antes de agregar nuevos campos
        try {
            numProcesses = Integer.parseInt(numProcessesField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un número válido de procesos.");
            return;
        }

        processSizeFields = new JTextField[numProcesses];
        for (int i = 0; i < numProcesses; i++) {
            processesPanel.add(new JLabel("Tamaño de Proceso " + (i + 1) + ":"));
            processSizeFields[i] = new JTextField();
            processesPanel.add(processSizeFields[i]);
        }

        inputPanel.revalidate();
        inputPanel.repaint();
    }

    // Método para asignar la memoria
    private void allocateMemory() {
        try {
            osSize = Integer.parseInt(osSizeField.getText());
            partitionSizes = new int[numPartitions];
            for (int i = 0; i < numPartitions; i++) {
                partitionSizes[i] = Integer.parseInt(partitionSizeFields[i].getText());
            }

            processSizes = new int[numProcesses];
            for (int i = 0; i < numProcesses; i++) {
                processSizes[i] = Integer.parseInt(processSizeFields[i].getText());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa tamaños válidos.");
            return;
        }

        // Inicializar la asignación de procesos (ningún proceso asignado inicialmente)
        processAssignment = new String[numPartitions];
        for (int i = 0; i < numPartitions; i++) {
            processAssignment[i] = "Libre";
        }

        // Asignar procesos a particiones según el algoritmo seleccionado
        String selectedAlgorithm = (String) algorithmSelector.getSelectedItem();
        if (selectedAlgorithm.equals("First Fit")) {
            firstFit();
        } else if (selectedAlgorithm.equals("Next Fit")) {
            nextFit();
        } else if (selectedAlgorithm.equals("Best Fit")) {
            bestFit();
        } else if (selectedAlgorithm.equals("Worst Fit")) {
            worstFit();
        }
    }

    // Algoritmo First Fit
    private void firstFit() {
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numPartitions; j++) {
                if (processAssignment[j].equals("Libre") && partitionSizes[j] >= processSizes[i]) {
                    processAssignment[j] = "Proceso " + (i + 1) + " (" + processSizes[i] + ")";
                    break;
                }
            }
        }
    }

    // Algoritmo Next Fit (Ejemplo básico)
    private void nextFit() {
        int lastPosition = 0;
        for (int i = 0; i < numProcesses; i++) {
            boolean assigned = false;
            for (int j = lastPosition; j < numPartitions; j++) {
                if (processAssignment[j].equals("Libre") && partitionSizes[j] >= processSizes[i]) {
                    processAssignment[j] = "Proceso " + (i + 1) + " (" + processSizes[i] + ")";
                    lastPosition = j + 1;
                    assigned = true;
                    break;
                }
            }
            if (!assigned) lastPosition = 0; // Reiniciar si no se encontró espacio
        }
    }

    // Algoritmo Best Fit
    private void bestFit() {
        for (int i = 0; i < numProcesses; i++) {
            int bestIndex = -1;
            for (int j = 0; j < numPartitions; j++) {
                if (processAssignment[j].equals("Libre") && partitionSizes[j] >= processSizes[i]) {
                    if (bestIndex == -1 || partitionSizes[j] < partitionSizes[bestIndex]) {
                        bestIndex = j;
                    }
                }
            }
            if (bestIndex != -1) {
                processAssignment[bestIndex] = "Proceso " + (i + 1) + " (" + processSizes[i] + ")";
            }
        }
    }

    // Algoritmo Worst Fit
    private void worstFit() {
        for (int i = 0; i < numProcesses; i++) {
            int worstIndex = -1;
            for (int j = 0; j < numPartitions; j++) {
                if (processAssignment[j].equals("Libre") && partitionSizes[j] >= processSizes[i]) {
                    if (worstIndex == -1 || partitionSizes[j] > partitionSizes[worstIndex]) {
                        worstIndex = j;
                    }
                }
            }
            if (worstIndex != -1) {
                processAssignment[worstIndex] = "Proceso " + (i + 1) + " (" + processSizes[i] + ")";
            }
        }
    }

    // Método para dibujar la memoria visualmente
    private void drawMemory(Graphics g) {
    // Validación para asegurarse de que las particiones y los procesos están inicializados
        if (partitionSizes == null || processAssignment == null) {
            return; // Si no están inicializados, no hacer nada
        }

        int totalMemory = osSize;
        for (int size : partitionSizes) {
            totalMemory += size;
        }

        int panelHeight = memoryPanel.getHeight();
        int panelWidth = memoryPanel.getWidth();

        // Dibujar el sistema operativo
        int osHeight = (int) ((double) osSize / totalMemory * panelHeight);
        g.setColor(Color.GRAY);
        g.fillRect(50, 0, panelWidth - 100, osHeight);
        g.setColor(Color.BLACK);
        g.drawRect(50, 0, panelWidth - 100, osHeight);
        g.drawString("Sistema Operativo (" + osSize + ")", 60, osHeight / 2);

        // Dibujar las particiones
        int currentY = osHeight;
        for (int i = 0; i < partitionSizes.length; i++) {
            int partitionHeight = (int) ((double) partitionSizes[i] / totalMemory * panelHeight);
            g.setColor(Color.BLUE);
            g.fillRect(50, currentY, panelWidth - 100, partitionHeight);
            g.setColor(Color.BLACK);
            g.drawRect(50, currentY, panelWidth - 100, partitionHeight);
            g.drawString(processAssignment[i] + " (" + partitionSizes[i] + ")", 60, currentY + partitionHeight / 2);
            currentY += partitionHeight;
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MemorySimulator().setVisible(true);
            }
        });
    }
}
