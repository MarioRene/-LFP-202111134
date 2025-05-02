package main.java.com.mapanarrativo.utils;

import main.java.com.mapanarrativo.models.*;
import java.util.HashMap;
import java.util.Map;

public class DotGenerator {
    private static final Map<String, String> PLACE_SHAPES = new HashMap<>();
    private static final Map<String, String> PLACE_COLORS = new HashMap<>();
    private static final Map<String, String> OBJECT_SHAPES = new HashMap<>();
    private static final Map<String, String> OBJECT_COLORS = new HashMap<>();
    private static final Map<String, String> OBJECT_EMOJIS = new HashMap<>();
    private static final Map<String, String> CONNECTION_STYLES = new HashMap<>();
    private static final Map<String, String> CONNECTION_COLORS = new HashMap<>();

    static {
        // Place shapes and colors
        PLACE_SHAPES.put("playa", "ellipse");
        PLACE_COLORS.put("playa", "lightblue");
        PLACE_SHAPES.put("cueva", "box");
        PLACE_COLORS.put("cueva", "gray");
        PLACE_SHAPES.put("templo", "octagon");
        PLACE_COLORS.put("templo", "gold");
        PLACE_SHAPES.put("jungla", "parallelogram");
        PLACE_COLORS.put("jungla", "forestgreen");
        PLACE_SHAPES.put("montaña", "triangle");
        PLACE_COLORS.put("montaña", "sienna");
        PLACE_SHAPES.put("pueblo", "house");
        PLACE_COLORS.put("pueblo", "burlywood");
        PLACE_SHAPES.put("isla", "invtriangle");
        PLACE_COLORS.put("isla", "lightgoldenrod");
        PLACE_SHAPES.put("río", "hexagon");
        PLACE_COLORS.put("río", "deepskyblue");
        PLACE_SHAPES.put("volcán", "doublecircle");
        PLACE_COLORS.put("volcán", "orangered");
        PLACE_SHAPES.put("pantano", "trapezium");
        PLACE_COLORS.put("pantano", "darkseagreen");

        // Object shapes, colors, and emojis
        OBJECT_SHAPES.put("tesoro", "box3d");
        OBJECT_COLORS.put("tesoro", "gold");
        OBJECT_EMOJIS.put("tesoro", "🎁");
        
        OBJECT_SHAPES.put("llave", "pentagon");
        OBJECT_COLORS.put("llave", "lightsteelblue");
        OBJECT_EMOJIS.put("llave", "🔑");
        
        OBJECT_SHAPES.put("arma", "diamond");
        OBJECT_COLORS.put("arma", "orangered");
        OBJECT_EMOJIS.put("arma", "🗡️");
        
        OBJECT_SHAPES.put("objeto mágico", "component");
        OBJECT_COLORS.put("objeto mágico", "violet");
        OBJECT_EMOJIS.put("objeto mágico", "✨");
        
        OBJECT_SHAPES.put("poción", "cylinder");
        OBJECT_COLORS.put("poción", "plum");
        OBJECT_EMOJIS.put("poción", "⚗️");
        
        OBJECT_SHAPES.put("trampa", "hexagon");
        OBJECT_COLORS.put("trampa", "crimson");
        OBJECT_EMOJIS.put("trampa", "💣");
        
        OBJECT_SHAPES.put("libro", "note");
        OBJECT_COLORS.put("libro", "navajowhite");
        OBJECT_EMOJIS.put("libro", "📕");
        
        OBJECT_SHAPES.put("herramienta", "folder");
        OBJECT_COLORS.put("herramienta", "darkkhaki");
        OBJECT_EMOJIS.put("herramienta", "🛠️");
        
        OBJECT_SHAPES.put("bandera", "tab");
        OBJECT_COLORS.put("bandera", "white");
        OBJECT_EMOJIS.put("bandera", "🚩");
        
        OBJECT_SHAPES.put("gema", "egg");
        OBJECT_COLORS.put("gema", "deepskyblue");
        OBJECT_EMOJIS.put("gema", "💎");

        // Connection styles and colors
        CONNECTION_STYLES.put("camino", "solid");
        CONNECTION_COLORS.put("camino", "black");
        
        CONNECTION_STYLES.put("puente", "dotted");
        CONNECTION_COLORS.put("puente", "gray");
        
        CONNECTION_STYLES.put("sendero", "dashed");
        CONNECTION_COLORS.put("sendero", "saddlebrown");
        
        CONNECTION_STYLES.put("carretera", "solid");
        CONNECTION_COLORS.put("carretera", "darkgray");
        
        CONNECTION_STYLES.put("nado", "dashed");
        CONNECTION_COLORS.put("nado", "deepskyblue");
        
        CONNECTION_STYLES.put("lancha", "solid");
        CONNECTION_COLORS.put("lancha", "blue");
        
        CONNECTION_STYLES.put("teleférico", "dotted");
        CONNECTION_COLORS.put("teleférico", "purple");
    }

