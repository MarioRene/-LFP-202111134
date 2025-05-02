package main.java.com.mapanarrativo.models;

public class ErrorEntry {
    private String error;
    private int line;
    private int column;

    public ErrorEntry(String error, int line, int column) {
        this.error = error;
        this.line = line;
        this.column = column;
    }

    public String getError() {
        return error;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "Error: '" + error + "' at line " + line + ", column " + column;
    }
}