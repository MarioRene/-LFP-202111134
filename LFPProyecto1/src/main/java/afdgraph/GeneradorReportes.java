package main.java.afdgraph;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GeneradorReportes {
    public static void generarReporteTokens(List<Token> tokens, String nombreAFD) throws IOException {
        crearDirectorioReportes();
        String archivo = "reportes/reporte_tokens_" + nombreAFD + "_" + 
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
        
        try (FileWriter fw = new FileWriter(archivo)) {
            fw.write("Reporte de Tokens - " + nombreAFD + "\n\n");
            fw.write(String.format("%-15s %-20s %-8s %-8s\n", "Tipo", "Lexema", "Línea", "Columna"));
            fw.write(String.format("%-15s %-20s %-8s %-8s\n", "-----", "------", "-----", "-------"));
            
            tokens.forEach(token -> {
                try {
                    fw.write(String.format("%-15s %-20s %-8d %-8d\n", 
                        token.getTipo(), token.getLexema(), token.getLinea(), token.getColumna()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void generarReporteErrores(List<ErrorLexico> errores, String nombreAFD) throws IOException {
        crearDirectorioReportes();
        String archivo = "reportes/reporte_errores_" + nombreAFD + "_" + 
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
        
        try (FileWriter fw = new FileWriter(archivo)) {
            fw.write("Reporte de Errores - " + nombreAFD + "\n\n");
            fw.write(String.format("%-10s %-8s %-8s\n", "Caracter", "Línea", "Columna"));
            fw.write(String.format("%-10s %-8s %-8s\n", "--------", "-----", "-------"));
            
            errores.forEach(error -> {
                try {
                    fw.write(String.format("%-10s %-8d %-8d\n", 
                        error.getCaracter(), error.getLinea(), error.getColumna()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private static void crearDirectorioReportes() throws IOException {
        if (!Files.exists(Paths.get("reportes"))) {
            Files.createDirectory(Paths.get("reportes"));
        }
    }
}
