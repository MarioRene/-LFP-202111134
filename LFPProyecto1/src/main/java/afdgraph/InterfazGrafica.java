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
    private JButton btnAnalizar;
    private JButton btnGraficar;
    private JButton btnGenerarReportes;
    private JButton btnValidar;
    private JComboBox<String> comboAutomas;
    private JTextArea textAreaTexto;
    private JPanel panelGrafo;
    private JPanel panelAutomata;
    private JTextField txtCadena;
    private Map<String, AFD> automatas;

    public InterfazGrafica() {
        automatas = new HashMap<>();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel principal con pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Panel para texto de salida
        textAreaTexto = new JTextArea();
        textAreaTexto.setEditable(false);
        JScrollPane scrollTexto = new JScrollPane(textAreaTexto);
        scrollTexto.setPreferredSize(new Dimension(300, 400));
        scrollTexto.setBorder(BorderFactory.createTitledBorder("Salida"));
        tabbedPane.addTab("Consola", scrollTexto);

        // Panel para visualización del grafo
        panelGrafo = new JPanel(new BorderLayout());
        panelGrafo.setBackground(Color.WHITE);
        panelGrafo.setBorder(BorderFactory.createTitledBorder("Visualización del Autómata"));
        tabbedPane.addTab("Gráfico", panelGrafo);

        // Panel para visualización del autómata
        panelAutomata = new JPanel(new BorderLayout());
        panelAutomata.setBackground(Color.WHITE);
        panelAutomata.setBorder(BorderFactory.createTitledBorder("Autómata Seleccionado"));
        tabbedPane.addTab("Autómata", panelAutomata);

        add(tabbedPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.setBorder(BorderFactory.createEtchedBorder());
        
        btnAnalizar = new JButton("Analizar Archivo");
        btnGraficar = new JButton("Graficar Autómata");
        btnGenerarReportes = new JButton("Generar Reportes");
        comboAutomas = new JComboBox<>();
        comboAutomas.setPreferredSize(new Dimension(200, 25));
        
        // Panel de validación
        JPanel panelValidacion = new JPanel(new FlowLayout());
        txtCadena = new JTextField(20);
        btnValidar = new JButton("Validar Cadena");
        panelValidacion.add(new JLabel("Cadena a validar:"));
        panelValidacion.add(txtCadena);
        panelValidacion.add(btnValidar);

        panelBotones.add(btnAnalizar);
        panelBotones.add(comboAutomas);
        panelBotones.add(btnGraficar);
        panelBotones.add(btnGenerarReportes);
        panelBotones.add(panelValidacion);
        
        add(panelBotones, BorderLayout.SOUTH);

        // Configurar acciones
        configurarAcciones();
    }

    private void configurarAcciones() {
        btnAnalizar.addActionListener(e -> analizarArchivo());
        btnGraficar.addActionListener(e -> graficarAutoma());
        btnGenerarReportes.addActionListener(e -> generarReportes());
        btnValidar.addActionListener(e -> validarCadena());
    }

    private void mostrarAutomataEnPanel(AFD afd) {
        panelAutomata.removeAll();
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        infoPanel.add(crearEtiqueta("Nombre: " + afd.getNombre()));
        infoPanel.add(crearEtiqueta("Descripción: " + (afd.getDescripcion() != null ? afd.getDescripcion() : "")));
        infoPanel.add(crearEtiqueta("Estado Inicial: " + afd.getEstadoInicial()));
        
        String finales = String.join(", ", afd.getEstadosFinales());
        infoPanel.add(crearEtiqueta("Estados Finales: " + finales));
        
        String alfabeto = String.join(", ", afd.getAlfabeto());
        infoPanel.add(crearEtiqueta("Alfabeto: " + alfabeto));
        
        int totalTransiciones = 0;
        for (Map<String, String> trans : afd.getTransiciones().values()) {
            totalTransiciones += trans.size();
        }
        
        String[] columnNames = {"Estado Origen", "Símbolo", "Estado Destino"};
        Object[][] data = new Object[totalTransiciones][3];
        
        int i = 0;
        for (String origen : afd.getTransiciones().keySet()) {
            for (Map.Entry<String, String> entry : afd.getTransiciones().get(origen).entrySet()) {
                String simbolo = entry.getKey();
                String destino = entry.getValue();
                if (i < data.length) {
                    data[i][0] = origen;
                    data[i][1] = simbolo;
                    data[i][2] = destino;
                    i++;
                }
            }
        }
        
        JTable tablaTransiciones = new JTable(data, columnNames);
        JScrollPane scrollTransiciones = new JScrollPane(tablaTransiciones);
        scrollTransiciones.setPreferredSize(new Dimension(400, 150));
        
        panelAutomata.add(infoPanel, BorderLayout.NORTH);
        panelAutomata.add(scrollTransiciones, BorderLayout.CENTER);
        
        panelAutomata.revalidate();
        panelAutomata.repaint();
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    private void analizarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo .lfp");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos LFP (*.lfp)", "lfp"));
        
        int userSelection = fileChooser.showOpenDialog(this);
    
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            try {
                // Limpiar datos anteriores
                automatas.clear();
                comboAutomas.removeAllItems();
                textAreaTexto.setText("");
                
                // Analizar archivo
                Map<String, AFD> nuevosAutomatas = AnalizadorAFD.analizarArchivo(fileToOpen);
                automatas.putAll(nuevosAutomatas);
                
                if (automatas.isEmpty()) {
                    textAreaTexto.setText("No se encontraron autómatas válidos en el archivo.");
                } else {
                    // Llenar combobox
                    for (String nombreAFD : automatas.keySet()) {
                        comboAutomas.addItem(nombreAFD);
                    }
                    
                    // Mostrar resumen
                    textAreaTexto.setText("Archivo analizado: " + fileToOpen.getName() + "\n");
                    textAreaTexto.append("Autómatas cargados: " + automatas.size() + "\n\n");
                    
                    // Mostrar información de cada autómata
                    for (AFD afd : automatas.values()) {
                        textAreaTexto.append("=== " + afd.getNombre() + " ===\n");
                        textAreaTexto.append("Estados: " + afd.getEstados().size() + "\n");
                        textAreaTexto.append("Transiciones: " + countTransitions(afd) + "\n\n");
                    }
                    
                    // Seleccionar el primer autómata
                    if (!automatas.isEmpty()) {
                        comboAutomas.setSelectedIndex(0);
                        mostrarAutomataEnPanel(automatas.get(comboAutomas.getSelectedItem()));
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error al procesar el archivo:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                textAreaTexto.setText("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
    private int countTransitions(AFD afd) {
        int count = 0;
        for (Map<String, String> trans : afd.getTransiciones().values()) {
            count += trans.size();
        }
        return count;
    }
    
    private void graficarAutoma() {
        String selectedAutoma = (String) comboAutomas.getSelectedItem();
        if (selectedAutoma != null) {
            AFD afd = automatas.get(selectedAutoma);
            if (afd != null) {
                // Verificar que el AFD esté completo
                if (afd.getEstadoInicial() == null || afd.getEstadosFinales().isEmpty() || 
                    afd.getTransiciones().isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "El autómata no está completo. Faltan definiciones.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                GeneradorGrafo.graficarAFD(afd, panelGrafo);
                mostrarAutomataEnPanel(afd);
                textAreaTexto.append("\nAutómata graficado: " + selectedAutoma);
            }
        }
    }

    private void generarReportes() {
        String selectedAutoma = (String) comboAutomas.getSelectedItem();
        if (selectedAutoma != null) {
            AFD afd = automatas.get(selectedAutoma);
            if (afd != null) {
                GeneradorReportes.generarReportesImagenes(afd, panelGrafo);
                mostrarAutomataEnPanel(afd);
                textAreaTexto.append("\nReportes generados para: " + selectedAutoma);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No se encontró el autómata seleccionado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un autómata para generar reportes.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void validarCadena() {
        String selectedAutoma = (String) comboAutomas.getSelectedItem();
        String cadena = txtCadena.getText();
        
        if (selectedAutoma != null && !cadena.isEmpty()) {
            AFD afd = automatas.get(selectedAutoma);
            if (afd != null) {
                boolean esValida = afd.validarCadena(cadena);
                String mensaje = "La cadena '" + cadena + "' es " + (esValida ? "VÁLIDA" : "INVÁLIDA") + 
                                " para el autómata " + selectedAutoma;
                JOptionPane.showMessageDialog(this, mensaje, "Resultado de Validación", 
                    JOptionPane.INFORMATION_MESSAGE);
                textAreaTexto.append("\nValidación: " + mensaje);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No se encontró el autómata seleccionado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un autómata e ingrese una cadena para validar.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
}
