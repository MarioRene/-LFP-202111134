package main.java.com.mapanarrativo.reports;

import main.java.com.mapanarrativo.models.Token;
import main.java.com.mapanarrativo.models.ErrorEntry;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReportGenerator {
    
    public static void generateTokenReport(List<Token> tokens, String outputPath) {
        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html lang=\"es\">\n");
            writer.write("<head>\n");
            writer.write("    <meta charset=\"UTF-8\">\n");
            writer.write("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
            writer.write("    <title>Reporte de Tokens</title>\n");
            writer.write("    <style>\n");
            writer.write("        body { font-family: Arial, sans-serif; margin: 20px; }\n");
            writer.write("        h1 { color: #2c3e50; }\n");
            writer.write("        table { border-collapse: collapse; width: 100%; margin-top: 20px; }\n");
            writer.write("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
            writer.write("        th { background-color: #4CAF50; color: white; }\n");
            writer.write("        tr:nth-child(even) { background-color: #f2f2f2; }\n");
            writer.write("        tr:hover { background-color: #ddd; }\n");
            writer.write("    </style>\n");
            writer.write("</head>\n");
            writer.write("<body>\n");
            writer.write("    <h1>Reporte de Tokens</h1>\n");
            writer.write("    <table>\n");
            writer.write("        <tr>\n");
            writer.write("            <th>Token</th>\n");
            writer.write("            <th>Lexema</th>\n");
            writer.write("            <th>Línea</th>\n");
            writer.write("            <th>Columna</th>\n");
            writer.write("        </tr>\n");
            
            for (Token token : tokens) {
                if (token.getType().toString().equals("EOF")) {
                    continue; // Skip EOF token in report
                }
                
                writer.write("        <tr>\n");
                writer.write("            <td>" + token.getType() + "</td>\n");
                writer.write("            <td>" + escapeHtml(token.getLexeme()) + "</td>\n");
                writer.write("            <td>" + token.getLine() + "</td>\n");
                writer.write("            <td>" + token.getColumn() + "</td>\n");
                writer.write("        </tr>\n");
            }
            
            writer.write("    </table>\n");
            writer.write("</body>\n");
            writer.write("</html>");
            
        } catch (IOException e) {
            System.err.println("Error generating token report: " + e.getMessage());
        }
    }
    
    public static void generateErrorReport(List<ErrorEntry> errors, String outputPath) {
        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html lang=\"es\">\n");
            writer.write("<head>\n");
            writer.write("    <meta charset=\"UTF-8\">\n");
            writer.write("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
            writer.write("    <title>Reporte de Errores</title>\n");
            writer.write("    <style>\n");
            writer.write("        body { font-family: Arial, sans-serif; margin: 20px; }\n");
            writer.write("        h1 { color: #2c3e50; }\n");
            writer.write("        table { border-collapse: collapse; width: 100%; margin-top: 20px; }\n");
            writer.write("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
            writer.write("        th { background-color: #e74c3c; color: white; }\n");
            writer.write("        tr:nth-child(even) { background-color: #f2f2f2; }\n");
            writer.write("        tr:hover { background-color: #ddd; }\n");
            writer.write("    </style>\n");
            writer.write("</head>\n");
            writer.write("<body>\n");
            writer.write("    <h1>Reporte de Errores</h1>\n");
            writer.write("    <table>\n");
            writer.write("        <tr>\n");
            writer.write("            <th>Error</th>\n");
            writer.write("            <th>Línea</th>\n");
            writer.write("            <th>Columna</th>\n");
            writer.write("        </tr>\n");
            
            for (ErrorEntry error : errors) {
                writer.write("        <tr>\n");
                writer.write("            <td>" + escapeHtml(error.getError()) + "</td>\n");
                writer.write("            <td>" + error.getLine() + "</td>\n");
                writer.write("            <td>" + error.getColumn() + "</td>\n");
                writer.write("        </tr>\n");
            }
            
            writer.write("    </table>\n");
            writer.write("</body>\n");
            writer.write("</html>");
            
        } catch (IOException e) {
            System.err.println("Error generating error report: " + e.getMessage());
        }
    }
    
    private static String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}