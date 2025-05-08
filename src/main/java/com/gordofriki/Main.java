package com.gordofriki;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Main {
    public static void main(String[] args) {
        // Crear un grafo simple no dirigido
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        // Añadir vértices
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");

        // Añadir aristas (conexiones entre vértices)
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "C");
        graph.addEdge("C", "D");

        // Aquí puedes agregar más vértices y aristas para crear un grafo más complejo

        // Verificar si el grafo es outerplanar
        boolean isOuterplanar = checkOuterplanarity(graph);

        // Imprimir el resultado
        System.out.println("El grafo es outerplanar: " + isOuterplanar);
    }

    // Método básico para verificar outerplanaridad
    private static boolean checkOuterplanarity(Graph<String, DefaultEdge> graph) {
        // Este es un ejemplo muy simplificado. Para verificar correctamente la outerplanaridad,
        // se necesitaría un algoritmo específico para detectar subgrafos K₄ o K₃,₃.

        // Supongamos que en este caso solo hacemos una validación básica.
        if (graph.vertexSet().size() > 4) {
            // Comprobar si tiene subgrafos K₄ o K₃,₃ es un paso avanzado
            return false;  // Consideramos que no es outerplanar si tiene demasiados vértices
        }

        // Este es un paso muy básico; se podría hacer una verificación más avanzada
        return true;
    }
}