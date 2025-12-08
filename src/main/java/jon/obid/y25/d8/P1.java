package jon.obid.y25.d8;

import java.util.*;
import java.io.*;

public class P1 {

    public static void main(String[] args) throws Exception {
        List<int[]> points = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("D:/algo/AoC/src/main/resources/y25/d8/input.txt"))) {
            String line;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                String[] p = line.split(",");
                points.add(new int[]{
                        Integer.parseInt(p[0].trim()),
                        Integer.parseInt(p[1].trim()),
                        Integer.parseInt(p[2].trim())
                });
            }
        }

        int n = points.size();
        DSU dsu = new DSU(n);

        List<Pair> pairs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int[] A = points.get(i);
            for (int j = i + 1; j < n; j++) {
                int[] B = points.get(j);
                long dx = A[0] - B[0];
                long dy = A[1] - B[1];
                long dz = A[2] - B[2];
                long dist2 = dx*dx + dy*dy + dz*dz;
                pairs.add(new Pair(dist2, i, j));
            }
        }

        pairs.sort(Comparator.comparingLong(p -> p.dist2));

        int k = 1000;
        for (int i = 0; i < Math.min(k, pairs.size()); i++) {
            Pair p = pairs.get(i);
            dsu.union(p.a, p.b);
        }

        Map<Integer, Integer> comp = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int r = dsu.find(i);
            comp.put(r, dsu.size[r]);
        }

        List<Integer> sizes = new ArrayList<>(comp.values());
        sizes.sort(Comparator.reverseOrder());

        while (sizes.size() < 3) sizes.add(1);

        long result = (long) sizes.get(0) * sizes.get(1) * sizes.get(2);

        System.out.println(result);
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
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }
        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;
            if (size[ra] < size[rb]) {
                int tmp = ra;
                ra = rb;
                rb = tmp;
            }
            parent[rb] = ra;
            size[ra] += size[rb];
        }

    }
    static class Pair {
        long dist2;
        int a, b;
        Pair(long d, int a, int b) {
            this.dist2 = d;
            this.a = a;
            this.b = b;
        }

    }
}
