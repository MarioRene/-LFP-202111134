package main.java.afdgraph;

public class ErrorLexico {
    private String caracter;
    private int linea;
    private int columna;

    public ErrorLexico(String caracter, int linea, int columna) {
        this.caracter = caracter;
        this.linea = linea;
        this.columna = columna;
    }

    // Getters
    public String getCaracter() { return caracter; }
    public int getLinea() { return linea; }
    public int getColumna() { return columna; }
}
