package main.java.afdgraph;

public enum TOK {
    KW_AFD("KW_AFD"),
    KW_descripcion("KW_descripcion"),
    KW_estados("KW_estados"),
    KW_alfabeto("KW_alfabeto"),
    KW_inicial("KW_inicial"),
    KW_finales("KW_finales"),
    KW_transiciones("KW_transiciones"),
    TK_identificador("TK_identificador"),
    TK_cadena("TK_cadena"),
    TK_flecha("TK_flecha"),
    TK_llaveIzq("TK_llaveIzq"),
    TK_llaveDer("TK_llaveDer"),
    TK_corcheteIzq("TK_corcheteIzq"),
    TK_corcheteDer("TK_corcheteDer"),
    TK_dosPuntos("TK_dosPuntos"),
    TK_coma("TK_coma"),
    TK_igual("TK_igual"),
    TK_parentesisIzq("TK_parentesisIzq"),
    TK_parentesisDer("TK_parentesisDer"),
    EOF("EOF");

    private String nombre;

    private TOK(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public static TOK fromSymbol(String symbol) {
        switch (symbol) {
            case "{": return TK_llaveIzq;
            case "}": return TK_llaveDer;
            case "[": return TK_corcheteIzq;
            case "]": return TK_corcheteDer;
            case ":": return TK_dosPuntos;
            case ",": return TK_coma;
            case "=": return TK_igual;
            case "(": return TK_parentesisIzq;
            case ")": return TK_parentesisDer;
            case "->": return TK_flecha;
            default: return null;
        }
    }
}
