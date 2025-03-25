package main.java.afdgraph;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GeneradorGrafo {
    public static void graficarAFD(AFD afd, JPanel panel) {
        if (afd == null || panel == null) return;
        
        try {
            // Crear archivo DOT temporal
            File dotFile = File.createTempFile("afd_", ".dot");
            try (FileWriter writer = new FileWriter(dotFile)) {
                writer.write("digraph " + afd.getNombre().replaceAll("\\s+", "_") + " {\n");
                writer.write("  rankdir=LR;\n  node [shape=circle];\n");
                
                // Estados finales
                afd.getEstadosFinales().forEach(estado -> 
                    writer.write("  \"" + estado + "\" [shape=doublecircle];\n"));
                
                // Estado inicial
                writer.write("  \"\" [shape=none];\n");
                writer.write("  \"\" -> \"" + afd.getEstadoInicial() + "\";\n");
                
                // Transiciones
                afd.getTransiciones().forEach((origen, trans) -> {
                    trans.forEach((simbolo, destino) -> {
                        try {
                            writer.write("  \"" + origen + "\" -> \"" + destino + 
                                       "\" [label=\"" + simbolo + "\"];\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                });
                writer.write("}\n");
            }
            
            // Generar imagen PNG
            File imgFile = File.createTempFile("afd_", ".png");
            try {
                ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", 
                    dotFile.getAbsolutePath(), "-o", imgFile.getAbsolutePath());
                pb.redirectErrorStream(true);
                Process p = pb.start();
                p.waitFor();
                
                // Mostrar imagen en el panel
                BufferedImage img = ImageIO.read(imgFile);
                if (img != null) {
                    ImageIcon icon = new ImageIcon(img.getScaledInstance(800, 600, Image.SCALE_SMOOTH));
                    panel.removeAll();
                    panel.add(new JScrollPane(new JLabel(icon)));
                    panel.revalidate();
                    panel.repaint();
                }
            } finally {
                // Limpiar archivos temporales
                dotFile.delete();
                imgFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, 
                "Error al generar gráfico:\n" + e.getMessage() +
                "\n\nAsegúrese que Graphviz esté instalado (https://graphviz.org/)",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
