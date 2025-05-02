package main.java.com.mapanarrativo.models;

import java.util.ArrayList;
import java.util.List;

public class World {
    private String name;
    private List<Place> places;
    private List<Connection> connections;
    private List<MapObject> objects;

    public World(String name) {
        this.name = name;
        this.places = new ArrayList<>();
        this.connections = new ArrayList<>();
        this.objects = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public List<MapObject> getObjects() {
        return objects;
    }

    public void addPlace(Place place) {
        places.add(place);
    }

    public void addConnection(Connection connection) {
        connections.add(connection);
    }

    public void addObject(MapObject object) {
        objects.add(object);
    }

    @Override
    public String toString() {
        return "World{" +
                "name='" + name + '\'' +
                ", places=" + places.size() +
                ", connections=" + connections.size() +
                ", objects=" + objects.size() +
                '}';
    }
}