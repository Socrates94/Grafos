package com.gordofriki;

import java.util.Set;

public class DPResult {

    int with;   // Tamaño del MIS si tomamos un vértice en este triángulo
    int without; // Tamaño del MIS si no tomamos ningún vértice en este triángulo
    Set<String> withSet;
    Set<String> withoutSet;


    public DPResult(int with, int without) {
        this.with = with;
        this.without = without;
    }

    public DPResult(int with, int without, Set<String> withSet, Set<String> withoutSet) {
        this.with = with;
        this.without = without;
        this.withSet = withSet;
        this.withoutSet = withoutSet;
    }
}
