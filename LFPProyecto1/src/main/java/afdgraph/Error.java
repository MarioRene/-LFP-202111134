package main.java.afdgraph;

public class Error {
    public int line;
    public int column;
    public String message;

    public Error(int line, int column, String message) {
        this.line = line;
        this.column = column;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Error en " + line + ":" + column + " - " + message;
    }
}