package jon.obid.y25.d3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class P2 {
    public static void main(String[] args) {
        System.out.println(p2());
    }

    public static BigInteger p2() {

        BigInteger total = BigInteger.ZERO;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(P1.class.getClassLoader().getResourceAsStream("y25/d3/input.txt")))) {
            String line;

            while ((line = br.readLine()) != null) {
                total = total.add(new BigInteger(p2(line)));
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return total;
    }

    private static String p2(String s) {
        int n = s.length();
        if (12 == n) return s;
        StringBuilder sb = new StringBuilder(12);
        int start = 0;
        for (int x = 12; x > 0; x--) {
            int end = n - x;
            char bc = '0' - 1;
            int bi = start;
            for (int i = start; i <= end; i++) {
                char c = s.charAt(i);
                if (c > bc) {
                    bc = c;
                    bi = i;
                    if (bc == '9') break;
                }
            }
            sb.append(bc);
            start = bi + 1;
        }
        return sb.toString();
    }
}
