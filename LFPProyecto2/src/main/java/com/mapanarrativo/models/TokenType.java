package main.java.com.mapanarrativo.models;

public enum TokenType {
    // Keywords
    WORLD, PLACE, CONNECT, TO, WITH, OBJECT, AT,
    
    // Identifiers and literals
    IDENTIFIER, STRING, NUMBER,
    
    // Symbols
    LBRACE, RBRACE, LPAREN, RPAREN, COMMA, COLON,
    
    // End of file
    EOF
}