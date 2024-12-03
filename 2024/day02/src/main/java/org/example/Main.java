package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

// --- Day 2: Red-Nosed Reports ---
// Fortunately, the first location The Historians want to search isn't a long walk from the Chief Historian's office.
//
// While the Red-Nosed Reindeer nuclear fusion/fission plant appears to contain no sign of the Chief Historian, the
// engineers there run up to you as soon as they see you. Apparently, they still talk about the time Rudolph was saved
// through molecular synthesis from a single electron.
//
// They're quick to add that - since you're already here - they'd really appreciate your help analyzing some unusual
// data from the Red-Nosed reactor. You turn to check if The Historians are waiting for you, but they seem to have
// already divided into groups that are currently searching every corner of the facility. You offer to help with the
// unusual data.
//
// The unusual data (your puzzle input) consists of many reports, one report per line. Each report is a list of numbers
// called levels that are separated by spaces. For example:
//
// 7 6 4 2 1
// 1 2 7 8 9
// 9 7 6 2 1
// 1 3 2 4 5
// 8 6 4 4 1
// 1 3 6 7 9
// This example data contains six reports each containing five levels.
//
// The engineers are trying to figure out which reports are safe. The Red-Nosed reactor safety systems can only tolerate
// levels that are either gradually increasing or gradually decreasing. So, a report only counts as safe if both of the following are true:
//
// The levels are either all increasing or all decreasing.
// Any two adjacent levels differ by at least one and at most three.
// In the example above, the reports can be found safe or unsafe by checking those rules:
//
// 7 6 4 2 1: Safe because the levels are all decreasing by 1 or 2.
// 1 2 7 8 9: Unsafe because 2 7 is an increase of 5.
// 9 7 6 2 1: Unsafe because 6 2 is a decrease of 4.
// 1 3 2 4 5: Unsafe because 1 3 is increasing but 3 2 is decreasing.
// 8 6 4 4 1: Unsafe because 4 4 is neither an increase or a decrease.
// 1 3 6 7 9: Safe because the levels are all increasing by 1, 2, or 3.
// So, in this example, 2 reports are safe.
//
// Analyze the unusual data from the engineers. How many reports are safe?
//
// Your puzzle answer was 534.

