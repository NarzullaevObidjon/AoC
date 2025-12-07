package jon.obid.y25.d7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class P1 {
    public static void main(String[] args) throws IOException {
        p1();
    }

    private static void p1() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("D:/algo/AoC/src/main/resources/y25/d7/input.txt"));
        List<String> raw = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            raw.add(line);
        }
        br.close();

        int rows = raw.size();
        int width = 0;
        for (String s : raw) width = Math.max(width, s.length());
        char[][] grid = new char[rows][width];
        for (int r = 0; r < rows; r++) {
            String s = raw.get(r);
            for (int c = 0; c < width; c++) {
                grid[r][c] = (c < s.length()) ? s.charAt(c) : '.';
            }
        }

        int startRow = -1, startCol = -1;
        outer:
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < width; c++) {
                if (grid[r][c] == 'S') {
                    startRow = r;
                    startCol = c;
                    break outer;
                }
            }
        }

        Set<Integer> active = new HashSet<>();
        active.add(startCol);

        long splitCount = 0;

        for (int r = startRow + 1; r < rows; r++) {
            Set<Integer> next = new HashSet<>();
            if (active.isEmpty()) break;
            for (int c : active) {
                if (c < 0 || c >= width) continue;
                char cell = grid[r][c];
                if (cell == '^') {
                    splitCount++;
                    int left = c - 1, right = c + 1;
                    if (left >= 0) next.add(left);
                    if (right < width) next.add(right);
                } else {
                    next.add(c);
                }
            }
            active = next;
        }

        System.out.println(splitCount);
    }
}
