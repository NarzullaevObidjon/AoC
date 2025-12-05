package jon.obid.y25.d5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class P1 {
    public static void main(String[] args) throws IOException {
        System.out.println(p1());
    }

    public static long p1() {
        List<long[]> ranges = new ArrayList<>();
        List<Long> ids = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(P1.class.getClassLoader()
                        .getResourceAsStream("y25/d5/input.txt")))) {

            String line;
            boolean readingRanges = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    readingRanges = false;
                    continue;
                }

                if (readingRanges) {
                    String[] parts = line.split("-");
                    ranges.add(new long[]{Long.parseLong(parts[0]), Long.parseLong(parts[1])});
                } else {
                    ids.add(Long.parseLong(line));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        long count = 0;

        for (Long id : ids) {
            for (long[] r : ranges) {
                if (id >= r[0] && id <= r[1]) {
                    count++;
                    break;
                }
            }
        }

        return count;
    }

}
