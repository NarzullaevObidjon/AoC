package jon.obid.y25.d7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class P2 {
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
            for (int c = 0; c < width; c++) grid[r][c] = (c < s.length() ? s.charAt(c) : '.');
        }

        int startR = -1, startC = -1;
        outer:
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < width; c++) {
                if (grid[r][c] == 'S') {
                    startR = r; startC = c;
                    break outer;
                }
            }
        }

        BigInteger[] dp = new BigInteger[width];
        for (int i = 0; i < width; i++) dp[i] = BigInteger.ZERO;
        dp[startC] = BigInteger.ONE;

        for (int r = startR + 1; r < rows; r++) {
            BigInteger[] next = new BigInteger[width];
            for (int i = 0; i < width; i++) next[i] = BigInteger.ZERO;

            boolean any = false;
            for (int c = 0; c < width; c++) {
                if (dp[c].signum() == 0) continue;
                any = true;
                if (grid[r][c] == '^') {
                    if (c - 1 >= 0) next[c - 1] = next[c - 1].add(dp[c]);
                    if (c + 1 < width) next[c + 1] = next[c + 1].add(dp[c]);
                } else {
                    next[c] = next[c].add(dp[c]);
                }
            }
            if (!any) {
                dp = new BigInteger[width];
                for (int i = 0; i < width; i++) dp[i] = BigInteger.ZERO;
                break;
            }
            dp = next;
        }

        BigInteger total = BigInteger.ZERO;
        for (BigInteger b : dp) total = total.add(b);
        System.out.println(total);
    }
}
