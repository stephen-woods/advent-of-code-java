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

    public int starOfWord(int x, int y, String word) {
        int ret = 0;

        // Horizontal forward and maybe backward if necessary
        var hf = IntStream
                .iterate(0, i -> i < word.length(), i -> i + 1)
                .allMatch(step -> horizontalMatch(x, y, word, step, step));

        var h = hf || IntStream
                .iterate(word.length() - 1, i -> i >= 0, i -> i - 1)
                .allMatch(step -> horizontalMatch(x, y, word, step, word.length() - 1 - step));
        if (h) ret++;


        // Vertical forward and maybe backward if necessary
        var vf = IntStream
                .range(0, word.length())
                .allMatch(step -> verticalMatch(x, y, word, step, step));

        var v = vf || IntStream
                .iterate(word.length() - 1, i -> i >= 0, i -> i - 1)
                .allMatch(step -> verticalMatch(x, y, word, step, word.length() - 1 - step));
        if (v) ret++;

        // DiagonalUp forward and maybe backward if necessary
        var duf = IntStream
                .range(0, word.length())
                .allMatch(step -> diagUpMatch(x, y, word, step, step));

        var du = duf || IntStream
                .iterate(word.length() - 1, i -> i >= 0, i -> i - 1)
                .allMatch(step -> diagUpMatch(x, y, word, step, word.length() - 1 - step));
        if (du) ret++;

        // DiagonalDown forward and maybe backward if necessary
        var ddf = IntStream
                .range(0, word.length())
                .allMatch(step -> diagDownMatch(x, y, word, step, step));

        var dd = ddf || IntStream
                .iterate(word.length() - 1, i -> i >= 0, i -> i - 1)
                .allMatch(step -> diagDownMatch(x, y, word, step, word.length() - 1 - step));
        if (du) ret++;

        return ret;
    }

    boolean horizontalMatch(int x, int y, String word, int puzzleStep, int wordStep) {
        var puzzleChar = get(x + puzzleStep, y);
        Character wordChar = word.charAt(wordStep);
        return puzzleChar == wordChar;
    }

    boolean verticalMatch(int x, int y, String word, int puzzleStep, int wordStep) {
        var puzzleChar = get(x, y + puzzleStep);
        Character wordChar = word.charAt(wordStep);
        return puzzleChar == wordChar;
    }

    boolean diagUpMatch(int x, int y, String word, int puzzleStep,  int wordStep) {
        var puzzleChar = get(x + puzzleStep, y + puzzleStep);
        Character wordChar = word.charAt(wordStep);
        return puzzleChar == wordChar;
    }

    boolean diagDownMatch(int x, int y, String word, int puzzleStep,  int wordStep) {
        var puzzleChar = get(x + puzzleStep, y - puzzleStep);
        Character wordChar = word.charAt(wordStep);
        return puzzleChar == wordChar;
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