    public static String generateDot(World world) {
        StringBuilder dot = new StringBuilder();
        
        dot.append("digraph \"").append(world.getName()).append("\" {\n");
        dot.append("    graph [pad=\"0.5\", nodesep=\"0.5\", ranksep=\"2\"];\n");
        dot.append("    node [shape=plain];\n");
        dot.append("    rankdir=LR;\n\n");
        
        // Add places
        for (Place place : world.getPlaces()) {
            String shape = PLACE_SHAPES.getOrDefault(place.getType(), "rectangle");
            String color = PLACE_COLORS.getOrDefault(place.getType(), "white");
            
            dot.append("    \"").append(place.getName()).append("\" [");
            dot.append("shape=").append(shape).append(", ");
            dot.append("fillcolor=\"").append(color).append("\", ");
            dot.append("style=filled, ");
            dot.append("label=\"").append(place.getName()).append("\"];\n");
        }
        
        // Add objects with coordinates (not in a place)
        for (MapObject object : world.getObjects()) {
            if (object.hasCoordinates()) {
                String shape = OBJECT_SHAPES.getOrDefault(object.getType(), "rectangle");
                String color = OBJECT_COLORS.getOrDefault(object.getType(), "white");
                String emoji = OBJECT_EMOJIS.getOrDefault(object.getType(), "");
                
                dot.append("    \"").append(object.getName()).append("\" [");
                dot.append("shape=").append(shape).append(", ");
                dot.append("fillcolor=\"").append(color).append("\", ");
                dot.append("style=filled, ");
                dot.append("label=\"").append(emoji).append(" ").append(object.getName()).append("\"];\n");
            }
        }
        
        // Add connections between places
        for (Connection connection : world.getConnections()) {
            String style = CONNECTION_STYLES.getOrDefault(connection.getType(), "solid");
            String color = CONNECTION_COLORS.getOrDefault(connection.getType(), "black");
            
            dot.append("    \"").append(connection.getFrom()).append("\" -> \"")
               .append(connection.getTo()).append("\" [");
            dot.append("label=\"").append(connection.getType()).append("\", ");
            dot.append("style=").append(style).append(", ");
            dot.append("color=\"").append(color).append("\"];\n");
        }
        
        // Add objects at places
        for (MapObject object : world.getObjects()) {
            if (!object.hasCoordinates()) {
                String shape = OBJECT_SHAPES.getOrDefault(object.getType(), "rectangle");
                String color = OBJECT_COLORS.getOrDefault(object.getType(), "white");
                String emoji = OBJECT_EMOJIS.getOrDefault(object.getType(), "");
                
                dot.append("    \"").append(object.getName()).append("\" [");
                dot.append("shape=").append(shape).append(", ");
                dot.append("fillcolor=\"").append(color).append("\", ");
                dot.append("style=filled, ");
                dot.append("label=\"").append(emoji).append(" ").append(object.getName()).append("\"];\n");
                
                // Connect object to its place with a dotted line
                dot.append("    \"").append(object.getName()).append("\" -> \"")
                   .append(object.getPlace()).append("\" [");
                dot.append("label=\"en\", ");
                dot.append("style=dotted, ");
                dot.append("dir=none];\n");
            }
        }
        
        dot.append("}");
        
        return dot.toString();
    }
}