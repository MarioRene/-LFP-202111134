package main.java.com.mapanarrativo.models;

public class MapObject {
    private String name;
    private String type;
    private int x;
    private int y;
    private String place;
    private boolean hasCoordinates;

    // Constructor for objects with coordinates
    public MapObject(String name, String type, int x, int y) {
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
        this.place = null;
        this.hasCoordinates = true;
    }

    // Constructor for objects at a place
    public MapObject(String name, String type, String place) {
        this.name = name;
        this.type = type;
        this.place = place;
        this.hasCoordinates = false;
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

    public String getPlace() {
        return place;
    }

    public boolean hasCoordinates() {
        return hasCoordinates;
    }

    @Override
    public String toString() {
        if (hasCoordinates) {
            return "MapObject{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        } else {
            return "MapObject{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", place='" + place + '\'' +
                    '}';
        }
    }
}