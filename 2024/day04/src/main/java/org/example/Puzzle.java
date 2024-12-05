package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Puzzle {
    List<List<Character>> underlying;
    int height;
    int width;

    int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
    int[] dy = {0, 1, 1, 1, 0, -1, -1, -1};

    private Puzzle(List<List<Character>> underlying) {
        this.underlying = underlying;
        this.height = underlying.size();
        this.width = underlying.getFirst().size();;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Character get(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) return null;
        return underlying.get(y).get(x);
    }

    public boolean outOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= width || y >= height);
    }

    public int spinCheck(int x, int y, String word) {
        var matches = 0;

        char puzzleChar = get(x, y);
        char wordChar = word.charAt(0);
        if (puzzleChar != wordChar) return 0;

        for (int i = 0; i < 8; i++) {
            var xx = x + dx[i];
            var yy = y + dy[i];
            var found = true;
            for (int c = 1; c < word.length(); c++) {

                if (outOfBounds(xx, yy)) {
                    found = false;
                    break;
                }

                puzzleChar = get(xx, yy);
                wordChar = word.charAt(c);
                if (puzzleChar != wordChar) {
                    found = false;
                    break;
                }

                xx += dx[i];
                yy += dy[i];
            }
            if (found) matches++;
        }
        return matches;
    }

    public static Puzzle from(BufferedReader br) throws IOException {
        List<List<Character>> ret = new ArrayList<>();
        while (br.ready()) {
            var line = br.readLine();
            List<Character> row = new ArrayList<>();
            for (char c : line.toCharArray()) {
                row.add(c);
            }
            ret.add(row);
        }
        return new Puzzle(ret);
    }
}
