package jon.obid.y25.d11;

import java.io.*;
import java.util.*;

public class ReactorPathFinder {

    private final Map<String, List<String>> graph;

    public ReactorPathFinder() {
        this.graph = new HashMap<>();
    }

    public void parseInput(String input) {
        graph.clear();
        String[] lines = input.trim().split("\n");

        for (String line : lines) {
            String[] parts = line.split(":");
            String device = parts[0].trim();
            String[] outputs = parts[1].trim().split("\\s+");

            graph.put(device, Arrays.asList(outputs));
        }
    }

    public void parseInputFromFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        parseInput(content.toString());
    }

    public long countPaths(String start, String end) {
        Map<String, Long> memo = new HashMap<>();
        return countPathsDFS(start, end, memo);
    }

    private long countPathsDFS(String current, String end, Map<String, Long> memo) {
        if (current.equals(end)) {
            return 1;
        }

        if (memo.containsKey(current)) {
            return memo.get(current);
        }

        long count = 0;
        if (graph.containsKey(current)) {
            for (String next : graph.get(current)) {
                count += countPathsDFS(next, end, memo);
            }
        }

        memo.put(current, count);
        return count;
    }

    public long countPathsVisitingBoth(String start, String end, String req1, String req2) {
        return countPathsWithRequirements(start, end, new HashSet<>(Arrays.asList(req1, req2)));
    }

    private long countPathsWithRequirements(String start, String end, Set<String> required) {
        Map<StateKey, Long> memo = new HashMap<>();
        return countPathsWithReqDFS(start, end, required, new HashSet<>(), memo);
    }

    private long countPathsWithReqDFS(String current, String end,
                                      Set<String> required, Set<String> visited,
                                      Map<StateKey, Long> memo) {
        Set<String> newVisited = new HashSet<>(visited);
        if (required.contains(current)) {
            newVisited.add(current);
        }

        if (current.equals(end)) {
            return newVisited.containsAll(required) ? 1 : 0;
        }

        StateKey key = new StateKey(current, newVisited);
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        long count = 0;
        if (graph.containsKey(current)) {
            for (String next : graph.get(current)) {
                count += countPathsWithReqDFS(next, end, required, newVisited, memo);
            }
        }

        memo.put(key, count);
        return count;
    }

    private static class StateKey {
        String node;
        Set<String> visitedRequired;

        StateKey(String node, Set<String> visitedRequired) {
            this.node = node;
            this.visitedRequired = new HashSet<>(visitedRequired);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StateKey stateKey = (StateKey) o;
            return node.equals(stateKey.node) &&
                    visitedRequired.equals(stateKey.visitedRequired);
        }

        @Override
        public int hashCode() {
            return Objects.hash(node, visitedRequired);
        }
    }

    public long solvePart1() {
        if (!graph.containsKey("you")) {
            return 0;
        }
        return countPaths("you", "out");
    }

    public long solvePart2() {
        if (!graph.containsKey("svr")) {
            return 0;
        }
        return countPathsVisitingBoth("svr", "out", "dac", "fft");
    }

    public static void main(String[] args) {
        ReactorPathFinder finder = new ReactorPathFinder();

        try {
            finder.parseInputFromFile("D:\\algo\\AoC\\src\\main\\resources\\y25\\d11\\input.txt");

            long part1Answer = finder.solvePart1();
            System.out.println("Part 1: " + part1Answer);

            long part2Answer = finder.solvePart2();
            System.out.println("Part 2: " + part2Answer);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}