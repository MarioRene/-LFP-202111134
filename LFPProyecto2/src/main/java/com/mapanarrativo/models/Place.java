package main.java.com.mapanarrativo.models;

public class Place {
    private String name;
    private String type;
    private int x;
    private int y;

    public Place(String name, String type, int x, int y) {
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Place{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}