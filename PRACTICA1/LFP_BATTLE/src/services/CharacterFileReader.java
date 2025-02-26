package services;

import models.Character;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CharacterFileReader {

    public static List<Character> readCharacters(String filePath) {
        List<Character> characters = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Saltar la primera línea (encabezado)
            while ((line = br.readLine()) != null) {
                // Usar "|" como separador y eliminar espacios en blanco alrededor de los valores
                String[] parts = line.split("\\|");
                if (parts.length == 4) { // Asegurarse de que haya 4 partes: Nombre, Salud, Ataque, Defensa
                    String name = parts[0].trim(); // Nombre
                    int health = Integer.parseInt(parts[1].trim()); // Salud
                    int attack = Integer.parseInt(parts[2].trim()); // Ataque
                    int defense = Integer.parseInt(parts[3].trim()); // Defensa
                    characters.add(new Character(name, health, attack, defense));
                } else {
                    System.err.println("Error: Formato incorrecto en la línea: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error: Formato numérico incorrecto en el archivo.");
        }

        return characters;
    }
}
