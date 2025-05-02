package main.java.com.mapanarrativo.ui;

import main.java.com.mapanarrativo.controllers.MapController;
import main.java.com.mapanarrativo.models.World;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainFrame extends JFrame {
    private JTextArea textArea;
    private ImagePanel imagePanel;
    private JComboBox<String> mapSelector;
    private MapController mapController;

    public MainFrame() {
        mapController = new MapController();
        initialize();
    }

    private void initialize() {
        // Setup frame
        setTitle("Generador Visual de Mapas Narrativos");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Initialize components
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane textScrollPane = new JScrollPane(textArea);
        
        imagePanel = new ImagePanel();
        JPanel imageContainer = new JPanel(new BorderLayout());
        imageContainer.setBorder(BorderFactory.createTitledBorder("Área de Imagen"));
        imageContainer.add(new JScrollPane(imagePanel), BorderLayout.CENTER);
        
        JButton loadButton = new JButton("Cargar Archivo");
        JButton clearButton = new JButton("Limpiar Área");
        JButton analyzeButton = new JButton("Analizar Archivo");
        JButton reportButton = new JButton("Generar Reportes");
        JButton aboutButton = new JButton("Acerca de");
        
        mapSelector = new JComboBox<>();
        mapSelector.setEnabled(false);
        
        // Set up layouts
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(loadButton);
        controlPanel.add(clearButton);
        controlPanel.add(analyzeButton);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(controlPanel, BorderLayout.WEST);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(reportButton);
        rightPanel.add(new JLabel("Mapa: "));
        rightPanel.add(mapSelector);
        topPanel.add(rightPanel, BorderLayout.EAST);
        
        JPanel aboutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        aboutPanel.add(aboutButton);
        
        // Set up split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, textScrollPane, imageContainer);
        splitPane.setResizeWeight(0.4);
        
        // Add components to frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(aboutPanel, BorderLayout.SOUTH);
        
        // Event handlers
        loadButton.addActionListener(this::loadFile);
        clearButton.addActionListener(e -> clearTextArea());
        analyzeButton.addActionListener(e -> analyzeFile());
        reportButton.addActionListener(e -> generateReports());
        aboutButton.addActionListener(e -> showAboutDialog());
        mapSelector.addActionListener(e -> generateSelectedMap());
        
        // Show the frame
        setVisible(true);
    }

    private void loadFile(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Archivo");
        
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            try {
                // Read file content
                StringBuilder content = new StringBuilder();
                java.util.Scanner scanner = new java.util.Scanner(selectedFile);
                
                while (scanner.hasNextLine()) {
                    content.append(scanner.nextLine()).append("\n");
                }
                
                scanner.close();
                
                // Update text area
                textArea.setText(content.toString());
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Error al cargar el archivo: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearTextArea() {
        textArea.setText("");
        imagePanel.setImage(null);
        mapSelector.removeAllItems();
        mapSelector.setEnabled(false);
    }

    private void analyzeFile() {
        String code = textArea.getText();
        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El área de texto está vacía. Por favor cargue un archivo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Analyze code using controller
        List<World> worlds = mapController.analyzeCode(code);
        
        // Update map selector
        mapSelector.removeAllItems();
        for (World world : worlds) {
            mapSelector.addItem(world.getName());
        }
        
        if (!worlds.isEmpty()) {
            mapSelector.setEnabled(true);
            mapSelector.setSelectedIndex(0);
            generateSelectedMap();
        } else {
            mapSelector.setEnabled(false);
            imagePanel.setImage(null);
            
            if (!mapController.getErrors().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Se encontraron errores al analizar el archivo. " +
                                "Genere un reporte para más detalles.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron mapas válidos en el archivo.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void generateSelectedMap() {
        int selectedIndex = mapSelector.getSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }
        
        List<World> worlds = mapController.getWorlds();
        if (worlds.isEmpty() || selectedIndex >= worlds.size()) {
            return;
        }
        
        World selectedWorld = worlds.get(selectedIndex);
        File imageFile = mapController.generateMapImage(selectedWorld);
        
        if (imageFile != null && imageFile.exists()) {
            try {
                Image image = ImageIO.read(imageFile);
                imagePanel.setImage(image);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Error al cargar la imagen: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error al generar la imagen. Asegúrese de tener Graphviz instalado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateReports() {
        if (mapController.getTokens().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay tokens para generar reportes. Analice un archivo primero.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Select directory for reports
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar carpeta para reportes");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        int result = fileChooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        File directory = fileChooser.getSelectedFile();
        String tokenReportPath = directory.getAbsolutePath() + File.separator + "Tokens.html";
        String errorReportPath = directory.getAbsolutePath() + File.separator + "Errores.html";
        
        // Generate reports
        mapController.generateReports(tokenReportPath, errorReportPath);
        
        JOptionPane.showMessageDialog(this,
                "Reportes generados exitosamente en:\n" + directory.getAbsolutePath(),
                "Reportes Generados", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
                "Generador Visual de Mapas Narrativos\n\n" +
                        "Nombre: Mario Rene Merida Taracena\n" +
                        "Carnet: 202111134\n\n" +
                        "Universidad de San Carlos de Guatemala\n" +
                        "Facultad de Ingeniería\n" +
                        "Lenguajes Formales y de Programación\n" +
                        "Primer Semestre 2025",
                "Acerca de", JOptionPane.INFORMATION_MESSAGE);
    }
}