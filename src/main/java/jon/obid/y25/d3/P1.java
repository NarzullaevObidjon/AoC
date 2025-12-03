package jon.obid.y25.d3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class P1 {
    public static void main(String[] args) {
        System.out.println(p1());
    }

    public static long p1() {
        long total=0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(P1.class.getClassLoader().getResourceAsStream("y25/d3/input.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                total += p1(line.trim());
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        return total;
    }

    private static int p1(String s) {
        int max = 0;
        int n = s.length();

        for (int i = 0; i < n; i++) {
            int a = s.charAt(i) - '0';
            for (int j = i + 1; j < n; j++) {
                int b = s.charAt(j) - '0';
                int val = a * 10 + b;
                if (val > max) max = val;
            }
        }
        return max;
    }
}
