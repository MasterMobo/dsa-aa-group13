package org.example.PathPrecomputation;

public class GridPathSolver {
    private final int N = 8; // Size of the input N x N grid

    // Offset positions for directions U, R, D, L respectively
    private final int[] rowOffset = {-1, 0, 1, 0};
    private final int[] columnOffset = {0, 1, 0, -1};

    private final int PATH_LEN = 63; // Length of all possible paths
    private final int GRID_SIZE = 10; // Two extra cells for the borders

    // Current path of traversal
    private final int[] currentPath = new int[PATH_LEN];

    // Keeps track of which cell is already visited
    private final boolean[][] onPath = new boolean[GRID_SIZE][GRID_SIZE];

    // Writer to write paths to file
    private final PathWriter pathWriter;

    public GridPathSolver(PathWriter pathWriter) {
        this.pathWriter = pathWriter;
    }

    // Backtracking method to find and store valid paths
    // Time complexity: O(4^N)
    private int tryPath(int pathIdx, int currentRow, int currentColumn, StringBuilder path) {
        // Optimization checks (for valid path constraints)
        if ((onPath[currentRow][currentColumn - 1] && onPath[currentRow][currentColumn + 1])
                &&
            (!onPath[currentRow - 1][currentColumn] && !onPath[currentRow + 1][currentColumn]))
            return 0;

        if ((onPath[currentRow - 1][currentColumn] && onPath[currentRow + 1][currentColumn])
                &&
            (!onPath[currentRow][currentColumn - 1] && !onPath[currentRow][currentColumn + 1]))
            return 0;

        // If we reach the endpoint and the path length is correct, save the path
        if (currentRow == N && currentColumn == 1) { // Reached endpoint (N, 1)
            if (pathIdx != PATH_LEN) return 0;

            // Notify writer about found path
            pathWriter.onPathFound(path.toString());
            return 1;
        }

        // If the path is too long, stop
        if (pathIdx == PATH_LEN) return 0;

        int res = 0;
        onPath[currentRow][currentColumn] = true;

        // Turn already determined:
        if (currentPath[pathIdx] < 4) {
            int nxtR = currentRow + rowOffset[currentPath[pathIdx]];
            int nxtC = currentColumn + columnOffset[currentPath[pathIdx]];

            // Append current direction
            path.append("URDL".charAt(currentPath[pathIdx]));

            if (!onPath[nxtR][nxtC])
                res += tryPath(pathIdx + 1, nxtR, nxtC, path);

            // Backtracking
            path.deleteCharAt(path.length() - 1);
        } else { // Iterate through all four possible turns
            for (int i = 0; i < 4; i++) {
                int nextRow = currentRow + rowOffset[i];
                int nextColumn = currentColumn + columnOffset[i];

                if (onPath[nextRow][nextColumn]) continue;

                // Append current direction
                path.append("URDL".charAt(i));
                res += tryPath(pathIdx + 1, nextRow, nextColumn, path);

                // Backtracking
                path.deleteCharAt(path.length() - 1);
            }
        }

        // Reset and return
        onPath[currentRow][currentColumn] = false;
        return res;
    }

    // Set up the grid for traversal
    private void setupGrid() {
        // Set the input path to all wildcards so that we explore all paths
        for (int i = 0; i < PATH_LEN; i++) {
            currentPath[i] = 4; // wildcard
        }

        // Set borders of the grid
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
    }

    public int solve() {
        setupGrid();

        int startIdx = 0;
        // Always start path at (1, 1)
        int startR = 1;
        int startC = 1;
        StringBuilder path = new StringBuilder();

        int res = tryPath(startIdx, startR, startC, path);

        // Notify writer that search has complete (for clean-up)
        pathWriter.onSearchComplete();

        return res;
    }
}

