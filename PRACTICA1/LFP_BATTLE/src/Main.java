import models.Character;
import services.CharacterFileReader;
import services.BattleSimulator;
import services.ReportGenerator;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import utils.Logger;

public class Main {
    private static List<Character> characters = new ArrayList<>();
    private static boolean fileLoaded = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            showMenu();
            option = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (option) {
                case 1:
                    loadFile(scanner);
                    break;
                case 2:
                    if (fileLoaded) {
                        playTournament();
                    } else {
                        Logger.log("Primero debes cargar un archivo.");
                    }
                    break;
                case 3:
                    if (fileLoaded) {
                        ReportGenerator.generateAttackReport(characters, "reports/top_attack.html");
                        Logger.log("Reporte de mayor ataque generado en 'reports/top_attack.html'.");
                    } else {
                        Logger.log("Primero debes cargar un archivo.");
                    }
                    break;
                case 4:
                    if (fileLoaded) {
                        ReportGenerator.generateDefenseReport(characters, "reports/top_defense.html");
                        Logger.log("Reporte de mayor defensa generado en 'reports/top_defense.html'.");
                    } else {
                        Logger.log("Primero debes cargar un archivo.");
                    }
                    break;
                case 5:
                    showDeveloperInfo();
                    break;
                case 6:
                    Logger.log("Saliendo de la aplicación...");
                    break;
                default:
                    Logger.log("Opción no válida. Intenta de nuevo.");
            }
        } while (option != 6);

        scanner.close();
    }

    private static void showMenu() {
        Logger.log("\n=== MENU ===");
        Logger.log("1. Cargar Archivo");
        Logger.log("2. Jugar");
        Logger.log("3. Generar Reporte Mayor Ataque");
        Logger.log("4. Generar Reporte Mayor Defensa");
        Logger.log("5. Información del Desarrollador");
        Logger.log("6. Salir");
        Logger.log("Selecciona una opción: ");
    }

    private static void loadFile(Scanner scanner) {
        Logger.log("Ingresa la ruta del archivo .lfp: ");
        String filePath = scanner.nextLine();

        characters = CharacterFileReader.readCharacters(filePath);
        if (!characters.isEmpty()) {
            fileLoaded = true;
            Logger.log("Archivo cargado exitosamente. " + characters.size() + " personajes listos.");
        } else {
            Logger.log("Error al cargar el archivo. Verifica la ruta y el formato.");
        }
    }

    private static void playTournament() {
        List<Character> tournamentCharacters = new ArrayList<>(characters);

        while (tournamentCharacters.size() > 1) {
            Logger.log("=== NUEVA RONDA ===");
            List<Character> survivors = new ArrayList<>();

            for (int i = 0; i < tournamentCharacters.size(); i += 2) {
                if (i + 1 < tournamentCharacters.size()) {
                    Character winner = BattleSimulator.simulateBattle(tournamentCharacters.get(i), tournamentCharacters.get(i + 1));
                    survivors.add(winner);
                } else {
                    // Si hay un número impar de personajes, el último avanza automáticamente
                    survivors.add(tournamentCharacters.get(i));
                }
            }

            tournamentCharacters = survivors;
        }

        // Declarar al campeón
        if (!tournamentCharacters.isEmpty()) {
            Logger.log(tournamentCharacters.get(0).getName() + " es el campeón del torneo!!!");
        } else {
            Logger.log("No hay campeón.");
        }
    }

    private static void showDeveloperInfo() {
        Logger.log("\n    === INFORMACIÓN DEL DESARROLLADOR ===");
        Logger.log("           ¡Hola! Soy el creador");
        Logger.log("  --------------------------------------");
        Logger.log("  | Nombre: Mario Rene Merida Taracena |");
        Logger.log("  | Carne: 202111134                   |");
        Logger.log("  --------------------------------------");
    }
}

