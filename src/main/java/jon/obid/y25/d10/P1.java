package jon.obid.y25.d10;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class P1 {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("D:/algo/AoC/src/main/resources/y25/d10/input.txt"));
        String line;
        long totalMinPresses = 0;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            int m = parseLightsCount(line);
            List<BigInteger> buttonCols = parseButtons(line);
            BigInteger target = parseTarget(line);

            int n = buttonCols.size();

            int minPresses = solveMinPressesGF2(m, n, buttonCols, target);

            totalMinPresses += minPresses;
        }
        System.out.println(totalMinPresses);
    }

    static int parseLightsCount(String line) {
        Pattern p = Pattern.compile("\\[(.*?)]");
        Matcher m = p.matcher(line);
        if (m.find()) {
            String pat = m.group(1);
            return pat.length();
        }
        return -1;
    }

    static List<BigInteger> parseButtons(String line) {
        List<BigInteger> cols = new ArrayList<>();
        Pattern p = Pattern.compile("\\((.*?)\\)");
        Matcher m = p.matcher(line);
        while (m.find()) {
            String inner = m.group(1).trim();
            if (inner.isEmpty()) {
                cols.add(BigInteger.ZERO);
                continue;
            }
            String[] parts = inner.split(",");
            BigInteger col = BigInteger.ZERO;
            for (String part : parts) {
                String s = part.trim();
                if (s.isEmpty()) continue;
                int idx = Integer.parseInt(s);
                col = col.setBit(idx);
            }
            cols.add(col);
        }
        return cols;
    }

    static BigInteger parseTarget(String line) {
        Pattern p = Pattern.compile("\\[(.*?)]");
        Matcher m = p.matcher(line);
        if (m.find()) {
            String pat = m.group(1);
            BigInteger t = BigInteger.ZERO;
            for (int i = 0; i < pat.length(); i++) {
                char c = pat.charAt(i);
                if (c == '#') t = t.setBit(i);
            }
            return t;
        }
        return null;
    }

    static int solveMinPressesGF2(int m, int n, List<BigInteger> buttonCols, BigInteger target) {
        BigInteger[] rows = new BigInteger[m];
        boolean[] rhs = new boolean[m];
        for (int r = 0; r < m; r++) {
            BigInteger coeff = BigInteger.ZERO;
            for (int j = 0; j < n; j++) {
                if (buttonCols.get(j).testBit(r)) coeff = coeff.setBit(j);
            }
            rows[r] = coeff;
            rhs[r] = target.testBit(r);
        }

        int row = 0;
        int col = 0;
        int[] pivotColForRow = new int[Math.min(m, n)];
        Arrays.fill(pivotColForRow, -1);

        while (row < m && col < n) {
            int sel = -1;
            for (int r = row; r < m; r++) {
                if (rows[r].testBit(col)) {
                    sel = r;
                    break;
                }
            }
            if (sel == -1) {
                col++;
                continue;
            }
            BigInteger tmp = rows[sel];
            rows[sel] = rows[row];
            rows[row] = tmp;
            boolean ttmp = rhs[sel];
            rhs[sel] = rhs[row];
            rhs[row] = ttmp;

            for (int r = 0; r < m; r++) {
                if (r != row && rows[r].testBit(col)) {
                    rows[r] = rows[r].xor(rows[row]);
                    rhs[r] = rhs[r] ^ rhs[row];
                }
            }
            pivotColForRow[row] = col;
            row++;
            col++;
        }
        int rank = row;

        for (int r = rank; r < m; r++) {
            if (rows[r].equals(BigInteger.ZERO) && rhs[r]) {
                return -1;
            }
        }

        boolean[] isPivotCol = new boolean[n];
        for (int r = 0; r < rank; r++) {
            isPivotCol[pivotColForRow[r]] = true;
        }
        List<Integer> freeCols = new ArrayList<>();
        for (int j = 0; j < n; j++) if (!isPivotCol[j]) freeCols.add(j);
        int k = freeCols.size();

        BigInteger x0 = BigInteger.ZERO;
        for (int r = rank - 1; r >= 0; r--) {
            int pc = pivotColForRow[r];
            BigInteger coeffRow = rows[r].clearBit(pc);
            int dot = coeffRow.and(x0).bitCount() & 1;
            int val = (rhs[r] ? 1 : 0) ^ dot;
            if (val == 1) x0 = x0.setBit(pc);
        }

        List<BigInteger> basis = new ArrayList<>();
        for (int free : freeCols) {
            BigInteger v = BigInteger.ZERO.setBit(free);
            for (int r = rank - 1; r >= 0; r--) {
                int pc = pivotColForRow[r];
                BigInteger coeffWithoutPivot = rows[r].clearBit(pc);
                int dot = coeffWithoutPivot.and(v).bitCount() & 1;
                if (dot == 1) v = v.setBit(pc);
            }
            basis.add(v);
        }

        if (k <= 26) {
            int min = Integer.MAX_VALUE;
            int combos = 1 << k;
            for (int mask = 0; mask < combos; mask++) {
                BigInteger s = BigInteger.ZERO;
                for (int i = 0; i < k; i++) if (((mask >> i) & 1) == 1) s = s.xor(basis.get(i));
                BigInteger cand = x0.xor(s);
                int w = cand.bitCount();
                if (w < min) min = w;
            }
            return min == Integer.MAX_VALUE ? -1 : min;
        } else {
            int k1 = k / 2;
            int k2 = k - k1;
            List<BigInteger> b1 = basis.subList(0, k1);
            List<BigInteger> b2 = basis.subList(k1, k);

            Map<BigInteger, Integer> map = new HashMap<>();
            int combos1 = 1 << k1;
            for (int mask = 0; mask < combos1; mask++) {
                BigInteger s = BigInteger.ZERO;
                for (int i = 0; i < k1; i++) if (((mask >> i) & 1) == 1) s = s.xor(b1.get(i));
                int w = s.bitCount();
                Integer prev = map.get(s);
                if (prev == null || w < prev) map.put(s, w);
            }

            int min = Integer.MAX_VALUE;
            int combos2 = 1 << k2;
            for (int mask = 0; mask < combos2; mask++) {
                BigInteger s2 = BigInteger.ZERO;
                for (int i = 0; i < k2; i++) if (((mask >> i) & 1) == 1) s2 = s2.xor(b2.get(i));
                BigInteger target2 = x0.xor(s2);
                for (Map.Entry<BigInteger, Integer> e : map.entrySet()) {
                    BigInteger s1 = e.getKey();
                    BigInteger cand = target2.xor(s1);
                    int w = cand.bitCount();
                    if (w < min) min = w;
                }
            }
            return min == Integer.MAX_VALUE ? -1 : min;
        }
    }
}
