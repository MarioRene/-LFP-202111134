package main.java.com.mapanarrativo.utils;

import java.io.File;
import java.io.IOException;

public class GraphvizUtil {
    
    public static boolean generateImage(String dotFilePath, String outputImagePath) {
        try {
            // Using ProcessBuilder to execute Graphviz (dot command)
            ProcessBuilder processBuilder = new ProcessBuilder(
                "dot", "-Tpng", dotFilePath, "-o", outputImagePath
            );
            
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                return true; // Success
            } else {
                System.err.println("Graphviz process exited with code " + exitCode);
                return false;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error generating image: " + e.getMessage());
            return false;
        }
    }
    
    public static File generateTempDotFile(String dotContent) {
        try {
            File tempFile = File.createTempFile("map_", ".dot");
            tempFile.deleteOnExit();
            
            FileHandler.generateDotFile(dotContent, tempFile.getAbsolutePath());
            return tempFile;
        } catch (IOException e) {
            System.err.println("Error creating temporary DOT file: " + e.getMessage());
            return null;
        }
    }
    
    public static File generateImageFromDot(String dotContent) {
        try {
            // Create temp files for dot and image
            File dotFile = File.createTempFile("map_", ".dot");
            File imageFile = File.createTempFile("map_", ".png");
            
            // Configure to delete on JVM exit
            dotFile.deleteOnExit();
            imageFile.deleteOnExit();
            
            // Write dot content
            FileHandler.generateDotFile(dotContent, dotFile.getAbsolutePath());
            
            // Generate image
            if (generateImage(dotFile.getAbsolutePath(), imageFile.getAbsolutePath())) {
                return imageFile;
            } else {
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error in image generation process: " + e.getMessage());
            return null;
        }
    }
}