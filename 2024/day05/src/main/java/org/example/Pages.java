package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class Pages {
    List<Integer> underlying;

    private Pages(List<Integer> underlying) {
        this.underlying = underlying;
    }

    public static Pages from(BufferedReader br) throws IOException {
        while (br.ready()) {
            var line = br.readLine();
            var ps =
        }
    }
}
