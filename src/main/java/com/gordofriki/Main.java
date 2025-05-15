package com.gordofriki;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import java.util.*;
import org.jgrapht.graph.*;


public class Main {

    public static boolean isOuterplanar(Graph<String, DefaultEdge> graph) {
        // Verifica que no contenga un K4 ni un K2,3 como subgrafo inducido
        return !containsK4(graph) && !containsK23(graph);
    }

    private static boolean containsK4(Graph<String, DefaultEdge> graph) {
        List<String> vertices = new ArrayList<>(graph.vertexSet());

        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                for (int k = j + 1; k < vertices.size(); k++) {
                    for (int l = k + 1; l < vertices.size(); l++) {
                        Set<String> subset = Set.of(vertices.get(i), vertices.get(j), vertices.get(k), vertices.get(l));
                        if (isK4(graph, subset)) return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isK4(Graph<String, DefaultEdge> graph, Set<String> subset) {
        List<String> nodes = new ArrayList<>(subset);
        int edgeCount = 0;

        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                if (graph.containsEdge(nodes.get(i), nodes.get(j))) {
                    edgeCount++;
                }
            }
        }

        return edgeCount == 6; // K4 tiene 6 aristas completamente conectadas
    }

    private static boolean containsK23(Graph<String, DefaultEdge> graph) {
        List<String> vertices = new ArrayList<>(graph.vertexSet());

        for (int a = 0; a < vertices.size(); a++) {
            for (int b = a + 1; b < vertices.size(); b++) {
                for (int c = 0; c < vertices.size(); c++) {
                    if (c == a || c == b) continue;
                    for (int d = c + 1; d < vertices.size(); d++) {
                        if (d == a || d == b) continue;
                        for (int e = d + 1; e < vertices.size(); e++) {
                            if (e == a || e == b) continue;

                            List<String> partA = List.of(vertices.get(a), vertices.get(b));
                            List<String> partB = List.of(vertices.get(c), vertices.get(d), vertices.get(e));

                            if (isK23(graph, partA, partB)) return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean isK23(Graph<String, DefaultEdge> graph, List<String> partA, List<String> partB) {
        for (String u : partA) {
            for (String v : partB) {
                if (!graph.containsEdge(u, v)) {
                    return false;
                }
            }
        }

        // Asegurarse de que no hay aristas dentro de cada parte
        for (int i = 0; i < partA.size(); i++) {
            for (int j = i + 1; j < partA.size(); j++) {
                if (graph.containsEdge(partA.get(i), partA.get(j))) return false;
            }
        }

        for (int i = 0; i < partB.size(); i++) {
            for (int j = i + 1; j < partB.size(); j++) {
                if (graph.containsEdge(partB.get(i), partB.get(j))) return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        // Crear el grafo
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        // Añadir nodos
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");

        // Añadir aristas (forma un ciclo y es outerplanar)
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "D");
        graph.addEdge("D", "A");

        // Opcional: triangulación para hacerlo maximal (MOP)
        graph.addEdge("A", "C");
        graph.addEdge("B", "D");

        // Validar
        if (Main.isOuterplanar(graph)) {
            System.out.println("✅ El grafo es outerplanar.");
        } else {
            System.out.println("❌ El grafo NO es outerplanar.");
        }
    }
}


//import org.graphstream.graph.*;
//import org.graphstream.graph.implementations.*;
//
//public class Main {
//
//    public static void main(String[] args) {
//        // 1. Crear un grafo
//        Graph graph = new SingleGraph("Mi Grafo");
//
//        // 2. Agregar nodos
//        Node nodeA = graph.addNode("A");
//        Node nodeB = graph.addNode("B");
//        Node nodeC = graph.addNode("C");
//        Node nodeD = graph.addNode("D");
//
//
//        // 3. Agregar aristas
//        graph.addEdge("AB", "A", "B");
//        graph.addEdge("BC", "B", "C");
//        graph.addEdge("CD", "C", "D");
//        graph.addEdge("DA", "D", "A");
//
//
//        graph.addEdge("AC", "A", "C");
//        graph.addEdge("BD", "B", "D");
//
//        // 4. Manipular atributos visuales
//        nodeA.setAttribute("ui.label", "A");
//        nodeB.setAttribute("ui.label", "B");
//        nodeC.setAttribute("ui.label", "C");
//        nodeD.setAttribute("ui.label", "D");
//        graph.getEdge("BC").setAttribute("ui.style", "fill-color: blue;");
//
//        // 5. Estilo general del grafo
//        graph.setAttribute("ui.stylesheet",
//                "node { size: 20px; fill-color: red; } " +
//                        "edge { size: 2px; } " +
//                        "node#B { fill-color: green; }");
//
//        // 6. Visualizar el grafo
//        graph.display();
//    }
//}