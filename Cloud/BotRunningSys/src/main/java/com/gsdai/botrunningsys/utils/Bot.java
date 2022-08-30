package com.gsdai.botrunningsys.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bot implements java.util.function.Supplier<Integer> {

    static class Cell {

        public int x, y;
        public static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public boolean checkTailInc(int step) {
        return step <= 10 || step % 3 == 1;
    }

    public List<Cell> getCells(int sx, int sy, String steps) {
        steps = steps.substring(1, steps.length() - 1);
        List<Cell> cells = new ArrayList<>();

        int round = 0;
        int x = sx, y = sy;
        cells.add(new Cell(x, y));
        for (int i = 0; i < steps.length(); i++) {
            int d = steps.charAt(i) - '0';
            x += Cell.dx[d];
            y += Cell.dy[d];
            cells.add(new Cell(x, y));
            if (!this.checkTailInc(++round))
                cells.remove(0);
        }

        return cells;
    }

    public Integer nextMove(String input) {
        String[] strings = input.split("#");
        int[][] map = new int[13][14];
        for (int i = 0, k = 0; i < 13; i++) {
            for (int j = 0; j < 14; j++, k++) {
                if (strings[0].charAt(k) == '1') {
                    map[i][j] = 1;
                }
            }
        }

        int aSx = Integer.parseInt(strings[1]), aSy = Integer.parseInt(strings[2]);
        int bSx = Integer.parseInt(strings[4]), bSy = Integer.parseInt(strings[5]);

        List<Cell> aCells = getCells(aSx, aSy, strings[3]);
        List<Cell> bCells = getCells(bSx, bSy, strings[6]);
        for (Cell c : aCells) map[c.x][c.y] = 1;
        for (Cell c : bCells) map[c.x][c.y] = 1;
        for (int i = 0; i < 4; i++) {
            int x = aCells.get(aCells.size() - 1).x + Cell.dx[i];
            int y = aCells.get(aCells.size() - 1).y + Cell.dy[i];
            if (x >= 0 && x < 13 && y >= 0 && y < 14 && map[x][y] == 0)
                return i;
        }

        return 0;
    }

    @Override
    public Integer get() {
        File file = new File("input.txt");
        try {
            Scanner sc = new Scanner(file);
            return nextMove(sc.next());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
