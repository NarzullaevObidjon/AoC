package jon.obid.y25.d4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class P1 {
    public static void main(String[] args) throws IOException {

        String content = Files.readString(Paths.get("src/main/resources/y25/d4/input.txt"));
        System.out.println(p1(content));
    }

    public static int p1(String gridText) {
        String[] lines = gridText.replace("\r", "").split("\n");

        int H = lines.length;
        int W = lines[0].length();

        char[][] grid = new char[H][W];
        for (int i = 0; i < H; i++) {
            grid[i] = lines[i].toCharArray();
        }

        int[][] dirs = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1},  {1, 0},  {1, 1}
        };

        int accessible = 0;

        for (int r = 0; r < H; r++) {
            for (int c = 0; c < W; c++) {
                if (grid[r][c] != '@') continue;

                int neighbors = 0;
                for (int[] d : dirs) {
                    int nr = r + d[0];
                    int nc = c + d[1];
                    if (nr >= 0 && nr < H && nc >= 0 && nc < W && grid[nr][nc] == '@') {
                        neighbors++;
                    }
                }

                if (neighbors < 4) {
                    accessible++;
                }
            }
        }

        return accessible;
    }
}
