package main.java.com.mapanarrativo;

import main.java.com.mapanarrativo.lexer.Lexer;
import main.java.com.mapanarrativo.models.Token;
import main.java.com.mapanarrativo.models.World;
import main.java.com.mapanarrativo.parser.Parser;
import main.java.com.mapanarrativo.utils.DotGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TestGenerator {
    public static void main(String[] args) {
        // Example input
        String input = "world \"Isla del Místico\" {\n" +
                "   place Playa:playa at (0,0)\n" +
                "   place Cueva:cueva at (2,1)\n" +
                "   place Templo:templo at (3,3)\n" +
                "   place Isla:isla at (1,2)\n" +
                "   place Pueblo:pueblo at (4,1)\n" +
                "   connect Playa to Cueva with \"sendero\"\n" +
                "   connect Cueva to Templo with \"puente\"\n" +
                "   connect Isla to Playa with \"lancha\"\n" +
                "   connect Pueblo to Templo with \"carretera\"\n" +
                "   object \"Tesoro\":tesoro at Cueva\n" +
                "   object \"Llave\":llave at (3,2)\n" +
                "   object \"Libro\":libro at Pueblo\n" +
                "}";
        
        // Lexical analysis
        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();
        
        System.out.println("Tokens:");
        for (Token token : tokens) {
            System.out.println(token);
        }
        
        System.out.println("\nLexer Errors:");
        lexer.getErrors().forEach(System.out::println);
        
        // Syntactic analysis
        Parser parser = new Parser(tokens);
        List<World> worlds = parser.parse();
        
        System.out.println("\nParser Errors:");
        parser.getErrors().forEach(System.out::println);
        
        System.out.println("\nWorlds:");
        worlds.forEach(System.out::println);
        
        // Generate DOT file for GraphViz
        if (!worlds.isEmpty()) {
            World firstWorld = worlds.get(0);
            String dotCode = DotGenerator.generateDot(firstWorld);
            
            try {
                File dotFile = new File("test_map.dot");
                try (FileWriter writer = new FileWriter(dotFile)) {
                    writer.write(dotCode);
                }
                System.out.println("DOT file generated: " + dotFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error writing DOT file: " + e.getMessage());
            }
        }
    }
}