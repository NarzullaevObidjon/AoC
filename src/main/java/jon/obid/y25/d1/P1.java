package jon.obid.y25.d1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class P1 {
    public static void main(String[] args) {
        System.out.println(p1());
    }

    public static int p1() {
        int currentPosition = 50;
        int zeroCount = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(P1.class.getClassLoader().getResourceAsStream("y25/d1/input.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                char direction = line.charAt(0);
                int distance = Integer.parseInt(line.substring(1));

                if (direction == 'L') {
                    currentPosition = (currentPosition - distance) % 100;
                    if (currentPosition < 0) {
                        currentPosition += 100;
                    }
                } else if (direction == 'R') {
                    currentPosition = (currentPosition + distance) % 100;
                }

                if (currentPosition == 0) {
                    zeroCount++;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        return zeroCount;
    }
}