// --- Part Two ---
// The engineers are surprised by the low number of safe reports until they realize they forgot to tell you about the
// Problem Dampener.
//
// The Problem Dampener is a reactor-mounted module that lets the reactor safety systems tolerate a single bad level in
// what would otherwise be a safe report. It's like the bad level never happened!
//
// Now, the same rules apply as before, except if removing a single level from an unsafe report would make it safe, the
// report instead counts as safe.
//
// More of the above example's reports are now safe:
//
// 7 6 4 2 1: Safe without removing any level.
// 1 2 7 8 9: Unsafe regardless of which level is removed.
// 9 7 6 2 1: Unsafe regardless of which level is removed.
// 1 3 2 4 5: Safe by removing the second level, 3.
// 8 6 4 4 1: Safe by removing the third level, 4.
// 1 3 6 7 9: Safe without removing any level.
// Thanks to the Problem Dampener, 4 reports are actually safe!
//
// Update your analysis by handling situations where the Problem Dampener can remove a single level from unsafe reports.
// How many reports are now safe?
public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("How many reports are safe?");
        System.out.println(partA());
        System.out.println(partA1());
        System.out.println();

        System.out.println("How many reports are now safe");
        System.out.println(partB());
    }


    static int partA() throws IOException {

        var count = 0;
        try (var is = ClassLoader.getSystemResourceAsStream("input_a.txt");
             var ir = new InputStreamReader(is);
             var br = new BufferedReader(ir)) {

            while (br.ready()) {
                var line = br.readLine();
                var levels = Arrays
                        .stream(line.split("\\s+"))
                        .mapToInt(Integer::parseInt)
                        .toArray();

                var safe = isSafeDirectionUnknown(levels, 0, 0);
//                System.out.println( safe + " " + line);
                if (safe) count++;
            }

        }
        return count;
    }

    static int partA1() throws IOException {

        var count = 0;
        try (var is = ClassLoader.getSystemResourceAsStream("input_a.txt");
             var ir = new InputStreamReader(is);
             var br = new BufferedReader(ir)) {

            while (br.ready()) {
                var line = br.readLine();
                var levels = Arrays
                        .stream(line.split("\\s+"))
                        .map(Integer::valueOf)
                        .collect(Collectors.toCollection(ArrayList::new));

                var safe = findUnsafe(levels) < 0;
//                System.out.println( safe + " " + line);
                if (safe) {
                    count++;
                }
            }

        }
        return count;
    }


    static int partB() throws IOException {

        var count = 0;
        try (var is = ClassLoader.getSystemResourceAsStream("input_a.txt");
             var ir = new InputStreamReader(is);
             var br = new BufferedReader(ir)) {


            while (br.ready()) {
                var line = br.readLine();
                var levels = Arrays
                        .stream(line.split("\\s+"))
                        .map(Integer::valueOf)
                        .collect(Collectors.toCollection(ArrayList::new));

                var unsafeIndex = findUnsafe(levels);
                if (unsafeIndex < 0) {
                    count++;
                } else {
                    var clone = new ArrayList<>(levels);
                    clone.remove(unsafeIndex);
                    if (findUnsafe(clone) < 0) {
                        count++;
                    } else {
                        clone = new ArrayList<>(levels);
                        clone.remove(unsafeIndex + 1);
                        if (findUnsafe(clone) < 0) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    static int findUnsafe(List<Integer> levels) {
        if (levels.size() <= 1) return -1;

        int first = levels.get(0);
        int second = levels.get(1);

        if (first == second) {
            return 0;
        }

        var ascending = first < second;

        for (int i = 0; i < levels.size() - 1; i++) {
            var safe = isSafe(levels.get(i), levels.get(i + 1), ascending);
            if (!safe) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Check to see if the pair of levels is considered safe.
     *
     * @param a         current level
     * @param b         next level
     * @param ascending true if the sequence must be ascending to be safe; false otherwise
     * @return true if the pair is considered safe
     */
    static boolean isSafe(int a,
                          int b,
                          boolean ascending) {
        if (ascending) {
            return (a < b && Math.abs(a - b) <= 3);
        }
        return (a > b && Math.abs(a - b) <= 3);
    }


    /**
     * Check to see if the sequence of levels is unsafe when we don't know if it is ascending or descending yet.
     *
     * @param levels    levels to compare
     * @param start     starting index into levels array
     * @param forgive   number of violations that are acceptable
     * @return true if level is considered safe
     */
    static boolean isSafeDirectionUnknown(int[] levels,
                                          int start,
                                          int forgive) {

        if (levels[start] == levels[start + 1]) {
            if (forgive > 0) return isSafeDirectionUnknown(levels, start + 1, forgive - 1);
            return false;
        }

        if (levels[start] > levels[start + 1]) return isSafeDirectionKnown(levels, start, forgive, false);
        return isSafeDirectionKnown(levels, start, forgive, true);
    }

    /**
     * Check to see if the sequence of levels is unsafe when ascending or descending has been determined.
     *
     * @param levels    levels to compare
     * @param start     starting index into levels array
     * @param forgive   number of violations that are acceptable
     * @param ascending true if the sequence must be ascending to be safe; false otherwise
     * @return true if level is considered safe
     */
    static boolean isSafeDirectionKnown(int[] levels,
                                        int start,
                                        int forgive,
                                        boolean ascending) {
        for (int i = start; i < levels.length - 1; i++) {
            var safe = isSafe(levels[i], levels[i + 1], ascending);
            if (!safe) {
                if (forgive > 0) {
                    return isSafeDirectionKnown(levels, start + 1, forgive - 1, ascending);
                }
                return false;
            }
        }
        return true;
    }
}