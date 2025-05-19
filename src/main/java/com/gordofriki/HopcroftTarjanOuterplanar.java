//package com.gordofriki;
//
//import org.jgrapht.Graph;
//import org.jgrapht.graph.DefaultEdge;
//import org.jgrapht.alg.connectivity.BiconnectivityInspector;
//import java.util.*;
//
//public class HopcroftTarjanOuterplanar {
//
//    public static boolean isOuterplanar(Graph<String, DefaultEdge> graph) {
//        BiconnectivityInspector<String, DefaultEdge> inspector = new BiconnectivityInspector<>(graph);
//        List<Set<String>> blocks = (List<Set<String>>) inspector.getConnectedComponents();
//
//        for (Set<String> blockVertices : blocks) {
//            Graph<String, DefaultEdge> block = extractSubgraph(graph, blockVertices);
//
//            int v = block.vertexSet().size();
//            int e = block.edgeSet().size();
//
//            if (e > 2 * v - 3) {
//                return false; // No puede ser outerplanar según Hopcroft-Tarjan
//            }
//        }
//
//        return true;
//    }
//
//    // Extrae el subgrafo inducido
//    private static Graph<String, DefaultEdge> extractSubgraph(Graph<String, DefaultEdge> graph, Set<String> vertices) {
//        Graph<String, DefaultEdge> subgraph = new org.jgrapht.graph.SimpleGraph<>(DefaultEdge.class);
//        for (String v : vertices) {
//            subgraph.addVertex(v);
//        }
//        for (String u : vertices) {
//            for (String v : vertices) {
//                if (!u.equals(v) && graph.containsEdge(u, v)) {
//                    if (!subgraph.containsEdge(u, v)) {
//                        subgraph.addEdge(u, v);
//                    }
//                }
//            }
//        }
//        return subgraph;
//    }
//}
//
