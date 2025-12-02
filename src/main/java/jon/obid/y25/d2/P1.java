package jon.obid.y25.d2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class P1 {
    public static void main(String[] args) {
        System.out.println(p1());
    }

    public static long p1() {
        long sum = 0;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        P1.class.getClassLoader().getResourceAsStream("y25/d2/input.txt")
                ))) {

            String line = br.readLine();
            String[] pairs = line.split(",");

            for (String pair : pairs) {
                String[] range = pair.split("-");
                long start = Long.parseLong(range[0]);
                long end = Long.parseLong(range[1]);

                for (long i = start; i <= end; i++) {
                    if (isInvalid(i)) {
                        sum += i;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sum;
    }

    private static boolean isInvalid(long n) {
        String s = Long.toString(n);
        int len = s.length();
        if (len % 2 != 0) return false;

        String left = s.substring(0, len / 2);
        String right = s.substring(len / 2);

        return left.equals(right);
    }

}
