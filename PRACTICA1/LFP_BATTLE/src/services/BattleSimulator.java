package services;

import models.Character;
import utils.Logger;

public class BattleSimulator {

    public static Character simulateBattle(Character character1, Character character2) {
        Logger.log("Batalla entre " + character1.getName() + " vs " + character2.getName());

        while (character1.isAlive() && character2.isAlive()) {
            // Personaje 1 ataca a Personaje 2
            int damage1 = Math.max(1, character1.getAttack() - character2.getDefense()); // Daño mínimo de 1
            character2.takeDamage(damage1);
            Logger.log(character1.getName() + " ataca a " + character2.getName() + " causando " + damage1 + " de daño.");

            // Verificar si el Personaje 2 sigue vivo
            if (!character2.isAlive()) {
                Logger.log(character1.getName() + " gana la batalla.");
                return character1;
            }

            // Personaje 2 ataca a Personaje 1
            int damage2 = Math.max(1, character2.getAttack() - character1.getDefense()); // Daño mínimo de 1
            character1.takeDamage(damage2);
            Logger.log(character2.getName() + " ataca a " + character1.getName() + " causando " + damage2 + " de daño.");

            // Verificar si el Personaje 1 sigue vivo
            if (!character1.isAlive()) {
                Logger.log(character2.getName() + " gana la batalla.");
                return character2;
            }
        }

        return null; // Nunca debería llegar aquí
    }
}
