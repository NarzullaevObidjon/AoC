package jon.obid.y25.d5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class P2 {
    public static void main(String[] args) throws IOException {
        System.out.println(p2());
    }

    public static long p2() throws IOException {
        List<long[]> ranges = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(P1.class.getClassLoader().getResourceAsStream("y25/d5/input.txt")))) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) break;
                String[] parts = line.split("-");
                ranges.add(new long[]{
                        Long.parseLong(parts[0]),
                        Long.parseLong(parts[1])
                });
            }
        }

        ranges.sort(Comparator.comparingLong(a -> a[0]));

        List<long[]> merged = new ArrayList<>();

        long[] current = ranges.get(0);

        for (int i = 1; i < ranges.size(); i++) {
            long[] next = ranges.get(i);

            if (next[0] <= current[1] + 1) {
                current[1] = Math.max(current[1], next[1]);
            } else {
                merged.add(current);
                current = next;
            }
        }
        merged.add(current);

        long total = 0;
        for (long[] r : merged) {
            total += (r[1] - r[0] + 1);
        }

        return total;
    }

}
