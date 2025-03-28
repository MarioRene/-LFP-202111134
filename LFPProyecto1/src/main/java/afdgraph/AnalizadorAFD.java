package main.java.afdgraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnalizadorAFD {
    private static ArrayList<Token> todosTokens = new ArrayList<>();
    private static ArrayList<Error> todosErrores = new ArrayList<>();

    public static Map<String, AFD> analizarArchivo(File archivo, boolean mantenerAnteriores) throws Exception {
        if (!mantenerAnteriores) {
            todosTokens.clear();
            todosErrores.clear();
        }
        
        BufferedReader lector = new BufferedReader(new FileReader(archivo));
        StringBuilder contenido = new StringBuilder();
        String linea;
        
        while ((linea = lector.readLine()) != null) {
            contenido.append(linea).append("\n");
        }
        lector.close();
        
        Scanner scanner = new Scanner(contenido.toString());
        ArrayList<Token> tokensArchivo = new ArrayList<>();
        Token token;
        do {
            token = scanner.siguiente_token();
            if (token.tipo != TOK.EOF) {
                tokensArchivo.add(token);
            }
        } while (token.tipo != TOK.EOF);
        
        todosTokens.addAll(tokensArchivo);
        todosErrores.addAll(scanner.errors);
        
        Map<String, AFD> nuevosAutomatas = construirAFD(tokensArchivo, scanner.errors);
        
        return nuevosAutomatas;
    }
    
    private static Map<String, AFD> construirAFD(ArrayList<Token> tokens, ArrayList<Error> errores) {
        Map<String, AFD> automatas = new HashMap<>();
        AFD afdActual = null;
        int i = 0;
        
        while (i < tokens.size()) {
            Token token = tokens.get(i);
            
            if (token.tipo == TOK.TK_identificador && i+1 < tokens.size() && 
                tokens.get(i+1).tipo == TOK.TK_dosPuntos && i+2 < tokens.size() && 
                tokens.get(i+2).tipo == TOK.TK_llaveIzq) {
                
                String nombreAFD = token.lexema;
                afdActual = new AFD(nombreAFD);
                automatas.put(nombreAFD, afdActual);
                i += 3;
                continue;
            }
            
            if (afdActual != null) {
                // Procesar descripción
                if (token.tipo == TOK.KW_descripcion && i+1 < tokens.size() && 
                    tokens.get(i+1).tipo == TOK.TK_dosPuntos && i+2 < tokens.size() && 
                    tokens.get(i+2).tipo == TOK.TK_cadena) {
                    
                    afdActual.setDescripcion(tokens.get(i+2).lexema.substring(1, tokens.get(i+2).lexema.length()-1));
                    i += 3;
                    continue;
                }
                
                // Procesar estados
                if (token.tipo == TOK.KW_estados && i+1 < tokens.size() && 
                    tokens.get(i+1).tipo == TOK.TK_dosPuntos && i+2 < tokens.size() && 
                    tokens.get(i+2).tipo == TOK.TK_corcheteIzq) {
                    
                    i += 3; // Saltar 'estados', : y '['
                    while (i < tokens.size() && tokens.get(i).tipo != TOK.TK_corcheteDer) {
                        if (tokens.get(i).tipo == TOK.TK_identificador) {
                            afdActual.agregarEstado(tokens.get(i).lexema);
                        }
                        i++;
                        if (i < tokens.size() && tokens.get(i).tipo == TOK.TK_coma) i++;
                    }
                    if (i < tokens.size() && tokens.get(i).tipo == TOK.TK_corcheteDer) i++;
                    continue;
                }
                
                // Procesar alfabeto
                if (token.tipo == TOK.KW_alfabeto && i+1 < tokens.size() && 
                    tokens.get(i+1).tipo == TOK.TK_dosPuntos && i+2 < tokens.size() && 
                    tokens.get(i+2).tipo == TOK.TK_corcheteIzq) {
                    
                    i += 3; // Saltar 'alfabeto', : y '['
                    while (i < tokens.size() && tokens.get(i).tipo != TOK.TK_corcheteDer) {
                        if (tokens.get(i).tipo == TOK.TK_cadena) {
                            String simbolo = tokens.get(i).lexema.substring(1, tokens.get(i).lexema.length()-1);
                            afdActual.agregarSimboloAlfabeto(simbolo);
                        } else if (tokens.get(i).tipo == TOK.TK_identificador && tokens.get(i).lexema.equals("digit")) {
                            afdActual.agregarSimboloAlfabeto("digit");
                        }
                        i++;
                        if (i < tokens.size() && tokens.get(i).tipo == TOK.TK_coma) i++;
                    }
                    if (i < tokens.size() && tokens.get(i).tipo == TOK.TK_corcheteDer) i++;
                    continue;
                }
                
                // Procesar estado inicial
                if (token.tipo == TOK.KW_inicial && i+1 < tokens.size() && 
                    tokens.get(i+1).tipo == TOK.TK_dosPuntos && i+2 < tokens.size() && 
                    tokens.get(i+2).tipo == TOK.TK_identificador) {
                    
                    afdActual.setEstadoInicial(tokens.get(i+2).lexema);
                    i += 3;
                    continue;
                }
                
                // Procesar estados finales
                if (token.tipo == TOK.KW_finales && i+1 < tokens.size() && 
                    tokens.get(i+1).tipo == TOK.TK_dosPuntos && i+2 < tokens.size() && 
                    tokens.get(i+2).tipo == TOK.TK_corcheteIzq) {
                    
                    i += 3; // Saltar 'finales', : y '['
                    while (i < tokens.size() && tokens.get(i).tipo != TOK.TK_corcheteDer) {
                        if (tokens.get(i).tipo == TOK.TK_identificador) {
                            afdActual.agregarEstadoFinal(tokens.get(i).lexema);
                        }
                        i++;
                        if (i < tokens.size() && tokens.get(i).tipo == TOK.TK_coma) i++;
                    }
                    if (i < tokens.size() && tokens.get(i).tipo == TOK.TK_corcheteDer) i++;
                    continue;
                }
                
                // Procesar transiciones
                if (token.tipo == TOK.KW_transiciones && i+1 < tokens.size() && 
                    tokens.get(i+1).tipo == TOK.TK_dosPuntos && i+2 < tokens.size() && 
                    tokens.get(i+2).tipo == TOK.TK_llaveIzq) {
                    
                    i += 3; // Saltar 'transiciones', : y '{'
                    while (i < tokens.size() && tokens.get(i).tipo != TOK.TK_llaveDer) {
                        // Formato: estadoOrigen = ( "simbolo" -> estadoDestino, ... )
                        if (tokens.get(i).tipo == TOK.TK_identificador && 
                            i+2 < tokens.size() && 
                            tokens.get(i+1).tipo == TOK.TK_igual &&
                            tokens.get(i+2).tipo == TOK.TK_parentesisIzq) {
                            
                            String estadoOrigen = tokens.get(i).lexema;
                            i += 3; // Saltar identificador, '=' y '('
                            
                            while (i < tokens.size() && tokens.get(i).tipo != TOK.TK_parentesisDer) {
                                if (tokens.get(i).tipo == TOK.TK_cadena || 
                                    (tokens.get(i).tipo == TOK.TK_identificador && tokens.get(i).lexema.equals("digit"))) {
                                    
                                    String simbolo = tokens.get(i).tipo == TOK.TK_cadena ? 
                                        tokens.get(i).lexema.substring(1, tokens.get(i).lexema.length()-1) : 
                                        tokens.get(i).lexema;
                                    
                                    if (i+2 < tokens.size() && 
                                        tokens.get(i+1).tipo == TOK.TK_flecha &&
                                        tokens.get(i+2).tipo == TOK.TK_identificador) {
                                        
                                        String estadoDestino = tokens.get(i+2).lexema;
                                        afdActual.agregarTransicion(estadoOrigen, simbolo, estadoDestino);
                                        i += 3;
                                        
                                        if (i < tokens.size() && tokens.get(i).tipo == TOK.TK_coma) {
                                            i++;
                                        }
                                    }
                                } else {
                                    i++;
                                }
                            }
                            if (i < tokens.size() && tokens.get(i).tipo == TOK.TK_parentesisDer) i++;
                        } else {
                            i++;
                        }
                    }
                    if (i < tokens.size() && tokens.get(i).tipo == TOK.TK_llaveDer) i++;
                    continue;
                }
                
                // Cierre de definición de AFD
                if (token.tipo == TOK.TK_llaveDer) {
                    // Verificar que el AFD esté completo
                    if (afdActual.getEstadoInicial() == null) {
                        errores.add(new Error(token.linea, token.columna, 
                            "AFD '" + afdActual.getNombre() + "' no tiene estado inicial definido"));
                    }
                    if (afdActual.getEstadosFinales().isEmpty()) {
                        errores.add(new Error(token.linea, token.columna, 
                            "AFD '" + afdActual.getNombre() + "' no tiene estados finales definidos"));
                    }
                    if (afdActual.getTransiciones().isEmpty()) {
                        errores.add(new Error(token.linea, token.columna, 
                            "AFD '" + afdActual.getNombre() + "' no tiene transiciones definidas"));
                    }
                    
                    afdActual = null;
                    i++;
                    continue;
                }
            }
            
            i++;
        }
        
        return automatas;
    }
    
    public static ArrayList<Token> getTodosTokens() {
        return todosTokens;
    }
    
    public static ArrayList<Error> getTodosErrores() {
        return todosErrores;
    }
    
    public static void limpiarDatos() {
        todosTokens.clear();
        todosErrores.clear();
    }
}
