package com.mycompany.fileallocationsimulator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FileAllocationSimulator extends JFrame {
    private int diskSize = 100;
    private int[] disk = new int[diskSize];
    private JTextArea inputArea;
    private JTextPane outputArea;
    private JTable directoryTable;
    private DefaultTableModel directoryModel;
    private JTable freeBlocksTable;
    private DefaultTableModel freeBlocksModel;

    public FileAllocationSimulator() {
        setTitle("File Allocation Simulator");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createMenu();
        setupUI();
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Select Allocation Method");
        menuBar.add(menu);

        JMenuItem contiguous = new JMenuItem("Contiguous");
        JMenuItem linked = new JMenuItem("Linked");
        JMenuItem indexed = new JMenuItem("Indexed");
        JMenuItem fat = new JMenuItem("FAT");
        JMenuItem inode = new JMenuItem("I-node");

        contiguous.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulateContiguous();
            }
        });

        linked.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulateLinked();
            }
        });

        indexed.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulateIndexed();
            }
        });

        fat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulateFAT();
            }
        });

        inode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulateINode();
            }
        });

        menu.add(contiguous);
        menu.add(linked);
        menu.add(indexed);
        menu.add(fat);
        menu.add(inode);

        setJMenuBar(menuBar);
    }

    private void setupUI() {
        inputArea = new JTextArea(5, 20);
        outputArea = new JTextPane();
        outputArea.setEditable(false);
        outputArea.setContentType("text/html");

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter file sizes separated by commas:"));
        inputPanel.add(inputArea);
        panel.add(inputPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Directory Table
        String[] directoryColumns = {"File Name", "Length", "Start Block"};
        directoryModel = new DefaultTableModel(directoryColumns, 0);
        directoryTable = new JTable(directoryModel);

        // Free Blocks Table
        String[] freeBlocksColumns = {"Start Block", "Number of Blocks"};
        freeBlocksModel = new DefaultTableModel(freeBlocksColumns, 0);
        freeBlocksTable = new JTable(freeBlocksModel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(directoryTable), new JScrollPane(freeBlocksTable));
        splitPane.setResizeWeight(0.5);
        panel.add(splitPane, BorderLayout.SOUTH);

        add(panel);
    }

    private void simulateContiguous() {
        String input = inputArea.getText();
        int[] fileSizes = parseInput(input);
        int freeSpace = diskSize;
        resetDisk();
        directoryModel.setRowCount(0);
        freeBlocksModel.setRowCount(0);

        int startBlock = 0;
        for (int size : fileSizes) {
            if (size <= freeSpace) {
                freeSpace -= size;
                for (int i = 0; i < size; i++) {
                    disk[startBlock + i] = 1;  // Marcar bloques como ocupados
                }
                directoryModel.addRow(new Object[]{"File" + (directoryModel.getRowCount() + 1), size, startBlock});
                startBlock += size;
            } else {
                outputArea.setText("<html><body>No hay espacio suficiente para un archivo de tamaño " + size + "</body></html>");
                return;
            }
        }
        updateFreeBlocksTable();
        drawAllocation();
    }

    private void simulateLinked() {
        String input = inputArea.getText();
        int[] fileSizes = parseInput(input);
        int freeSpace = diskSize;
        resetDisk();
        directoryModel.setRowCount(0);
        freeBlocksModel.setRowCount(0);

        for (int size : fileSizes) {
            if (size <= freeSpace) {
                freeSpace -= size;
                int start = diskSize - freeSpace;
                int next = start + 1;
                for (int i = 0; i < size; i++) {
                    if (i == size - 1) {
                        disk[start + i] = -1;  // Último bloque, sin siguiente
                    } else {
                        disk[start + i] = next++;  // Bloque apunta al siguiente
                    }
                }
                directoryModel.addRow(new Object[]{"File" + (directoryModel.getRowCount() + 1), size, start});
            } else {
                outputArea.setText("<html><body>No hay espacio suficiente para un archivo de tamaño " + size + "</body></html>");
                return;
            }
        }
        updateFreeBlocksTable();
        drawLinkedAllocation();
    }

    private void simulateIndexed() {
        String input = inputArea.getText();
        int[] fileSizes = parseInput(input);
        int freeSpace = diskSize;
        resetDisk();
        directoryModel.setRowCount(0);
        freeBlocksModel.setRowCount(0);

        for (int size : fileSizes) {
            if (size <= freeSpace) {
                freeSpace -= size;
                int indexBlock = diskSize - freeSpace;
                disk[indexBlock] = 1;  // Marcar bloque de índice como ocupado
                for (int i = 1; i <= size; i++) {
                    disk[indexBlock + i] = 1;  // Marcar bloques de datos como ocupados
                }
                directoryModel.addRow(new Object[]{"File" + (directoryModel.getRowCount() + 1), size, indexBlock});
            } else {
                outputArea.setText("<html><body>No hay espacio suficiente para un archivo de tamaño " + size + "</body></html>");
                return;
            }
        }
        updateFreeBlocksTable();
        drawAllocation();
    }

    private void simulateFAT() {
        String input = inputArea.getText();
        int[] fileSizes = parseInput(input);
        int freeSpace = diskSize;
        resetDisk();
        int[] fat = new int[diskSize];
        for (int i = 0; i < fat.length; i++) fat[i] = -1;
        directoryModel.setRowCount(0);
        freeBlocksModel.setRowCount(0);

        for (int size : fileSizes) {
            if (size <= freeSpace) {
                freeSpace -= size;
                int start = diskSize - freeSpace;
                int prev = start;
                for (int i = 1; i < size; i++) {
                    fat[prev] = start + i;
                    prev = start + i;
                }
                fat[prev] = -1;  // Último bloque, sin siguiente
                directoryModel.addRow(new Object[]{"File" + (directoryModel.getRowCount() + 1), size, start});
            } else {
                outputArea.setText("<html><body>No hay espacio suficiente para un archivo de tamaño " + size + "</body></html>");
                return;
            }
        }
        updateFreeBlocksTable();
        drawFATAllocation(fat);
    }

    private void simulateINode() {
        String input = inputArea.getText();
        int[] fileSizes = parseInput(input);
        int freeSpace = diskSize;
        resetDisk();
        int[][] inodes = new int[diskSize][];
        int inodeIndex = 0;
        directoryModel.setRowCount(0);
        freeBlocksModel.setRowCount(0);

        for (int size : fileSizes) {
            if (size <= freeSpace) {
                freeSpace -= size;
                inodes[inodeIndex] = new int[size];
                for (int i = 0; i < size; i++) {
                    inodes[inodeIndex][i] = diskSize - freeSpace + i;
                    disk[diskSize - freeSpace + i] = 1;  // Marcar bloques como ocupados
                }
                directoryModel.addRow(new Object[]{"File" + (directoryModel.getRowCount() + 1), size, inodeIndex});
                inodeIndex++;
            } else {
                outputArea.setText("<html><body>No hay espacio suficiente para un archivo de tamaño " + size + "</body></html>");
                return;
            }
        }
        updateFreeBlocksTable();
        drawINodeAllocation(inodes);
    }

    private int[] parseInput(String input) {
        String[] parts = input.split(",");
        int[] sizes = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            sizes[i] = Integer.parseInt(parts[i].trim());
        }
        return sizes;
    }

    private void resetDisk() {
        for (int i = 0; i < diskSize; i++) {
            disk[i] = 0;
        }
    }

    private void updateFreeBlocksTable() {
        freeBlocksModel.setRowCount(0);
        int start = -1;
        int count = 0;
        for (int i = 0; i < diskSize; i++) {
            if (disk[i] == 0) {
                if (start == -1) start = i;
                count++;
            } else {
                if (start != -1) {
                    freeBlocksModel.addRow(new Object[]{start, count});
                    start = -1;
                    count = 0;
                }
            }
        }
        if (start != -1) {
            freeBlocksModel.addRow(new Object[]{start, count});
        }
    }

    private void drawAllocation() {
        StringBuilder sb = new StringBuilder("<html><body><table border='1' cellpadding='15'><tr>");
        for (int i = 0; i < diskSize; i++) {
            sb.append("<td style='background-color:" + (disk[i] == 1 ? "blue" : "white") + "'>")
                .append(disk[i] == 1 ? "■" : "□")
                .append("</td>");
            if ((i + 1) % 10 == 0) sb.append("</tr><tr>");
        }
        sb.append("</tr></table></body></html>");
        outputArea.setText(sb.toString());
    }

    private void drawLinkedAllocation() {
        StringBuilder sb = new StringBuilder("<html><body><table border='1' cellpadding='15'><tr>");
        for (int i = 0; i < diskSize; i++) {
            sb.append("<td style='background-color:" + (disk[i] == -1 ? "red" : (disk[i] > 0 ? "yellow" : "white")) + "'>")
                .append(disk[i] == -1 ? "X" : (disk[i] > 0 ? "→" : "□"))
                .append("</td>");
            if ((i + 1) % 10 == 0) sb.append("</tr><tr>");
        }
        sb.append("</tr></table></body></html>");
        outputArea.setText(sb.toString());
    }

    private void drawFATAllocation(int[] fat) {
        StringBuilder sb = new StringBuilder("<html><body><table border='1' cellpadding='15'><tr>");
        for (int i = 0; i < fat.length; i++) {
            sb.append("<td style='background-color:" + (fat[i] == -1 ? "white" : "green") + "'>")
                .append(fat[i] == -1 ? "□" : "→")
                .append("</td>");
            if ((i + 1) % 10 == 0) sb.append("</tr><tr>");
        }
        sb.append("</tr></table></body></html>");
        outputArea.setText(sb.toString());
    }

    private void drawINodeAllocation(int[][] inodes) {
        StringBuilder sb = new StringBuilder("<html><body><table border='1' cellpadding='15'><tr>");
        for (int i = 0; i < inodes.length; i++) {
            if (inodes[i] != null) {
                sb.append("<td style='background-color:purple'>");
                for (int j : inodes[i]) {
                    sb.append("■");
                }
                sb.append("</td>");
            } else {
                sb.append("<td style='background-color:white'>□</td>");
            }
            if ((i + 1) % 10 == 0) sb.append("</tr><tr>");
        }
        sb.append("</tr></table></body></html>");
        outputArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FileAllocationSimulator().setVisible(true);
            }
        });
    }
}
