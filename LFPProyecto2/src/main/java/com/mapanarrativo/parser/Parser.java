package main.java.com.mapanarrativo.parser;

import main.java.com.mapanarrativo.models.*;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int position;
    private Token currentToken;
    private List<ErrorEntry> errors;
    private List<World> worlds;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.position = 0;
        this.currentToken = tokens.get(0);
        this.errors = new ArrayList<>();
        this.worlds = new ArrayList<>();
    }

    private void advance() {
        position++;
        if (position < tokens.size()) {
            currentToken = tokens.get(position);
        }
    }

    private boolean match(TokenType type) {
        if (currentToken.getType() == type) {
            advance();
            return true;
        }
        return false;
    }

    private void expect(TokenType type) {
        if (match(type)) {
            return;
        }
        addError("Expected " + type + ", found " + currentToken.getType(), 
                currentToken.getLine(), currentToken.getColumn());
    }

    private void addError(String message, int line, int column) {
        errors.add(new ErrorEntry(message, line, column));
    }

    public List<World> parse() {
        while (currentToken.getType() != TokenType.EOF) {
            if (currentToken.getType() == TokenType.WORLD) {
                worlds.add(parseWorld());
            } else {
                addError("Expected 'world', found " + currentToken.getType(), 
                        currentToken.getLine(), currentToken.getColumn());
                advance(); // Skip the unexpected token
            }
        }
        return worlds;
    }

    private World parseWorld() {
        expect(TokenType.WORLD);
        
        // Get world name
        String name = "";
        if (currentToken.getType() == TokenType.STRING) {
            name = currentToken.getLexeme().replaceAll("\"", "");
            advance();
        } else {
            addError("Expected world name string", currentToken.getLine(), currentToken.getColumn());
        }
        
        expect(TokenType.LBRACE);
        
        World world = new World(name);
        
        // Parse world definitions
        while (currentToken.getType() != TokenType.RBRACE && 
               currentToken.getType() != TokenType.EOF) {
            
            switch (currentToken.getType()) {
                case PLACE:
                    Place place = parsePlace();
                    world.addPlace(place);
                    break;
                case CONNECT:
                    Connection connection = parseConnection();
                    world.addConnection(connection);
                    break;
                case OBJECT:
                    MapObject object = parseObject();
                    world.addObject(object);
                    break;
                default:
                    addError("Unexpected token " + currentToken.getType(), 
                            currentToken.getLine(), currentToken.getColumn());
                    advance(); // Skip the unexpected token
                    break;
            }
        }
        
        expect(TokenType.RBRACE);
        
        // Check for comma after world block
        if (currentToken.getType() == TokenType.COMMA) {
            advance();
        }
        
        return world;
    }

    private Place parsePlace() {
        expect(TokenType.PLACE);
        
        String name = "";
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            name = currentToken.getLexeme();
            advance();
        } else {
            addError("Expected place name", currentToken.getLine(), currentToken.getColumn());
        }
        
        expect(TokenType.COLON);
        
        String type = "";
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            type = currentToken.getLexeme();
            advance();
        } else {
            addError("Expected place type", currentToken.getLine(), currentToken.getColumn());
        }
        
        expect(TokenType.AT);
        expect(TokenType.LPAREN);
        
        int x = 0;
        if (currentToken.getType() == TokenType.NUMBER) {
            x = Integer.parseInt(currentToken.getLexeme());
            advance();
        } else {
            addError("Expected x coordinate", currentToken.getLine(), currentToken.getColumn());
        }
        
        expect(TokenType.COMMA);
        
        int y = 0;
        if (currentToken.getType() == TokenType.NUMBER) {
            y = Integer.parseInt(currentToken.getLexeme());
            advance();
        } else {
            addError("Expected y coordinate", currentToken.getLine(), currentToken.getColumn());
        }
        
        expect(TokenType.RPAREN);
        
        return new Place(name, type, x, y);
    }

    private Connection parseConnection() {
        expect(TokenType.CONNECT);
        
        String from = "";
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            from = currentToken.getLexeme();
            advance();
        } else {
            addError("Expected source place", currentToken.getLine(), currentToken.getColumn());
        }
        
        expect(TokenType.TO);
        
        String to = "";
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            to = currentToken.getLexeme();
            advance();
        } else {
            addError("Expected destination place", currentToken.getLine(), currentToken.getColumn());
        }
        
        expect(TokenType.WITH);
        
        String type = "";
        if (currentToken.getType() == TokenType.STRING) {
            type = currentToken.getLexeme().replaceAll("\"", "");
            advance();
        } else {
            addError("Expected connection type", currentToken.getLine(), currentToken.getColumn());
        }
        
        return new Connection(from, to, type);
    }

    private MapObject parseObject() {
        expect(TokenType.OBJECT);
        
        String name = "";
        if (currentToken.getType() == TokenType.STRING) {
            name = currentToken.getLexeme().replaceAll("\"", "");
            advance();
        } else {
            addError("Expected object name", currentToken.getLine(), currentToken.getColumn());
        }
        
        expect(TokenType.COLON);
        
        String type = "";
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            type = currentToken.getLexeme();
            advance();
        } else {
            addError("Expected object type", currentToken.getLine(), currentToken.getColumn());
        }
        
        expect(TokenType.AT);
        
        MapObject object;
        
        if (currentToken.getType() == TokenType.LPAREN) {
            // Object with coordinates
            advance();
            
            int x = 0;
            if (currentToken.getType() == TokenType.NUMBER) {
                x = Integer.parseInt(currentToken.getLexeme());
                advance();
            } else {
                addError("Expected x coordinate", currentToken.getLine(), currentToken.getColumn());
            }
            
            expect(TokenType.COMMA);
            
            int y = 0;
            if (currentToken.getType() == TokenType.NUMBER) {
                y = Integer.parseInt(currentToken.getLexeme());
                advance();
            } else {
                addError("Expected y coordinate", currentToken.getLine(), currentToken.getColumn());
            }
            
            expect(TokenType.RPAREN);
            
            object = new MapObject(name, type, x, y);
        } else if (currentToken.getType() == TokenType.IDENTIFIER) {
            // Object at a place
            String place = currentToken.getLexeme();
            advance();
            
            object = new MapObject(name, type, place);
        } else {
            addError("Expected place name or coordinates", 
                    currentToken.getLine(), currentToken.getColumn());
            object = new MapObject(name, type, "");
        }
        
        return object;
    }

    public List<ErrorEntry> getErrors() {
        return errors;
    }
}