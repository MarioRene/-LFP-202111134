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
import java.util.ArrayList;

public class GeneradorReportes {
    public static void generarReportesImagenes(AFD afd, JPanel panel) {
        try {
            Path reportesDir = Paths.get("reportes");
            if (!Files.exists(reportesDir)) {
                Files.createDirectories(reportesDir);
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            
            // Reporte de Tokens (de todos los tokens cargados)
            String nombreTokens = "reporte_tokens_" + timeStamp + ".jpeg";
            File tokensFile = reportesDir.resolve(nombreTokens).toFile();
            generarImagenTabla(crearTablaTokensCompleta(), tokensFile, "Reporte de Tokens (Todos los archivos)");

            // Reporte de Errores (de todos los errores detectados)
            String nombreErrores = "reporte_errores_" + timeStamp + ".jpeg";
            File erroresFile = reportesDir.resolve(nombreErrores).toFile();
            generarImagenTabla(crearTablaErroresCompleta(), erroresFile, "Reporte de Errores Léxicos (Todos los archivos)");

            JOptionPane.showMessageDialog(panel,
                "Reportes generados exitosamente:\n" +
                "1. " + tokensFile.getAbsolutePath() + "\n" +
                "2. " + erroresFile.getAbsolutePath(),
                "Reportes Generados", JOptionPane.INFORMATION_MESSAGE);

            mostrarImagenEnPanel(tokensFile, panel);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel,
                "Error al generar reportes: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String[][] crearTablaTokensCompleta() {
        ArrayList<Token> tokens = AnalizadorAFD.getTodosTokens();
        String[][] tabla = new String[tokens.size() + 1][4];
        tabla[0] = new String[]{"Token", "Lexema", "Línea", "Columna"};
        
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            tabla[i+1] = new String[]{
                token.tipo.getNombre(),
                token.lexema,
                String.valueOf(token.linea),
                String.valueOf(token.columna)
            };
        }
        return tabla;
    }

    private static String[][] crearTablaErroresCompleta() {
        ArrayList<Error> errores = AnalizadorAFD.getTodosErrores();
        String[][] tabla = new String[errores.size() + 1][3];
        tabla[0] = new String[]{"Error", "Línea", "Columna"};
        
        for (int i = 0; i < errores.size(); i++) {
            Error error = errores.get(i);
            tabla[i+1] = new String[]{
                error.message,
                String.valueOf(error.line),
                String.valueOf(error.column)
            };
        }
        return tabla;
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
