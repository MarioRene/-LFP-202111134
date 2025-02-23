package services;

import models.Character;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReportGenerator {

    public static void generateAttackReport(List<Character> characters, String filePath) {
        // Ordenar personajes por ataque (de mayor a menor)
        characters.sort((c1, c2) -> Integer.compare(c2.getAttack(), c1.getAttack()));

        // Crear el contenido HTML
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><head><title>Top 5 - Mayor Ataque</title></head><body>");
        htmlContent.append("<h1>Top 5 - Mayor Ataque</h1>");
        htmlContent.append("<table border='1'><tr><th>Posición</th><th>Nombre</th><th>Ataque</th></tr>");

        for (int i = 0; i < Math.min(5, characters.size()); i++) {
            Character character = characters.get(i);
            htmlContent.append("<tr>")
                       .append("<td>").append(i + 1).append("</td>")
                       .append("<td>").append(character.getName()).append("</td>")
                       .append("<td>").append(character.getAttack()).append("</td>")
                       .append("</tr>");
        }

        htmlContent.append("</table></body></html>");

        // Escribir el archivo HTML
        writeFile(filePath, htmlContent.toString());
    }

    public static void generateDefenseReport(List<Character> characters, String filePath) {
        // Ordenar personajes por defensa (de mayor a menor)
        characters.sort((c1, c2) -> Integer.compare(c2.getDefense(), c1.getDefense()));

        // Crear el contenido HTML
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><head><title>Top 5 - Mayor Defensa</title></head><body>");
        htmlContent.append("<h1>Top 5 - Mayor Defensa</h1>");
        htmlContent.append("<table border='1'><tr><th>Posición</th><th>Nombre</th><th>Defensa</th></tr>");

        for (int i = 0; i < Math.min(5, characters.size()); i++) {
            Character character = characters.get(i);
            htmlContent.append("<tr>")
                       .append("<td>").append(i + 1).append("</td>")
                       .append("<td>").append(character.getName()).append("</td>")
                       .append("<td>").append(character.getDefense()).append("</td>")
                       .append("</tr>");
        }

        htmlContent.append("</table></body></html>");

        // Escribir el archivo HTML
        writeFile(filePath, htmlContent.toString());
    }

    private static void writeFile(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
            System.out.println("Reporte generado exitosamente: " + filePath);
        } catch (IOException e) {
            System.err.println("Error al generar el reporte: " + e.getMessage());
        }
    }
}
