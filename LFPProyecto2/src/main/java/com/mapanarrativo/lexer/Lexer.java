package main.java.com.mapanarrativo.lexer;

import main.java.com.mapanarrativo.models.Token;
import main.java.com.mapanarrativo.models.TokenType;
import main.java.com.mapanarrativo.models.ErrorEntry;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private String input;
    private int position;
    private int line;
    private int column;
    private char currentChar;
    private List<Token> tokens;
    private List<ErrorEntry> errors;

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
        this.line = 1;
        this.column = 1;
        this.tokens = new ArrayList<>();
        this.errors = new ArrayList<>();
        
        if (!input.isEmpty()) {
            this.currentChar = input.charAt(0);
        } else {
            this.currentChar = '\0'; // End of file
        }
    }

    private void advance() {
        position++;
        column++;
        
        if (position >= input.length()) {
            currentChar = '\0'; // End of file
        } else {
            currentChar = input.charAt(position);
        }
    }

    private void advanceWithNewline() {
        position++;
        if (position >= input.length()) {
            currentChar = '\0'; // End of file
            return;
        }
        
        currentChar = input.charAt(position);
        line++;
        column = 1;
    }

    private void skipWhitespace() {
        while (currentChar != '\0' && Character.isWhitespace(currentChar)) {
            if (currentChar == '\n') {
                advanceWithNewline();
            } else {
                advance();
            }
        }
    }

    private String readIdentifier() {
        StringBuilder identifier = new StringBuilder();
        
        while (currentChar != '\0' && (Character.isLetterOrDigit(currentChar) || currentChar == '_')) {
            identifier.append(currentChar);
            advance();
        }
        
        return identifier.toString();
    }

    private String readNumber() {
        StringBuilder number = new StringBuilder();
        
        while (currentChar != '\0' && Character.isDigit(currentChar)) {
            number.append(currentChar);
            advance();
        }
        
        return number.toString();
    }

    private String readString() {
        StringBuilder str = new StringBuilder();
        int startLine = line;
        int startColumn = column;
        
        // Skip the opening quote
        advance();
        
        while (currentChar != '\0' && currentChar != '\"') {
            str.append(currentChar);
            advance();
        }
        
        if (currentChar == '\"') {
            advance(); // Skip the closing quote
            return str.toString();
        } else {
            // Error: unclosed string
            addError("Unclosed string literal", startLine, startColumn);
            return str.toString();
        }
    }

    public List<Token> tokenize() {
        while (currentChar != '\0') {
            if (Character.isWhitespace(currentChar)) {
                skipWhitespace();
                continue;
            }

            if (Character.isLetter(currentChar)) {
                String identifier = readIdentifier();
                int tokenLine = line;
                int tokenColumn = column - identifier.length();
                
                // Check for keywords
                switch (identifier) {
                    case "world":
                        tokens.add(new Token(TokenType.WORLD, identifier, tokenLine, tokenColumn));
                        break;
                    case "place":
                        tokens.add(new Token(TokenType.PLACE, identifier, tokenLine, tokenColumn));
                        break;
                    case "connect":
                        tokens.add(new Token(TokenType.CONNECT, identifier, tokenLine, tokenColumn));
                        break;
                    case "to":
                        tokens.add(new Token(TokenType.TO, identifier, tokenLine, tokenColumn));
                        break;
                    case "with":
                        tokens.add(new Token(TokenType.WITH, identifier, tokenLine, tokenColumn));
                        break;
                    case "object":
                        tokens.add(new Token(TokenType.OBJECT, identifier, tokenLine, tokenColumn));
                        break;
                    case "at":
                        tokens.add(new Token(TokenType.AT, identifier, tokenLine, tokenColumn));
                        break;
                    default:
                        tokens.add(new Token(TokenType.IDENTIFIER, identifier, tokenLine, tokenColumn));
                        break;
                }
                continue;
            }

            if (Character.isDigit(currentChar)) {
                String number = readNumber();
                tokens.add(new Token(TokenType.NUMBER, number, line, column - number.length()));
                continue;
            }

            if (currentChar == '\"') {
                int stringLine = line;
                int stringColumn = column;
                String stringValue = readString();
                tokens.add(new Token(TokenType.STRING, "\"" + stringValue + "\"", stringLine, stringColumn));
                continue;
            }

            // Check for single-character tokens
            switch (currentChar) {
                case '{':
                    tokens.add(new Token(TokenType.LBRACE, "{", line, column));
                    advance();
                    break;
                case '}':
                    tokens.add(new Token(TokenType.RBRACE, "}", line, column));
                    advance();
                    break;
                case '(':
                    tokens.add(new Token(TokenType.LPAREN, "(", line, column));
                    advance();
                    break;
                case ')':
                    tokens.add(new Token(TokenType.RPAREN, ")", line, column));
                    advance();
                    break;
                case ',':
                    tokens.add(new Token(TokenType.COMMA, ",", line, column));
                    advance();
                    break;
                case ':':
                    tokens.add(new Token(TokenType.COLON, ":", line, column));
                    advance();
                    break;
                default:
                    // Error handling - unrecognized character
                    addError(String.valueOf(currentChar), line, column);
                    advance();
                    break;
            }
        }
        
        // Add EOF token
        tokens.add(new Token(TokenType.EOF, "", line, column));
        return tokens;
    }

    private void addError(String error, int line, int column) {
        errors.add(new ErrorEntry(error, line, column));
    }

    public List<ErrorEntry> getErrors() {
        return errors;
    }
}