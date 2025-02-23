package models;

public class Character {
    private String name;
    private int health;
    private int attack;
    private int defense;

    public Character(String name, int health, int attack, int defense) {
        this.name = name;
        this.health = health * 10; // La vida inicial es 10 veces la salud
        this.attack = attack;
        this.defense = defense;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public boolean isAlive() {
        return health > 0;
    }
}
