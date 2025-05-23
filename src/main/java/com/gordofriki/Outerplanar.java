package com.gordofriki;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import java.util.*;
import org.jgrapht.graph.*;


public class Outerplanar {

    // Kuratowski
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

    //valida si hay un subgrafo K4 en el grafo
    private static boolean isK4(Graph<String, DefaultEdge> graph, Set<String> nodes) {
        //se convierte el conjunto de nodos en una lista para poder iterarlo
        List<String> list = new ArrayList<>(nodes);
        //guarda el numero de aristas encontradas en el subgrafo
        int countEdge = 0;

        for (int i = 0; i < list.size(); i++) {

            for (int j = i + 1; j < list.size(); j++) {
                if (graph.containsEdge(list.get(i), list.get(j)) || graph.containsEdge(list.get(j), list.get(i))) {
                    //si encuentra una arista entre dos nodos del subgrafo K4, se suma 1 al contador
                    countEdge++;
                }
            }
        }
        return countEdge == 6; // Total de aristas en K4
    }

    //valida si es un grafo bipartito
    private static boolean isK23(Graph<String, DefaultEdge> graph, Set<String> partA, Set<String> partB) {

        //el bucle recorre las aristas del grafo
        // y comprueba que no existan aristas entre ambos conjuntos de vertices {u, v}, {v, u}
        for (String u : partA) {
            for (String v : partB) {
                //Comprobamos que no exista una arista entre ambos vertices
                if (!graph.containsEdge(u, v) && !graph.containsEdge(v, u)) {
                    //Si no existe una arista entre ambos vertices no encuenra un subgrafo bipartito
                    return false;
                }
            }
        }

        //si encontro aristas entre vertices es k23
        return true;
    }

    public static <V, E> void triangulateCycle(Graph<V, E> graph, List<V> orderedCycle) {
        V anchor = orderedCycle.get(0);
        int n = orderedCycle.size();

        System.out.println("Triangulando ciclo: ");
        for (int i = 2; i < n - 1; i++) {
            V target = orderedCycle.get(i);
            //si no existe una arista para poder triangular
            if (!graph.containsEdge(anchor, target)) {
                graph.addEdge(anchor, target);
                System.out.println("Added edge: (" + anchor + " : " + target + ")");

                // Buscar triángulo
                Set<V> neighborsAnchor = Graphs.neighborSetOf(graph, anchor);
                Set<V> neighborsTarget = Graphs.neighborSetOf(graph, target);

                for (V common : neighborsAnchor) {
                    if (neighborsTarget.contains(common)) {
                        System.out.println("Triángulo: (" + anchor + ", " + target + ", " + common + ")");
                    }
                }
            }
        }
    }

    public static Graph<Triangulo, DefaultEdge> buildDualTree(Graph<String, DefaultEdge> mop) {
        Graph<Triangulo, DefaultEdge> dualTree = new SimpleGraph<>(DefaultEdge.class);

        List<String> vertices = new ArrayList<>(mop.vertexSet());
        List<Triangulo> triangles = new ArrayList<>();

        // Buscar todos los triángulos posibles
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                for (int k = j + 1; k < vertices.size(); k++) {
                    String u = vertices.get(i), v = vertices.get(j), w = vertices.get(k);

                    if (mop.containsEdge(u, v) && mop.containsEdge(v, w) && mop.containsEdge(w, u)) {
                        Triangulo t = new Triangulo(u, v, w);
                        triangles.add(t);
                        dualTree.addVertex(t);
                    }
                }
            }
        }

        // Conectar triángulos que comparten arista interna
        for (int i = 0; i < triangles.size(); i++) {
            for (int j = i + 1; j < triangles.size(); j++) {
                Triangulo t1 = triangles.get(i);
                Triangulo t2 = triangles.get(j);

                Set<Set<String>> edges1 = t1.getEdges();
                Set<Set<String>> edges2 = t2.getEdges();

                int shared = 0;
                for (Set<String> e : edges1) {
                    if (edges2.contains(e)) {
                        shared++;
                    }
                }

                if (shared == 1) { // comparten exactamente una arista
                    dualTree.addEdge(t1, t2);
                }
            }
        }

        return dualTree;
    }

    public static List<Triangulo> dfsPostOrder(Graph<Triangulo, DefaultEdge> dualTree) {
        List<Triangulo> postOrder = new ArrayList<>();
        Set<Triangulo> visited = new HashSet<>();

        if (dualTree.vertexSet().isEmpty()) return postOrder;

        Triangulo start = dualTree.vertexSet().iterator().next(); // Tomamos cualquier triángulo como raíz

        dfsHelper(dualTree, start, visited, postOrder);

        return postOrder;
    }

    private static void dfsHelper(Graph<Triangulo, DefaultEdge> dualTree, Triangulo current,
                                  Set<Triangulo> visited, List<Triangulo> postOrder) {
        visited.add(current);

        for (DefaultEdge edge : dualTree.edgesOf(current)) {

            Triangulo neighbor = Graphs.getOppositeVertex(dualTree, edge, current);
            if (!visited.contains(neighbor)) {
                dfsHelper(dualTree, neighbor, visited, postOrder);
            }
        }

        postOrder.add(current); // Postorden → se añade al final al terminar hijos
    }

