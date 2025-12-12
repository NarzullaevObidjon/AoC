package jon.obid.y25.d12;

import java.io.*;
import java.util.*;

public class P1 {

    static class Shape {
        int[][] grid;
        int hash;
        int height, width;
        List<int[]> cells;

        Shape(int[][] grid) {
            this.grid = grid;
            this.hash = Arrays.deepHashCode(grid);
            this.height = grid.length;
            this.width = grid[0].length;
            this.cells = new ArrayList<>();
            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {
                    if (grid[r][c] == 1) {
                        cells.add(new int[]{r, c});
                    }
                }
            }
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Shape)) return false;
            return Arrays.deepEquals(grid, ((Shape)o).grid);
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }

    static class Region {
        int width, height;
        int[] quantities;

        Region(int width, int height, int[] quantities) {
            this.width = width;
            this.height = height;
            this.quantities = quantities;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("D:\\algo\\AoC\\src\\main\\resources\\y25\\d12\\input.txt"));
        List<Shape> shapes = new ArrayList<>();
        List<Region> regions = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.matches("\\d+:")) {
                List<String> shapeLines = new ArrayList<>();
                while ((line = br.readLine()) != null && !line.trim().isEmpty()
                        && !line.contains(":")) {
                    shapeLines.add(line.trim());
                }
                shapes.add(parseShape(shapeLines));

                if (line != null && !line.trim().isEmpty()) {
                    if (line.matches("\\d+x\\d+:.*")) {
                        regions.add(parseRegion(line));
                    }
                }
            } else if (line.matches("\\d+x\\d+:.*")) {
                regions.add(parseRegion(line));
            }
        }

        int fittingRegions = 0;
        for (int i = 0; i < regions.size(); i++) {
            Region region = regions.get(i);
            boolean fits = canFitAllPresents(region, shapes);
            if (fits) fittingRegions++;
        }

        System.out.println(fittingRegions);
    }

    static Shape parseShape(List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).length();
        int[][] grid = new int[rows][cols];

        for (int r = 0; r < rows; r++) {
            String line = lines.get(r);
            for (int c = 0; c < line.length(); c++) {
                grid[r][c] = line.charAt(c) == '#' ? 1 : 0;
            }
        }
        return new Shape(grid);
    }

    static Region parseRegion(String line) {
        String[] parts = line.split(":");
        String[] dims = parts[0].split("x");
        int width = Integer.parseInt(dims[0]);
        int height = Integer.parseInt(dims[1]);

        String[] countStrs = parts[1].trim().split("\\s+");
        int[] quantities = new int[countStrs.length];
        for (int i = 0; i < countStrs.length; i++) {
            quantities[i] = Integer.parseInt(countStrs[i]);
        }

        return new Region(width, height, quantities);
    }

    static boolean canFitAllPresents(Region region, List<Shape> shapes) {
        int totalArea = 0;
        for (int shapeIdx = 0; shapeIdx < region.quantities.length; shapeIdx++) {
            totalArea += getShapeSize(shapes.get(shapeIdx)) * region.quantities[shapeIdx];
        }
        if (totalArea > region.width * region.height) {
            return false;
        }

        List<List<Shape>> allRotations = new ArrayList<>();
        for (Shape shape : shapes) {
            allRotations.add(getUniqueRotations(shape));
        }

        List<int[]> presentsToPlace = new ArrayList<>();
        int presentId = 1;
        for (int shapeIdx = 0; shapeIdx < region.quantities.length; shapeIdx++) {
            for (int q = 0; q < region.quantities[shapeIdx]; q++) {
                presentsToPlace.add(new int[]{shapeIdx, presentId++});
            }
        }

        presentsToPlace.sort((a, b) -> {
            int sizeA = getShapeSize(shapes.get(a[0]));
            int sizeB = getShapeSize(shapes.get(b[0]));
            return Integer.compare(sizeB, sizeA);
        });

        int[][] grid = new int[region.height][region.width];
        return solve(grid, allRotations, presentsToPlace, 0);
    }

    static int getShapeSize(Shape shape) {
        return shape.cells.size();
    }

    static boolean solve(int[][] grid, List<List<Shape>> allRotations,
                         List<int[]> presentsToPlace, int index) {
        if (index == presentsToPlace.size()) {
            return true;
        }

        int shapeIdx = presentsToPlace.get(index)[0];
        int presentId = presentsToPlace.get(index)[1];

        List<Shape> rotations = allRotations.get(shapeIdx);

        int startRow = 0, startCol = 0;
        outer:
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c] == 0) {
                    startRow = r;
                    startCol = c;
                    break outer;
                }
            }
        }

        for (Shape rotation : rotations) {
            for (int row = startRow; row <= grid.length - rotation.height; row++) {
                int colStart = (row == startRow) ? startCol : 0;
                for (int col = colStart; col <= grid[0].length - rotation.width; col++) {
                    if (canPlaceFast(grid, rotation, row, col)) {
                        placeFast(grid, rotation, row, col, presentId);
                        if (solve(grid, allRotations, presentsToPlace, index + 1)) {
                            return true;
                        }
                        unplaceFast(grid, rotation, row, col);
                    }
                }
            }
        }

        return false;
    }

    static boolean canPlaceFast(int[][] grid, Shape shape, int row, int col) {
        for (int[] cell : shape.cells) {
            if (grid[row + cell[0]][col + cell[1]] != 0) {
                return false;
            }
        }
        return true;
    }

    static void placeFast(int[][] grid, Shape shape, int row, int col, int id) {
        for (int[] cell : shape.cells) {
            grid[row + cell[0]][col + cell[1]] = id;
        }
    }

    static void unplaceFast(int[][] grid, Shape shape, int row, int col) {
        for (int[] cell : shape.cells) {
            grid[row + cell[0]][col + cell[1]] = 0;
        }
    }

    static List<Shape> getUniqueRotations(Shape shape) {
        Set<Shape> unique = new LinkedHashSet<>();
        int[][] orig = shape.grid;

        unique.add(new Shape(orig));
        unique.add(new Shape(rotate90(orig)));
        unique.add(new Shape(rotate180(orig)));
        unique.add(new Shape(rotate270(orig)));

        int[][] flipped = flipHorizontal(orig);
        unique.add(new Shape(flipped));
        unique.add(new Shape(rotate90(flipped)));
        unique.add(new Shape(rotate180(flipped)));
        unique.add(new Shape(rotate270(flipped)));

        return new ArrayList<>(unique);
    }

    static int[][] rotate90(int[][] m) {
        int rows = m.length, cols = m[0].length;
        int[][] r = new int[cols][rows];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                r[j][rows - 1 - i] = m[i][j];
        return r;
    }

    static int[][] rotate180(int[][] m) {
        int rows = m.length, cols = m[0].length;
        int[][] r = new int[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                r[rows - 1 - i][cols - 1 - j] = m[i][j];
        return r;
    }

    static int[][] rotate270(int[][] m) {
        int rows = m.length, cols = m[0].length;
        int[][] r = new int[cols][rows];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                r[cols - 1 - j][i] = m[i][j];
        return r;
    }

    static int[][] flipHorizontal(int[][] m) {
        int rows = m.length, cols = m[0].length;
        int[][] r = new int[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                r[i][cols - 1 - j] = m[i][j];
        return r;
    }
}