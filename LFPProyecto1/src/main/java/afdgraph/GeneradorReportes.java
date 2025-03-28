package main.java.afdgraph;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class GeneradorReportes {
    public static void generarReportesImagenes(AFD afd, JPanel panel) {
        try {
            // Crear carpeta reportes si no existe
            Path reportesDir = Paths.get("reportes");
            if (!Files.exists(reportesDir)) {
                Files.createDirectories(reportesDir);
            }

            // Generar nombres de archivo con fecha y hora
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String nombreTokens = "reporte_tokens_" + afd.getNombre() + "_" + timeStamp + ".jpeg";
            String nombreErrores = "reporte_errores_" + afd.getNombre() + "_" + timeStamp + ".jpeg";

            // Generar imagen de tokens
            File tokensFile = reportesDir.resolve(nombreTokens).toFile();
            generarImagenTabla(crearTablaTokens(afd), tokensFile, "Reporte de Tokens");

            // Generar imagen de errores
            File erroresFile = reportesDir.resolve(nombreErrores).toFile();
            generarImagenTabla(crearTablaErrores(), erroresFile, "Reporte de Errores Léxicos");

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(panel,
                "Reportes generados exitosamente:\n" +
                "1. " + tokensFile.getAbsolutePath() + "\n" +
                "2. " + erroresFile.getAbsolutePath(),
                "Reportes Generados", JOptionPane.INFORMATION_MESSAGE);

            // Mostrar el reporte de tokens en el panel
            mostrarImagenEnPanel(tokensFile, panel);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel,
                "Error al generar reportes: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private static String[][] crearTablaTokens(AFD afd) {
        // Calcular tamaño exacto del array
        int numFilas = 1; // Encabezado
        numFilas++; // Identificador
        if (afd.getDescripcion() != null) numFilas++;
        numFilas += afd.getEstados().size();
        numFilas += afd.getAlfabeto().size();
        
        // Contar transiciones (ahora cada elemento es una fila separada)
        int numTransiciones = 0;
        for (Map<String, String> trans : afd.getTransiciones().values()) {
            numTransiciones += trans.size() * 3; // 3 filas por transición: origen, símbolo, destino
        }
        numFilas += numTransiciones;

        // Contar símbolos especiales (llaves, comas, etc.)
        numFilas += 5; // Para los símbolos especiales de la definición

        // Crear array con tamaño exacto
        String[][] tabla = new String[numFilas][4];
        int fila = 0;
        
        // Encabezados
        tabla[fila++] = new String[]{"Token", "Lexema", "Línea", "Columna"};
        
        // Datos del AFD
        tabla[fila++] = new String[]{"Identificador", afd.getNombre(), "1", "1"};
        
        if (afd.getDescripcion() != null) {
            tabla[fila++] = new String[]{"Descripción", afd.getDescripcion(), "2", "15"};
        }
        
        // Símbolos especiales de definición
        tabla[fila++] = new String[]{"Símbolo", "{", String.valueOf(fila), "1"};
        tabla[fila++] = new String[]{"Símbolo", "}", String.valueOf(fila), "1"};
        tabla[fila++] = new String[]{"Símbolo", "[", String.valueOf(fila), "1"};
        tabla[fila++] = new String[]{"Símbolo", "]", String.valueOf(fila), "1"};
        tabla[fila++] = new String[]{"Símbolo", ",", String.valueOf(fila), "1"};
        
        // Estados
        for (String estado : afd.getEstados()) {
            tabla[fila++] = new String[]{"Estado", estado, String.valueOf(fila), "1"};
        }
        
        // Alfabeto (incluyendo todos los símbolos)
        for (String simbolo : afd.getAlfabeto()) {
            tabla[fila++] = new String[]{"Símbolo", simbolo, String.valueOf(fila), "1"};
        }
        
        // Transiciones (separadas por tokens)
        for (String origen : afd.getTransiciones().keySet()) {
            for (Map.Entry<String, String> entry : afd.getTransiciones().get(origen).entrySet()) {
                String simbolo = entry.getKey();
                String destino = entry.getValue();
                
                // Estado origen
                tabla[fila++] = new String[]{"Transición (Origen)", origen, String.valueOf(fila), "1"};
                // Símbolo
                tabla[fila++] = new String[]{"Transición (Símbolo)", simbolo, String.valueOf(fila), "1"};
                // Estado destino
                tabla[fila++] = new String[]{"Transición (Destino)", destino, String.valueOf(fila), "1"};
            }
        }
        
        return tabla;
    }

    private static String[][] crearTablaErrores() {
        // Datos de ejemplo para errores léxicos
        return new String[][]{
            {"Carácter", "Línea", "Columna"},
            {"@", "1", "1"},
            {"$", "1", "2"},
            {"~", "2", "6"},
            {"+", "2", "8"}
        };
    }

    private static void generarImagenTabla(String[][] datos, File archivo, String titulo) throws IOException {
        // Configuración de la tabla
        int filas = datos.length;
        int columnas = datos[0].length;
        int anchoCelda = 200;
        int altoCelda = 30;
        int margen = 20;
        
        // Calcular dimensiones de la imagen
        int anchoImagen = columnas * anchoCelda + 2 * margen;
        int altoImagen = filas * altoCelda + 2 * margen + 40; // +40 para el título
        
        // Crear imagen
        BufferedImage imagen = new BufferedImage(anchoImagen, altoImagen, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imagen.createGraphics();
        
        // Configurar renderizado de texto
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Fondo blanco
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, anchoImagen, altoImagen);
        
        // Dibujar título
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g.getFontMetrics();
        int tituloX = (anchoImagen - fm.stringWidth(titulo)) / 2;
        g.drawString(titulo, tituloX, margen + fm.getAscent());
        
        // Fuente para la tabla
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        fm = g.getFontMetrics();
        
        // Dibujar tabla
        int tablaY = margen + 40;
        g.setColor(Color.BLACK);
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int x = j * anchoCelda + margen;
                int y = i * altoCelda + tablaY;
                
                // Dibujar celda
                g.drawRect(x, y, anchoCelda, altoCelda);
                
                // Centrar texto
                int textX = x + (anchoCelda - fm.stringWidth(datos[i][j])) / 2;
                int textY = y + (altoCelda - fm.getHeight()) / 2 + fm.getAscent();
                
                // Resaltar encabezados
                if (i == 0) {
                    g.setColor(new Color(220, 220, 220));
                    g.fillRect(x + 1, y + 1, anchoCelda - 1, altoCelda - 1);
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 12));
                }
                
                g.drawString(datos[i][j], textX, textY);
                
                // Restaurar fuente normal
                if (i == 0) {
                    g.setFont(new Font("Arial", Font.PLAIN, 12));
                }
            }
        }
        
        // Guardar imagen
        ImageIO.write(imagen, "jpeg", archivo);
        g.dispose();
    }

    private static void mostrarImagenEnPanel(File imagenFile, JPanel panel) throws IOException {
        BufferedImage imagen = ImageIO.read(imagenFile);
        ImageIcon icono = new ImageIcon(imagen.getScaledInstance(800, -1, Image.SCALE_SMOOTH));
        
        panel.removeAll();
        panel.setLayout(new BorderLayout());
        
        JScrollPane scrollPane = new JScrollPane(new JLabel(icono));
        scrollPane.setPreferredSize(new Dimension(850, 650));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }
}