//    public static Map<Triangulo, DPResult> computeMISDP(Graph<Triangulo, DefaultEdge> dualTree, List<Triangulo> postOrder) {
//        Map<Triangulo, DPResult> dp = new HashMap<>();
//
//        // Hacemos DP en orden postorder (de hojas a raíz)
//        for (Triangulo triangle : postOrder) {
//            int with = 1;   // Tomamos un vértice arbitrario del triángulo
//            int without = 0;
//
//            for (DefaultEdge edge : dualTree.edgesOf(triangle)) {
//                Triangulo neighbor = Graphs.getOppositeVertex(dualTree, edge, triangle);
//                if (dp.containsKey(neighbor)) {
//                    DPResult childDP = dp.get(neighbor);
//
//                    with += childDP.without;
//                    without += Math.max(childDP.with, childDP.without);
//                }
//            }
//
//            dp.put(triangle, new DPResult(with, without));
//        }
//
//        return dp;
//    }
    public static Map<Triangulo, DPResult> computeMISDP(Graph<Triangulo, DefaultEdge> dualTree, List<Triangulo> postOrder) {
        Map<Triangulo, DPResult> dp = new HashMap<>();

        for (Triangulo triangle : postOrder) {
            Set<String> withSet = new HashSet<>();
            Set<String> withoutSet = new HashSet<>();

            // Elige un vértice arbitrario del triángulo para "with"
            String chosenVertex = triangle.v1; // podrías hacer heurística aquí
            withSet.add(chosenVertex);

            int with = 1;
            int without = 0;

            for (DefaultEdge edge : dualTree.edgesOf(triangle)) {
                Triangulo neighbor = Graphs.getOppositeVertex(dualTree, edge, triangle);
                if (!dp.containsKey(neighbor)) continue;

                DPResult child = dp.get(neighbor);

                // Si tú eliges un vértice, tus hijos deben ir "sin vértice"
                with += child.without;
                withSet.addAll(child.withoutSet);

                // Si tú no eliges vértice, tus hijos eligen lo mejor
                if (child.with > child.without) {
                    without += child.with;
                    withoutSet.addAll(child.withSet);
                } else {
                    without += child.without;
                    withoutSet.addAll(child.withoutSet);
                }
            }

            dp.put(triangle, new DPResult(with, without, withSet, withoutSet));
        }

        return dp;
    }


    public static void main(String[] args) {
        // Crear el grafo
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);


        //Prueba 1 Ciclo simple C5
        String[] vertices = {"A", "B", "C", "D", "E", "F"};
        for (char c = 'A'; c <= 'F'; c++) {
            graph.addVertex(String.valueOf(c));
        }


        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "D");
        graph.addEdge("D", "E");
        graph.addEdge("E", "F");
        graph.addEdge("F", "A");


        // Opcional: triangulación para hacerlo maximal (MOP)
        graph.addEdge("A", "C");
        graph.addEdge("A", "D");
