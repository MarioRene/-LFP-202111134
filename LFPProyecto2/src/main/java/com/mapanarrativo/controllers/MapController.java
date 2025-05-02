package main.java.com.mapanarrativo.controllers;

import main.java.com.mapanarrativo.lexer.Lexer;
import main.java.com.mapanarrativo.models.ErrorEntry;
import main.java.com.mapanarrativo.models.Token;
import main.java.com.mapanarrativo.models.World;
import main.java.com.mapanarrativo.parser.Parser;
import main.java.com.mapanarrativo.reports.ReportGenerator;
import main.java.com.mapanarrativo.utils.DotGenerator;
import main.java.com.mapanarrativo.utils.GraphvizUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapController {
    private List<Token> tokens;
    private List<ErrorEntry> errors;
    private List<World> worlds;
    
    public MapController() {
        this.tokens = new ArrayList<>();
        this.errors = new ArrayList<>();
        this.worlds = new ArrayList<>();
    }
    
    public List<World> analyzeCode(String code) {
        // Clean previous state
        tokens.clear();
        errors.clear();
        worlds.clear();
        
        // Lexical analysis
        Lexer lexer = new Lexer(code);
        tokens = lexer.tokenize();
        List<ErrorEntry> lexicalErrors = lexer.getErrors();
        
        // Syntactic analysis
        Parser parser = new Parser(tokens);
        worlds = parser.parse();
        List<ErrorEntry> syntacticErrors = parser.getErrors();
        
        // Combine errors
        errors.addAll(lexicalErrors);
        errors.addAll(syntacticErrors);
        
        return worlds;
    }
    
    public File generateMapImage(World world) {
        String dotCode = DotGenerator.generateDot(world);
        return GraphvizUtil.generateImageFromDot(dotCode);
    }
    
    public void generateReports(String tokenReportPath, String errorReportPath) {
        ReportGenerator.generateTokenReport(tokens, tokenReportPath);
        ReportGenerator.generateErrorReport(errors, errorReportPath);
    }
    
    public List<Token> getTokens() {
        return tokens;
    }
    
    public List<ErrorEntry> getErrors() {
        return errors;
    }
    
    public List<World> getWorlds() {
        return worlds;
    }
}