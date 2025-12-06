package jon.obid.y25.d6;

import java.util.*;
import java.io.*;

public class P2 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("D:/algo/AoC/src/main/resources/y25/d6/input.txt"));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();

        int width = 0;
        for (String l : lines) width = Math.max(width, l.length());
        for (int i = 0; i < lines.size(); i++) {
            while (lines.get(i).length() < width) {
                lines.set(i, lines.get(i) + " ");
            }
        }

        long grandTotal = 0;

        for (int col = width - 1; col >= 0; col--) {
            boolean empty = true;
            for (String l : lines) {
                if (l.charAt(col) != ' ') {
                    empty = false;
                    break;
                }
            }
            if (empty) continue;

            int end = col;
            while (col >= 0) {
                empty = true;
                for (String l : lines) {
                    if (l.charAt(col) != ' ') {
                        empty = false;
                        break;
                    }
                }
                if (empty) break;
                col--;
            }
            int start = col + 1;

            List<Long> nums = new ArrayList<>();
            char op = ' ';

            for (int c = end; c >= start; c--) {
                String num = "";
                for (String l : lines) {
                    char ch = l.charAt(c);
                    if (ch == '+' || ch == '*') {
                        op = ch;
                    } else if (ch != ' ') {
                        num += ch;
                    }
                }
                if (!num.isEmpty()) {
                    nums.add(Long.parseLong(num));
                }
            }

            long result = nums.get(0);
            for (int i = 1; i < nums.size(); i++) {
                if (op == '+') result += nums.get(i);
                else result *= nums.get(i);
            }

            grandTotal += result;
        }

        System.out.println(grandTotal);
    }

}
