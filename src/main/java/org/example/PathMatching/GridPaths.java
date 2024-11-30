package org.example.PathMatching;

// credits: https://www.geeksforgeeks.org/cses-solutions-grid-paths/

public class GridPaths {
        // Constants
        private static final int DIR_LEN = 4;
        private static final int[] dr = {-1, 0, 1, 0};
        private static final int[] dc = {0, 1, 0, -1};
        private static final int PATH_LEN = 63; // Length of all possible paths
        private static final int GRID_SIZE = 10; // Two extra cells for the borders
        private static final int N = 8;


    // Variables
        private static int[] p = new int[PATH_LEN];
        private static boolean[][] onPath = new boolean[GRID_SIZE][GRID_SIZE];

        public static int tryPath(int pathIdx, int curR, int curC) {
            // Optimization 3
            if ((onPath[curR][curC - 1] && onPath[curR][curC + 1]) &&
                    (!onPath[curR - 1][curC] && !onPath[curR + 1][curC]))
                return 0;
            if ((onPath[curR - 1][curC] && onPath[curR + 1][curC]) &&
                    (!onPath[curR][curC - 1] && !onPath[curR][curC + 1]))
                return 0;

            if (curR == N && curC == 1) { // Reached endpoint before visiting all
                if (pathIdx == PATH_LEN) return 1;
                return 0;
            }

            if (pathIdx == PATH_LEN) return 0;

            int ret = 0;
            onPath[curR][curC] = true;

            // Turn already determined:
            if (p[pathIdx] < 4) {
                int nxtR = curR + dr[p[pathIdx]];
                int nxtC = curC + dc[p[pathIdx]];
                if (!onPath[nxtR][nxtC]) ret += tryPath(pathIdx + 1, nxtR, nxtC);
            } else { // Iterate through all four possible turns
                for (int i = 0; i < DIR_LEN; i++) {
                    int nxtR = curR + dr[i];
                    int nxtC = curC + dc[i];
                    if (onPath[nxtR][nxtC]) continue;
                    ret += tryPath(pathIdx + 1, nxtR, nxtC);
                }
            }

            // Reset and return
            onPath[curR][curC] = false;
            return ret;
        }

        public static void main(String[] args) {
            String line = "*****DR******R******R********************R*D************L******";

            // Convert path to ints
            for (int i = 0; i < PATH_LEN; i++) {
                char cur = line.charAt(i);
                if (cur == 'U') p[i] = 0;
                else if (cur == 'R') p[i] = 1;
                else if (cur == 'D') p[i] = 2;
                else if (cur == 'L') p[i] = 3;
                else p[i] = 4; // cur == '?'
            }

            // Set borders of grid
            for (int i = 0; i < GRID_SIZE; i++) {
                onPath[0][i] = true;
                onPath[9][i] = true;
                onPath[i][0] = true;
                onPath[i][9] = true;
            }

            // Initialize the inside of the grid to be completely empty
            for (int i = 1; i <= N; i++) {
                for (int j = 1; j <= N; j++) {
                    onPath[i][j] = false;
                }
            }

            int startIdx = 0;
            int startR = 1;
            int startC = 1; // Always start path at (1, 1)

            long startTime = System.currentTimeMillis();
            int ans = tryPath(startIdx, startR, startC);
            long duration = System.currentTimeMillis() - startTime;

            System.out.println(ans);
            System.out.println("Took " + duration + " ms");
        }
    }
//This code is contrbiuted by Utkarsh
