//package com.gordofriki;
//
//import org.jgrapht.Graph;
//import org.jgrapht.graph.DefaultEdge;
//import org.jgrapht.alg.connectivity.BiconnectivityInspector;
//import java.util.*;
//
//public class MitchellOuterplanar {
//
//    // Detecta K4 en un subgrafo
//    private static boolean containsK4(Graph<String, DefaultEdge> block) {
//        List<String> vertices = new ArrayList<>(block.vertexSet());
//        for (int i = 0; i < vertices.size(); i++) {
//            for (int j = i + 1; j < vertices.size(); j++) {
//                for (int k = j + 1; k < vertices.size(); k++) {
//                    for (int l = k + 1; l < vertices.size(); l++) {
//                        Set<String> sub = Set.of(vertices.get(i), vertices.get(j), vertices.get(k), vertices.get(l));
//                        int edgeCount = 0;
//                        List<String> list = new ArrayList<>(sub);
//                        for (int a = 0; a < list.size(); a++) {
//                            for (int b = a + 1; b < list.size(); b++) {
//                                if (block.containsEdge(list.get(a), list.get(b))) {
//                                    edgeCount++;
//                                }
//                            }
//                        }
//                        if (edgeCount == 6) return true; // K4 tiene 6 aristas
//                    }
//                }
//            }
//        }
//        return false;
//    }
//
//    // Detecta K2,3 en un subgrafo
//    private static boolean containsK23(Graph<String, DefaultEdge> block) {
//        List<String> vertices = new ArrayList<>(block.vertexSet());
//        int n = vertices.size();
//        for (int a = 0; a < n; a++) {
//            for (int b = a + 1; b < n; b++) {
//                Set<String> left = Set.of(vertices.get(a), vertices.get(b));
//                for (int c = 0; c < n; c++) {
//                    if (c == a || c == b) continue;
//                    for (int d = c + 1; d < n; d++) {
//                        if (d == a || d == b) continue;
//                        for (int e = d + 1; e < n; e++) {
//                            if (e == a || e == b) continue;
//                            Set<String> right = Set.of(vertices.get(c), vertices.get(d), vertices.get(e));
//                            boolean complete = true;
//                            for (String u : left) {
//                                for (String v : right) {
//                                    if (!block.containsEdge(u, v)) {
//                                        complete = false;
//                                        break;
//                                    }
//                                }
//                            }
//                            if (complete) return true; // Es un K2,3
//                        }
//                    }
//                }
//            }
//        }
//        return false;
//    }
//
//    // Algoritmo principal
//    public static boolean isOuterplanar(Graph<String, DefaultEdge> graph) {
//        BiconnectivityInspector<String, DefaultEdge> inspector = new BiconnectivityInspector<>(graph);
//        List<Set<String>> blocks = (List<Set<String>>) inspector.getConnectedComponents();
//
//
//        for (Set<String> blockVertices : blocks) {
//            Graph<String, DefaultEdge> block = extractSubgraph(graph, blockVertices);
//            if (containsK4(block) || containsK23(block)) {
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    // Extrae un subgrafo inducido por un conjunto de vértices
//    private static Graph<String, DefaultEdge> extractSubgraph(Graph<String, DefaultEdge> graph, Set<String> vertices) {
//        Graph<String, DefaultEdge> subgraph = new org.jgrapht.graph.SimpleGraph<>(DefaultEdge.class);
//        for (String v : vertices) {
//            subgraph.addVertex(v);
//        }
//        for (String u : vertices) {
//            for (String v : vertices) {
//                if (!u.equals(v) && graph.containsEdge(u, v)) {
//                    subgraph.addEdge(u, v);
//                }
//            }
//        }
//        return subgraph;
//    }
//}