//        graph.addEdge("C", "E");
//        graph.addEdge("D", "E");

        //Validar
        boolean esOuterplanar = Outerplanar.isLikelyOuterplanar(graph);

        if(esOuterplanar){
            System.out.println("✅ El grafo es outerplanar.");

            List<String> orderedCycle = Arrays.asList("A", "B", "C", "D", "E", "F");
            triangulateCycle(graph,orderedCycle);

            Graph<Triangulo, DefaultEdge> dualTree = buildDualTree(graph);
            List<Triangulo> dfsOrder = dfsPostOrder(dualTree);

            System.out.println("DFS postorder del árbol dual:");
            for (Triangulo t : dfsOrder) {
                System.out.println(t);
            }

            Map<Triangulo, DPResult> results = computeMISDP(dualTree, dfsOrder);

//        Triangulo root = dfsOrder.getLast(); // Último en postorden = raíz
//        DPResult finalResult = results.get(root);
//        int maxIndependentSetSize = Math.max(finalResult.with, finalResult.without);
//
//        System.out.println("✅ Tamaño del Maximum Independent Set (MIS): " + maxIndependentSetSize);

            DPResult result = results.get(dfsOrder.getLast());
            Set<String> misVertices = result.with > result.without ? result.withSet : result.withoutSet;

            System.out.println("✅ MIS size: " + Math.max(result.with, result.without));
            System.out.println("🔹 Vértices en el MIS:");
            for (String v : misVertices) {
                System.out.println("- " + v);
            }

        }else{
            System.out.println("❌ El grafo NO es outerplanar.");
        }


    }
}

        //Prueba 1 Ciclo simple C5
//        String[] vertices = {"A", "B", "C", "D", "E"};
//        for (char c = 'A'; c <= 'E'; c++) {
//            graph.addVertex(String.valueOf(c));
//        }
//
//
//        graph.addEdge("A", "B");
//        graph.addEdge("B", "C");
//        graph.addEdge("C", "D");
//        graph.addEdge("D", "E");
//        graph.addEdge("E", "A");


            //MOP con 6 o 7 aristas
//        String[] vertices = {"A", "B", "C", "D", "E", "F", "G"};
//        for (char c = 'A'; c <= 'G'; c++) {
//            graph.addVertex(String.valueOf(c));
//        }
//
//        // Aristas del ciclo
//        graph.addEdge("A", "B");
//        graph.addEdge("B", "C");
//        graph.addEdge("C", "D");
//        graph.addEdge("D", "E");
//        graph.addEdge("E", "F");
//        graph.addEdge("F", "G");
//        graph.addEdge("G", "A");
//
//// Diagonales (para formar el MOP)
//        graph.addEdge("A", "C");
//        graph.addEdge("A", "D");
//        graph.addEdge("A", "E");


// Grafo outerplanar de 10 vertices
// Añadir vértices A–J (10 vértices)
//String[] vertices = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
//        for (String v : vertices) {
//        graph.addVertex(v);
//        }
//
//                // Añadir aristas del ciclo (C10)
//                graph.addEdge("A", "B");
//        graph.addEdge("B", "C");
//        graph.addEdge("C", "D");
//        graph.addEdge("D", "E");
//        graph.addEdge("E", "F");
//        graph.addEdge("F", "G");
//        graph.addEdge("G", "H");
//        graph.addEdge("H", "I");
//        graph.addEdge("I", "J");
//        graph.addEdge("J", "A");
//
//// Añadir diagonales para convertirlo en MOP (desde A)
//        graph.addEdge("A", "C");
//        graph.addEdge("A", "D");
//        graph.addEdge("A", "E");
//        graph.addEdge("A", "F");
//        graph.addEdge("A", "G");
//        graph.addEdge("A", "H");
//        graph.addEdge("A", "I");
//
//// Total de aristas = 10 (ciclo) + 7 (diagonales) = 17
//// 2n – 3 = 2*10 – 3 = 17 → ✅ MOP válido
//
//        System.out.println("Grafo MOP con 10 vértices listo.");


