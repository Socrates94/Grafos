package com.gordofriki;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import java.util.*;
import org.jgrapht.graph.*;


public class Outerplanar {

    public static boolean isLikelyOuterplanar(Graph<String, DefaultEdge> graph) {
        int vertexCount = graph.vertexSet().size();
        int edgeCount = graph.edgeSet().size();

        // Condición necesaria básica
        if (edgeCount > 2 * vertexCount - 3) {
            return false;
        }

        // Verificación heurística de subgrafo K4
        List<String> vertices = new ArrayList<>(graph.vertexSet());
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                for (int k = j + 1; k < vertices.size(); k++) {
                    for (int l = k + 1; l < vertices.size(); l++) {
                        Set<String> subgraph = Set.of(vertices.get(i), vertices.get(j), vertices.get(k), vertices.get(l));
                        if (isK4(graph, subgraph)) {
                            return false;
                        }
                    }
                }
            }
        }

        // Verificación heurística de subgrafo K2,3
        for (int a = 0; a < vertices.size(); a++) {
            for (int b = a + 1; b < vertices.size(); b++) {
                for (int c = 0; c < vertices.size(); c++) {
                    if (c == a || c == b) continue;
                    for (int d = c + 1; d < vertices.size(); d++) {
                        if (d == a || d == b) continue;
                        for (int e = d + 1; e < vertices.size(); e++) {
                            if (e == a || e == b) continue;

                            Set<String> left = Set.of(vertices.get(a), vertices.get(b));
                            Set<String> right = Set.of(vertices.get(c), vertices.get(d), vertices.get(e));

                            if (isK23(graph, left, right)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    private static boolean isK4(Graph<String, DefaultEdge> graph, Set<String> nodes) {
        List<String> list = new ArrayList<>(nodes);
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (graph.containsEdge(list.get(i), list.get(j)) || graph.containsEdge(list.get(j), list.get(i))) {
                    count++;
                }
            }
        }
        return count == 6; // Total de aristas en K4
    }

    private static boolean isK23(Graph<String, DefaultEdge> graph, Set<String> partA, Set<String> partB) {
        for (String u : partA) {
            for (String v : partB) {
                if (!graph.containsEdge(u, v) && !graph.containsEdge(v, u)) {
                    return false;
                }
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
        graph.addVertex("E");

        // Añadir aristas (forma un ciclo y es outerplanar)
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "D");
        graph.addEdge("D", "A");

        // Opcional: triangulación para hacerlo maximal (MOP)
//        graph.addEdge("A", "E");
//        graph.addEdge("B", "E");
//        graph.addEdge("C", "E");
//        graph.addEdge("D", "E");

//         Validar
        boolean esOuterplanar = Outerplanar.isLikelyOuterplanar(graph);
//        System.out.println("¿Es outerplanar?: " + esOuterplanar);

        if(esOuterplanar){
            System.out.println("✅ El grafo es outerplanar.");
        }else{
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
//        Node nodeE = graph.addNode("E");
//
//
//        // 3. Agregar aristas
//        graph.addEdge("AB", "A", "B");
//        graph.addEdge("BC", "B", "C");
//        graph.addEdge("CD", "C", "D");
//        graph.addEdge("DA", "D", "A");
//
//
//        graph.addEdge("EA", "E", "A");
//        graph.addEdge("EB", "E", "B");
//        graph.addEdge("EC", "E", "C");
//        graph.addEdge("ED", "E", "D");
//
//        // 4. Manipular atributos visuales
//        nodeA.setAttribute("ui.label", "A");
//        nodeB.setAttribute("ui.label", "B");
//        nodeC.setAttribute("ui.label", "C");
//        nodeD.setAttribute("ui.label", "D");
//        nodeE.setAttribute("ui.label", "E");
//        graph.getEdge("EA").setAttribute("ui.style", "fill-color: blue;");
//        graph.getEdge("EB").setAttribute("ui.style", "fill-color: blue;");
//        graph.getEdge("EC").setAttribute("ui.style", "fill-color: blue;");
//        graph.getEdge("ED").setAttribute("ui.style", "fill-color: blue;");
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