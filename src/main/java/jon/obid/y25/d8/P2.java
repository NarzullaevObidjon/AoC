package jon.obid.y25.d8;

import java.io.*;
import java.util.*;

public class P2 {
    public static void main(String[] args) throws Exception {
        List<int[]> points = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("D:/algo/AoC/src/main/resources/y25/d8/input.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 3) continue;
                int x = Integer.parseInt(parts[0].trim());
                int y = Integer.parseInt(parts[1].trim());
                int z = Integer.parseInt(parts[2].trim());
                points.add(new int[]{x, y, z});
            }
        }

        int n = points.size();
        List<Pair> pairs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int[] A = points.get(i);
            for (int j = i + 1; j < n; j++) {
                int[] B = points.get(j);
                long dx = (long)A[0] - B[0];
                long dy = (long)A[1] - B[1];
                long dz = (long)A[2] - B[2];
                long d2 = dx*dx + dy*dy + dz*dz;
                pairs.add(new Pair(d2, i, j));
            }
        }

        pairs.sort(Comparator.comparingLong(p -> p.dist2));

        DSU dsu = new DSU(n);
        int components = n;

        for (Pair p : pairs) {
            int a = p.a, b = p.b;
            boolean merged = dsu.union(a, b);
            if (merged) {
                components--;
                if (components == 1) {
                    long productX = (long)points.get(a)[0] * (long)points.get(b)[0];
                    System.out.println(productX);
                    return;
                }
            }
        }
    }

    static class DSU {
        int[] parent;
        int[] size;
        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }
        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }
        boolean union(int a, int b) {
            int ra = find(a), rb = find(b);
            if (ra == rb) return false;
            if (size[ra] < size[rb]) {
                int t = ra; ra = rb; rb = t;
            }
            parent[rb] = ra;
            size[ra] += size[rb];
            size[rb] = 0;
            return true;
        }
    }

    static class Pair {
        long dist2;
        int a, b;
        Pair(long d, int a, int b) { dist2 = d; this.a = a; this.b = b; }
    }
}
