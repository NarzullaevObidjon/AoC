package jon.obid.y25.d9;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class P1 {

    public static void main(String[] args) throws IOException {
        System.out.println(maxRectangleArea());
    }

    private static long maxRectangleArea() throws IOException {

        String path = "D:/algo/AoC/src/main/resources/y25/d9/input.txt";

        List<Point> points = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] split = line.split(",");
                points.add(new Point(
                        Integer.parseInt(split[0].trim()),
                        Integer.parseInt(split[1].trim())
                ));
            }
        }

        long maxArea = 0;

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {

                Point a = points.get(i);
                Point b = points.get(j);

                long width  = Math.abs((long) a.x - b.x) + 1L;
                long height = Math.abs((long) a.y - b.y) + 1L;

                long area = width * height;

                if (area > maxArea) {
                    maxArea = area;
                }
            }
        }

        return maxArea;
    }

    static class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
