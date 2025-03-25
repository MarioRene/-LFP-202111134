package main.java.afdgraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorAFD {
    private List<Token> tokens;
    private List<ErrorLexico> errores;
    private AFD afdActual;

    public AnalizadorAFD() {
        this.tokens = new ArrayList<>();
        this.errores = new ArrayList<>();
    }

    public AFD analizarArchivo(File archivo) throws IOException {
        tokens.clear();
        errores.clear();
        BufferedReader lector = new BufferedReader(new FileReader(archivo));
        String linea;
        int numLinea = 0;
        boolean enTransiciones = false;
        AFD afd = null;

        while ((linea = lector.readLine()) != null) {
            numLinea++;
            linea = linea.trim();
            if (linea.isEmpty()) continue;

            // Detección de nuevo AFD
            if (linea.matches("^\\s*\\w+\\s*:\\s*\\{.*")) {
                String[] partes = linea.split(":");
                String nombreAFD = partes[0].trim();
                afd = new AFD(nombreAFD);
                continue;
            }

            if (afd == null) continue;

            // Procesamiento de componentes
            if (linea.contains("description:")) {
                String descripcion = linea.split(":")[1].replaceAll("\"|,", "").trim();
                afd.setDescripcion(descripcion);
            } 
            else if (linea.contains("estados:")) {
                procesarLista(afd, linea, "estados", true);
            }
            else if (linea.contains("alfabeto:")) {
                procesarLista(afd, linea, "alfabeto", false);
            }
            else if (linea.contains("inicial:")) {
                String estadoInicial = linea.split(":")[1].replace(",", "").trim();
                afd.setEstadoInicial(estadoInicial);
            }
            else if (linea.contains("finales:")) {
                procesarLista(afd, linea, "finales", true);
            }
            else if (linea.contains("transiciones:")) {
                enTransiciones = true;
            }
            else if (enTransiciones && linea.matches("^\\w+\\s*[:=]\\s*\\(.*\\)")) {
                procesarTransicion(afd, linea);
            }
        }
        lector.close();
        this.afdActual = afd;
        return afd;
    }

    private void procesarLista(AFD afd, String linea, String tipo, boolean esEstado) {
        String contenido = linea.substring(linea.indexOf("[") + 1, linea.indexOf("]"));
        String[] elementos = contenido.split(",\\s*");
        
        for (String elemento : elementos) {
            elemento = elemento.trim().replaceAll("\"", "");
            if (elemento.isEmpty()) continue;
            
            if (esEstado) {
                afd.agregarEstado(elemento);
            } else {
                afd.agregarSimboloAlfabeto(elemento);
            }
        }
    }

    private void procesarTransicion(AFD afd, String linea) {
        String[] partes = linea.split("\\s*[:=]\\s*");
        String estadoOrigen = partes[0].trim();
        
        String transicionesStr = linea.substring(linea.indexOf('(') + 1, linea.lastIndexOf(')'));
        String[] transiciones = transicionesStr.split(",\\s*");
        
        for (String transicion : transiciones) {
            try {
                String[] partesTrans = transicion.split("->");
                String simbolo = partesTrans[0].replaceAll("\"", "").trim();
                String estadoDestino = partesTrans[1].trim();
                
                afd.agregarTransicion(estadoOrigen, simbolo, estadoDestino);
            } catch (Exception e) {
                errores.add(new ErrorLexico(transicion, 0, 0));
            }
        }
    }

    public List<Token> getTokens() { return tokens; }
    public List<ErrorLexico> getErrores() { return errores; }
    public String getTextoGrafo() {
        if (afdActual == null) return "No hay autómata cargado";
        
        StringBuilder sb = new StringBuilder();
        sb.append("Autómata: ").append(afdActual.getNombre()).append("\n");
        sb.append("Descripción: ").append(afdActual.getDescripcion()).append("\n\n");
        sb.append("Estados: ").append(afdActual.getEstados()).append("\n");
        sb.append("Alfabeto: ").append(afdActual.getAlfabeto()).append("\n");
        sb.append("Estado inicial: ").append(afdActual.getEstadoInicial()).append("\n");
        sb.append("Estados finales: ").append(afdActual.getEstadosFinales()).append("\n\n");
        sb.append("Transiciones:\n");
        
        afdActual.getTransiciones().forEach((origen, trans) -> {
            trans.forEach((simbolo, destino) -> {
                sb.append(origen).append(" --").append(simbolo).append("--> ").append(destino).append("\n");
            });
        });
        
        return sb.toString();
    }
}
