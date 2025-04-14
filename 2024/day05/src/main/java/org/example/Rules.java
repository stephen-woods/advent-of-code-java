package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Rules {
    Map<Integer, List<Integer>> deps;

    private Rules(Map<Integer, List<Integer>> deps) {
        this.deps = deps;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Rules:\n");
        for (Map.Entry<Integer, List<Integer>> entry : deps.entrySet()) {
            var line = " %d -> %s\n".formatted(entry.getKey(), entry.getValue());
            sb.append(line);
        }
        return sb.toString();
    }

    public static Rules from(BufferedReader br) throws IOException {
        Map<Integer, List<Integer>> deps = new HashMap<>();
        while (br.ready()) {
            var line = br.readLine();
            if (line.trim().isBlank()) break;

            var data = line.split("\\|");
            var p1 = Integer.parseInt(data[0]);
            var p2 = Integer.parseInt(data[1]);
            deps.computeIfAbsent(p1, k -> new ArrayList<>()).add(p2);

        }
        return new Rules(deps);
    }
}
