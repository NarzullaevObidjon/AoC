package jon.obid.y25.d9;

import java.io.*;
import java.util.*;

public class P2 {

    static class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Point)) return false;
            Point p = (Point) o;
            return x == p.x && y == p.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public static void main(String[] args) throws IOException {
        List<Point> redTiles = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader("D:/algo/AoC/src/main/resources/y25/d9/input.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.trim().split(",");
            redTiles.add(new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
        }
        br.close();

        Map<Integer, List<int[]>> validRangesPerY = new HashMap<>();

        for (int i = 0; i < redTiles.size(); i++) {
            Point p1 = redTiles.get(i);
            Point p2 = redTiles.get((i + 1) % redTiles.size());

            if (p1.x == p2.x) {
                int minY = Math.min(p1.y, p2.y);
                int maxY = Math.max(p1.y, p2.y);
                for (int y = minY; y <= maxY; y++) {
                    validRangesPerY.computeIfAbsent(y, k -> new ArrayList<>()).add(new int[]{p1.x, p1.x});
                }
            } else {
                int minX = Math.min(p1.x, p2.x);
                int maxX = Math.max(p1.x, p2.x);
                validRangesPerY.computeIfAbsent(p1.y, k -> new ArrayList<>()).add(new int[]{minX, maxX});
            }
        }

        int polygonMinY = redTiles.stream().mapToInt(p -> p.y).min().getAsInt();
        int polygonMaxY = redTiles.stream().mapToInt(p -> p.y).max().getAsInt();

        for (int y = polygonMinY; y <= polygonMaxY; y++) {
            List<Integer> intersections = new ArrayList<>();

            for (int i = 0; i < redTiles.size(); i++) {
                Point v1 = redTiles.get(i);
                Point v2 = redTiles.get((i + 1) % redTiles.size());

                if ((v1.y <= y && v2.y > y) || (v2.y <= y && v1.y > y)) {
                    int xIntersect = v1.x + (y - v1.y) * (v2.x - v1.x) / (v2.y - v1.y);
                    intersections.add(xIntersect);
                }
            }

            Collections.sort(intersections);

            for (int i = 0; i < intersections.size(); i += 2) {
                if (i + 1 < intersections.size()) {
                    validRangesPerY.computeIfAbsent(y, k -> new ArrayList<>()).add(
                            new int[]{intersections.get(i), intersections.get(i + 1)});
                }
            }
        }

        validRangesPerY.replaceAll((y, v) -> mergeRanges(v));

        long maxArea = 0;

        for (int i = 0; i < redTiles.size(); i++) {
            for (int j = i + 1; j < redTiles.size(); j++) {
                Point p1 = redTiles.get(i);
                Point p2 = redTiles.get(j);

                if (p1.x == p2.x || p1.y == p2.y) continue;

                int minX = Math.min(p1.x, p2.x);
                int maxX = Math.max(p1.x, p2.x);
                int minY = Math.min(p1.y, p2.y);
                int maxY = Math.max(p1.y, p2.y);

                boolean valid = true;
                for (int y = minY; y <= maxY && valid; y++) {
                    if (!validRangesPerY.containsKey(y)) {
                        valid = false;
                        break;
                    }
                    if (!isRangeCovered(validRangesPerY.get(y), minX, maxX)) {
                        valid = false;
                    }
                }

                if (valid) {
                    long area = (long)(maxX - minX + 1) * (maxY - minY + 1);
                    maxArea = Math.max(maxArea, area);
                }
            }
        }

        System.out.println(maxArea);
    }

    static List<int[]> mergeRanges(List<int[]> ranges) {
        if (ranges.isEmpty()) return ranges;

        ranges.sort(Comparator.comparingInt(a -> a[0]));
        List<int[]> merged = new ArrayList<>();
        int[] current = ranges.get(0);

        for (int i = 1; i < ranges.size(); i++) {
            int[] next = ranges.get(i);
            if (next[0] <= current[1] + 1) {
                current[1] = Math.max(current[1], next[1]);
            } else {
                merged.add(current);
                current = next;
            }
        }
        merged.add(current);
        return merged;
    }

    static boolean isRangeCovered(List<int[]> ranges, int minX, int maxX) {
        for (int[] range : ranges) {
            if (range[0] <= minX && range[1] >= maxX) {
                return true;
            }
        }
        return false;
    }
}