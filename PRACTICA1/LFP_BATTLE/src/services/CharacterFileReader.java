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

        try (BufferedReader br = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            br.readLine(); // Saltar la primera línea (encabezado)
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                String name = parts[0];
                int health = Integer.parseInt(parts[1]);
                int attack = Integer.parseInt(parts[2]);
                int defense = Integer.parseInt(parts[3]);
                characters.add(new Character(name, health, attack, defense));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return characters;
    }
}
