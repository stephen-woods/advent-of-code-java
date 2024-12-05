package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Puzzle {
    List<List<Character>> underlying;
    int height;
    int width;

    int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
    int[] dy = {0, 1, 1, 1, 0, -1, -1, -1};


    private Puzzle(List<List<Character>> underlying) {
        this.underlying = underlying;
        this.height = underlying.size();
        this.width = underlying.getFirst().size();
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

    public int checkPartA(int x, int y, String word) {
        var matches = 0;

        char puzzleChar = get(x, y);
        char wordChar = word.charAt(0);
        if (puzzleChar != wordChar) return 0;

        for (int i = 0; i < dx.length; i++) {
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

    public int checkPartB(int x, int y) {
        char puzzleChar = get(x, y);
        if (puzzleChar != 'A') return 0;

        boolean condition1 = checkChar(
                get(x - 1, y - 1),
                get(x + 1, y + 1));

        boolean condition2 =  checkChar(
                get(x + 1, y - 1),
                get(x - 1, y + 1)
        );

        return (condition1 && condition2) ? 1 : 0;
    }

    boolean checkChar(Character c1, Character c2) {
        if (c1 == null || c2 == null) return false;
        return (c1 == 'M' || c1 == 'S') && (c2 == 'M' || c2 == 'S') && (c1 != c2);
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
