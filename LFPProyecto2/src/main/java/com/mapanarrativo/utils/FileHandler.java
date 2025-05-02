package main.java.com.mapanarrativo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileHandler {
    
    public static String loadFile() {
        StringBuilder content = new StringBuilder();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Archivo");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt"));
        
        int result = fileChooser.showOpenDialog(null);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            } catch (IOException e) {
                System.err.println("Error loading file: " + e.getMessage());
            }
        }
        
        return content.toString();
    }
    
    public static void saveFile(String content, String extension) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Archivo");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos " + extension, extension));
        
        int result = fileChooser.showSaveDialog(null);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            // Add extension if not present
            if (!selectedFile.getName().toLowerCase().endsWith("." + extension)) {
                selectedFile = new File(selectedFile.getAbsolutePath() + "." + extension);
            }
            
            try (FileWriter writer = new FileWriter(selectedFile)) {
                writer.write(content);
            } catch (IOException e) {
                System.err.println("Error saving file: " + e.getMessage());
            }
        }
    }
    
    public static void generateDotFile(String dotCode, String outputPath) {
        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write(dotCode);
        } catch (IOException e) {
            System.err.println("Error generating DOT file: " + e.getMessage());
        }
    }
    
    public static void generateReportFiles(String tokenReportPath, String errorReportPath) {
        // Implementation to be added in ReportGenerator class
    }
}