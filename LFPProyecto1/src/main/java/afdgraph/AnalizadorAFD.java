package main.java.afdgraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class AnalizadorAFD {
    public static Map<String, AFD> analizarArchivo(File archivo) throws Exception {
        Map<String, AFD> automatas = new HashMap<>();
        BufferedReader lector = new BufferedReader(new FileReader(archivo));
        String linea;
        AFD afdActual = null;
        boolean enTransiciones = false;

        while ((linea = lector.readLine()) != null) {
            linea = linea.trim();
            if (linea.isEmpty()) continue;

            // Detectar nuevo AFD
            if (linea.endsWith("{")) {
                int index = linea.indexOf(':');
                if (index != -1) {
                    String nombreAFD = linea.substring(0, index).trim();
                    if (!nombreAFD.equalsIgnoreCase("transiciones")) {
                        afdActual = new AFD(nombreAFD);
                        automatas.put(nombreAFD, afdActual);
                        enTransiciones = false;
                    }
                }
                continue;
            }

            if (afdActual != null) {
                // Analizar descripción
                if (linea.startsWith("descripcion:")) {
                    int start = linea.indexOf('"') + 1;
                    int end = linea.lastIndexOf('"');
                    if (start > 0 && end > start) {
                        afdActual.setDescripcion(linea.substring(start, end));
                    }
                }
                // Analizar estados
                else if (linea.startsWith("estados:")) {
                    int start = linea.indexOf('[') + 1;
                    int end = linea.indexOf(']');
                    if (start > 0 && end > start) {
                        String estadosStr = linea.substring(start, end);
                        String[] estados = estadosStr.split(",");
                        for (String estado : estados) {
                            afdActual.agregarEstado(estado.trim());
                        }
                    }
                }
                // Analizar alfabeto
                else if (linea.startsWith("alfabeto:")) {
                    int start = linea.indexOf('[') + 1;
                    int end = linea.indexOf(']');
                    if (start > 0 && end > start) {
                        String alfabetoStr = linea.substring(start, end);
                        String[] simbolos = alfabetoStr.split(",");
                        for (String simbolo : simbolos) {
                            simbolo = simbolo.trim().replaceAll("^\"|\"$", "");
                            afdActual.agregarSimboloAlfabeto(simbolo);
                        }
                    }
                }
                // Analizar estado inicial
                else if (linea.startsWith("inicial:")) {
                    String estadoInicial = linea.substring(linea.indexOf(':') + 1).trim();
                    afdActual.setEstadoInicial(estadoInicial);
                }
                // Analizar estados finales
                else if (linea.startsWith("finales:")) {
                    int start = linea.indexOf('[') + 1;
                    int end = linea.indexOf(']');
                    if (start > 0 && end > start) {
                        String finalesStr = linea.substring(start, end);
                        String[] finales = finalesStr.split(",");
                        for (String estadoFinal : finales) {
                            afdActual.agregarEstadoFinal(estadoFinal.trim());
                        }
                    }
                }
                // Analizar transiciones
                else if (linea.contains("=") && linea.contains("(") && linea.contains(")")) {
                    String estadoOrigen = linea.substring(0, linea.indexOf('=')).trim();
                    String transicionesStr = linea.substring(linea.indexOf('(') + 1, linea.indexOf(')'));

                    // Procesar cada transición individualmente
                    int pos = 0;
                    while (pos < transicionesStr.length()) {
                        // Buscar comilla de inicio
                        int startQuote = transicionesStr.indexOf('"', pos);
                        if (startQuote == -1) break;
                        
                        // Buscar comilla de fin
                        int endQuote = transicionesStr.indexOf('"', startQuote + 1);
                        if (endQuote == -1) break;
                        
                        String simbolo = transicionesStr.substring(startQuote + 1, endQuote);
                        
                        // Buscar flecha
                        int arrowPos = transicionesStr.indexOf("->", endQuote);
                        if (arrowPos == -1) break;
                        
                        // Buscar siguiente estado (puede ser al final de la línea o seguido de coma)
                        int estadoStart = arrowPos + 2;
                        int estadoEnd = transicionesStr.indexOf(',', estadoStart);
                        if (estadoEnd == -1) {
                            estadoEnd = transicionesStr.length();
                        }
                        
                        String estadoDestino = transicionesStr.substring(estadoStart, estadoEnd).trim();
                        afdActual.agregarTransicion(estadoOrigen, simbolo, estadoDestino);
                        
                        pos = estadoEnd + 1;
                    }
                }
            }
        }
        
        lector.close();
        return automatas;
    }
}
