package com.gordofriki;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
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

    public static <V, E> void triangulateOuterplanar(Graph<V, E> graph) {
        List<V> vertices = new ArrayList<>(graph.vertexSet());

        // Si ya es un ciclo simple (C_n), añade diagonales sin cruzarlas
        for (int i = 0; i < vertices.size() - 2; i++) {
            for (int j = i + 2; j < vertices.size(); j++) {
                if ((i == 0 && j == vertices.size() - 1)) continue; // Evita cerrar el ciclo por el otro extremo
                if (!graph.containsEdge(vertices.get(i), vertices.get(j))) {
                    System.out.println(graph.addEdge(vertices.get(i), vertices.get(j)));;
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

            triangulateOuterplanar(graph);
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