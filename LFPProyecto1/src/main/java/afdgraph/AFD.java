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
        this.nombre = nombre != null ? nombre.trim() : "AFD_SIN_NOMBRE";
        this.estados = new ArrayList<>();
        this.alfabeto = new ArrayList<>();
        this.estadosFinales = new ArrayList<>();
        this.transiciones = new HashMap<>();
    }

    // Getters y setters
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public List<String> getEstados() { return estados; }
    public List<String> getAlfabeto() { return alfabeto; }
    public String getEstadoInicial() { return estadoInicial; }
    public List<String> getEstadosFinales() { return estadosFinales; }
    public Map<String, Map<String, String>> getTransiciones() { return transiciones; }

    public void agregarEstado(String estado) {
        if (estado == null) return;
        String estadoNormalizado = estado.trim();
        if (!estadoNormalizado.isEmpty() && !estados.contains(estadoNormalizado)) {
            estados.add(estadoNormalizado);
        }
    }

    public void agregarSimboloAlfabeto(String simbolo) {
        if (simbolo == null) return;
        String simboloNormalizado = simbolo.trim();
        if (!simboloNormalizado.isEmpty() && !alfabeto.contains(simboloNormalizado)) {
            alfabeto.add(simboloNormalizado);
        }
    }

    public void agregarTransicion(String estadoOrigen, String simbolo, String estadoDestino) {
        if (estadoOrigen == null || simbolo == null || estadoDestino == null) return;
        
        String origen = estadoOrigen.trim();
        String simb = simbolo.trim();
        String destino = estadoDestino.trim();
        
        if (!origen.isEmpty() && !simb.isEmpty() && !destino.isEmpty()) {
            transiciones.putIfAbsent(origen, new HashMap<>());
            transiciones.get(origen).put(simb, destino);
        }
    }
    
    public void setEstadoInicial(String estadoInicial) {
        this.estadoInicial = estadoInicial.trim();
    }

    public void agregarEstadoFinal(String estadoFinal) {
        String estadoFinalNormalizado = estadoFinal.trim();
        if (!estadosFinales.contains(estadoFinalNormalizado)) {
            estadosFinales.add(estadoFinalNormalizado);
        }
    }

    @Override
    public String toString() {
        return nombre;
    }
}
