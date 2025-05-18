package com.gordofriki;

import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public class Triangulo {
    public String v1, v2, v3;

    public Triangulo(String v1, String v2, String v3) {
        List<String> sorted = new ArrayList<>(List.of(v1, v2, v3));
        Collections.sort(sorted); // Para igualdad estructural
        this.v1 = sorted.get(0);
        this.v2 = sorted.get(1);
        this.v3 = sorted.get(2);
    }

    public Set<Set<String>> getEdges() {
        Set<Set<String>> edges = new HashSet<>();
        edges.add(Set.of(v1, v2));
        edges.add(Set.of(v2, v3));
        edges.add(Set.of(v1, v3));
        return edges;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Triangulo other) {
            return v1.equals(other.v1) && v2.equals(other.v2) && v3.equals(other.v3);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(v1, v2, v3);
    }

    @Override
    public String toString() {
        return "(" + v1 + "," + v2 + "," + v3 + ")";
    }
}

