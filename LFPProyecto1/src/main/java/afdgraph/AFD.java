package main.java.afdgraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AFD {
    private String nombre;
    private String descripcion;
    private List<String> estados;
    private List<String> alfabeto;
    private String estadoInicial;
    private List<String> estadosFinales;
    private Map<String, Map<String, String>> transiciones;

    public AFD(String nombre) {
        this.nombre = nombre;
        this.estados = new ArrayList<>();
        this.alfabeto = new ArrayList<>();
        this.estadosFinales = new ArrayList<>();
        this.transiciones = new HashMap<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<String> getEstados() {
        return estados;
    }

    public void agregarEstado(String estado) {
        if (!estados.contains(estado)) {
            estados.add(estado);
        }
    }

    public List<String> getAlfabeto() {
        return alfabeto;
    }

    public void agregarSimboloAlfabeto(String simbolo) {
        if (!alfabeto.contains(simbolo)) {
            alfabeto.add(simbolo);
        }
    }

    public String getEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(String estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public List<String> getEstadosFinales() {
        return estadosFinales;
    }

    public void agregarEstadoFinal(String estadoFinal) {
        if (!estadosFinales.contains(estadoFinal)) {
            estadosFinales.add(estadoFinal);
        }
    }

    public Map<String, Map<String, String>> getTransiciones() {
        return transiciones;
    }

    public void agregarTransicion(String estadoOrigen, String simbolo, String estadoDestino) {
        transiciones.putIfAbsent(estadoOrigen, new HashMap<>());
        transiciones.get(estadoOrigen).put(simbolo, estadoDestino);
    }

    public boolean validarCadena(String cadena) {
        if (estadoInicial == null || estadosFinales.isEmpty()) {
            return false;
        }

        String estadoActual = estadoInicial;
        
        for (int i = 0; i < cadena.length(); i++) {
            String simbolo = String.valueOf(cadena.charAt(i));
            
            if (!alfabeto.contains(simbolo)) {
                return false;
            }

            Map<String, String> transicionesEstado = transiciones.get(estadoActual);
            if (transicionesEstado == null) {
                return false;
            }

            String siguienteEstado = transicionesEstado.get(simbolo);
            if (siguienteEstado == null) {
                return false;
            }

            estadoActual = siguienteEstado;
        }

        return estadosFinales.contains(estadoActual);
    }

    @Override
    public String toString() {
        return "AFD{" +
                "nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", estados=" + estados +
                ", alfabeto=" + alfabeto +
                ", estadoInicial='" + estadoInicial + '\'' +
                ", estadosFinales=" + estadosFinales +
                ", transiciones=" + transiciones +
                '}';
    }
}
