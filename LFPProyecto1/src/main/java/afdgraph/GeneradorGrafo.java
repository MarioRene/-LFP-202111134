package main.java.afdgraph;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class GeneradorGrafo {
    public static void graficarAFD(AFD afd, JPanel panelGrafo) {
        try {
            String nombreArchivo = "afd_" + afd.getNombre().replaceAll("[^a-zA-Z0-9]", "_");
            
            // Crear archivo DOT
            File dotFile = new File(nombreArchivo + ".dot");
            FileWriter writer = new FileWriter(dotFile);
            
            writer.write("digraph " + afd.getNombre() + " {\n");
            writer.write("  rankdir=LR;\n");
            writer.write("  node [shape = circle];\n");
            
            // Estados finales con doble círculo
            for (String estadoFinal : afd.getEstadosFinales()) {
                writer.write("  \"" + estadoFinal + "\" [shape = doublecircle];\n");
            }
            
            // Estado inicial
            writer.write("  \"\" [shape = none];\n");
            writer.write("  \"\" -> \"" + afd.getEstadoInicial() + "\";\n");
            
            // Transiciones
            for (String estadoOrigen : afd.getTransiciones().keySet()) {
                Map<String, String> transiciones = afd.getTransiciones().get(estadoOrigen);
                for (String simbolo : transiciones.keySet()) {
                    String estadoDestino = transiciones.get(simbolo);
                    writer.write("  \"" + estadoOrigen + "\" -> \"" + estadoDestino + "\" [label = \"" + simbolo + "\"];\n");
                }
            }
            
            writer.write("}\n");
            writer.close();
            
            // Ejecutar Graphviz
            ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", nombreArchivo + ".dot", "-o", nombreArchivo + ".png");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            process.waitFor();
            
            // Mostrar imagen en el panel
            BufferedImage imagen = ImageIO.read(new File(nombreArchivo + ".png"));
            ImageIcon icono = new ImageIcon(imagen);
            JLabel etiqueta = new JLabel(icono);
            
            panelGrafo.removeAll();
            panelGrafo.setLayout(new BorderLayout());
            panelGrafo.add(new JScrollPane(etiqueta), BorderLayout.CENTER);
            panelGrafo.revalidate();
            panelGrafo.repaint();
            
            // Eliminar archivos temporales
            dotFile.delete();
            new File(nombreArchivo + ".png").delete();
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panelGrafo, "Error al generar el gráfico: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
