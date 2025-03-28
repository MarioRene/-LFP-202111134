package main.java.afdgraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Scanner {
    private String input;
    private int pos = 0;
    private int line = 1;
    private int column = 1;
    public ArrayList<Error> errors = new ArrayList<>();

    private static final Map<String, TOK> keywords = new HashMap<>();
    static {
        keywords.put("descripcion", TOK.KW_descripcion);
        keywords.put("estados", TOK.KW_estados);
        keywords.put("alfabeto", TOK.KW_alfabeto);
        keywords.put("inicial", TOK.KW_inicial);
        keywords.put("finales", TOK.KW_finales);
        keywords.put("transiciones", TOK.KW_transiciones);
    }

    public Scanner(String input) {
        this.input = input;
    }

    public Token siguiente_token() {
        // Ignorar espacios en blanco
        while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
            if (input.charAt(pos) == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
            pos++;
        }

        if (pos >= input.length()) {
            return new Token(TOK.EOF, line, column, "");
        }

        char current = input.charAt(pos);

        // Identificadores y palabras reservadas
        if (Character.isLetter(current)) {
            return scanIdentifier();
        }

        // Números (para digit)
        if (Character.isDigit(current)) {
            return scanNumber();
        }

        // Cadenas
        if (current == '"') {
            return scanString();
        }

        // Símbolos compuestos (->)
        if (current == '-' && pos + 1 < input.length() && input.charAt(pos + 1) == '>') {
            Token token = new Token(TOK.TK_flecha, line, column, "->");
            pos += 2;
            column += 2;
            return token;
        }

        // Símbolos individuales
        TOK symbolToken = TOK.fromSymbol(String.valueOf(current));
        if (symbolToken != null) {
            Token token = new Token(symbolToken, line, column, String.valueOf(current));
            pos++;
            column++;
            return token;
        }

        // Caracteres no reconocidos (marcar como error pero continuar)
        if (!Character.isWhitespace(current)) {
            errors.add(new Error(line, column, "Carácter no reconocido: '" + current + "'"));
        }
        pos++;
        column++;
        return siguiente_token(); // Saltar carácter no válido
    }

    private Token scanIdentifier() {
        int start = pos;
        int startLine = line;
        int startColumn = column;

        while (pos < input.length() && (Character.isLetterOrDigit(input.charAt(pos)) || input.charAt(pos) == '_')) {
            pos++;
            column++;
        }

        String lexeme = input.substring(start, pos);
        TOK tokenType = keywords.getOrDefault(lexeme, TOK.TK_identificador);
        return new Token(tokenType, startLine, startColumn, lexeme);
    }

    private Token scanNumber() {
        int start = pos;
        int startLine = line;
        int startColumn = column;

        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            pos++;
            column++;
        }

        return new Token(TOK.TK_numero, startLine, startColumn, input.substring(start, pos));
    }

    private Token scanString() {
        int startLine = line;
        int startColumn = column;
        StringBuilder sb = new StringBuilder();

        sb.append(input.charAt(pos)); // Añadir la comilla inicial
        pos++;
        column++;

        boolean escape = false;
        boolean closed = false;

        while (pos < input.length()) {
            char current = input.charAt(pos);
            sb.append(current);

            if (current == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }

            pos++;

            if (current == '"' && !escape) {
                closed = true;
                break;
            }

            escape = current == '\\';
        }

        if (!closed) {
            errors.add(new Error(startLine, startColumn, "Cadena no cerrada"));
        }

        return new Token(TOK.TK_cadena, startLine, startColumn, sb.toString());
    }
}
