package jon.obid.y25.d6;

import java.io.*;
import java.util.*;

public class P1 {

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

        for (int col = 0; col < width; col++) {
            boolean empty = true;
            for (String l : lines) {
                if (l.charAt(col) != ' ') {
                    empty = false;
                    break;
                }
            }
            if (empty) continue;

            int start = col;
            while (col < width) {
                empty = true;
                for (String l : lines) {
                    if (l.charAt(col) != ' ') {
                        empty = false;
                        break;
                    }
                }
                if (empty) break;
                col++;
            }
            int end = col;

            List<Long> nums = new ArrayList<>();
            char op = ' ';

            for (String l : lines) {
                String segment = l.substring(start, end).trim();
                if (segment.isEmpty()) continue;

                if (segment.equals("+") || segment.equals("*")) {
                    op = segment.charAt(0);
                } else {
                    nums.add(Long.parseLong(segment));
                }
            }

            if (!nums.isEmpty() && op != ' ') {
                long result = nums.get(0);
                for (int i = 1; i < nums.size(); i++) {
                    if (op == '+') result += nums.get(i);
                    else result *= nums.get(i);
                }
                grandTotal += result;
            }
        }

        System.out.println(grandTotal);
    }
}