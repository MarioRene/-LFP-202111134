package main.java.afdgraph;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class InterfazGrafica extends JPanel {
    private JButton btnAnalizar, btnGraficar, btnGenerarReportes;
    private JComboBox<String> comboAutomas;
    private JTextArea textAreaTexto, textAreaGrafo;
    private JPanel panelGrafo;
    private Map<String, AFD> automatas;
    private AnalizadorAFD analizador;

    public InterfazGrafica() {
        automatas = new HashMap<>();
        analizador = new AnalizadorAFD();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel principal con pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Panel consola
        textAreaTexto = new JTextArea();
        textAreaTexto.setEditable(false);
        JScrollPane scrollTexto = new JScrollPane(textAreaTexto);
        scrollTexto.setBorder(BorderFactory.createTitledBorder("Consola"));
        tabbedPane.addTab("Consola", scrollTexto);

        // Panel texto del grafo
        textAreaGrafo = new JTextArea();
        textAreaGrafo.setEditable(false);
        JScrollPane scrollGrafo = new JScrollPane(textAreaGrafo);
        scrollGrafo.setBorder(BorderFactory.createTitledBorder("Texto del Autómata"));
        tabbedPane.addTab("Texto AFD", scrollGrafo);

        // Panel visualización gráfica
        panelGrafo = new JPanel(new BorderLayout());
        panelGrafo.setBackground(Color.WHITE);
        panelGrafo.setBorder(BorderFactory.createTitledBorder("Visualización Gráfica"));
        tabbedPane.addTab("Gráfico", panelGrafo);

        add(tabbedPane, BorderLayout.CENTER);

        // Panel botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAnalizar = new JButton("Analizar Archivo");
        btnGraficar = new JButton("Graficar Autómata");
        btnGenerarReportes = new JButton("Generar Reportes");
        comboAutomas = new JComboBox<>();
        comboAutomas.setPreferredSize(new Dimension(200, 25));

        panelBotones.add(btnAnalizar);
        panelBotones.add(comboAutomas);
        panelBotones.add(btnGraficar);
        panelBotones.add(btnGenerarReportes);
        
        add(panelBotones, BorderLayout.SOUTH);

        // Configurar acciones
        btnAnalizar.addActionListener(e -> analizarArchivo());
        btnGraficar.addActionListener(e -> graficarAutoma());
        btnGenerarReportes.addActionListener(e -> generarReportes());
        comboAutomas.addActionListener(e -> actualizarTextoGrafo());
    }

    private void analizarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos LFP (*.lfp)", "lfp"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            try {
                AFD afd = analizador.analizarArchivo(fileToOpen);
                
                if (afd.getNombre() == null || afd.getNombre().isEmpty()) {
                    throw new Exception("El archivo no contiene un nombre válido para el AFD");
                }
                
                automatas.put(afd.getNombre(), afd);
                
                DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
                automatas.keySet().forEach(model::addElement);
                comboAutomas.setModel(model);
                
                comboAutomas.setSelectedItem(afd.getNombre());
                
                textAreaTexto.setText("Archivo analizado exitosamente:\n" + 
                                    fileToOpen.getAbsolutePath() + "\n\n");
                textAreaTexto.append("Autómata cargado: " + afd.getNombre() + "\n");
                
                actualizarTextoGrafo();
                graficarAutoma();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error al procesar el archivo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
                textAreaTexto.setText("Error: " + ex.getMessage());
            }
        }
    }

    private void graficarAutoma() {
        String selectedAutoma = (String) comboAutomas.getSelectedItem();
        if (selectedAutoma != null && automatas.containsKey(selectedAutoma)) {
            try {
                GeneradorGrafo.graficarAFD(automatas.get(selectedAutoma), panelGrafo);
                textAreaTexto.append("\nAutómata graficado: " + selectedAutoma);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error al graficar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void generarReportes() {
        String seleccion = (String) comboAutomas.getSelectedItem();
        if (seleccion != null) {
            try {
                GeneradorReportes.generarReporteTokens(analizador.getTokens(), seleccion);
                GeneradorReportes.generarReporteErrores(analizador.getErrores(), seleccion);
                
                textAreaTexto.append("\n\nReportes generados en carpeta 'reportes'");
                JOptionPane.showMessageDialog(this, "Reportes generados exitosamente", 
                                           "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error generando reportes: " + ex.getMessage(), 
                                           "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarTextoGrafo() {
        String seleccion = (String) comboAutomas.getSelectedItem();
        textAreaGrafo.setText(seleccion != null && automatas.containsKey(seleccion) ? 
                            analizador.getTextoGrafo() : "No hay autómata seleccionado");
    }
}
