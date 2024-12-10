import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// import org.example.PathMatching.BinaryPathMatching.BinaryConverter;

public class StoreToBinaryFile {
    // Constants
    private static final int DIR_LEN = 4;
    private static final int[] dr = {-1, 0, 1, 0};
    private static final int[] dc = {0, 1, 0, -1};
    private static final int PATH_LEN = 63; // Length of all possible paths
    private static final int GRID_SIZE = 10; // Two extra cells for the borders
    private static final int N = 8; // Path generation grid size
    private static final String FILE_NAME = "validPaths.bin"; // Output binary file location

    // Variables
    private static int[] p = new int[PATH_LEN];
    private static boolean[][] onPath = new boolean[GRID_SIZE][GRID_SIZE];

    // BinaryConverter instance to convert valid paths to binary format
    private static BinaryConverter converter = new BinaryConverter();

    // Helper method to convert a path string to binary and write it to the file
    private static void writePathToBinaryFile(DataOutputStream writer, String path) throws IOException {
        // Convert the path to a long[] binary representation
        long[] binArray = converter.toBinArray(path);

        // Write each long value to the binary file
        for (long value : binArray) {
            writer.writeLong(value);
        }
    }

    // Backtracking method to find and store valid paths
    public static int tryPath(DataOutputStream writer, int pathIdx, int curR, int curC, StringBuilder path) throws IOException {
        // Optimization checks (for valid path constraints)
        if ((onPath[curR][curC - 1] && onPath[curR][curC + 1]) &&
                (!onPath[curR - 1][curC] && !onPath[curR + 1][curC]))
            return 0;
        if ((onPath[curR - 1][curC] && onPath[curR + 1][curC]) &&
                (!onPath[curR][curC - 1] && !onPath[curR][curC + 1]))
            return 0;

        // If we reach the endpoint and the path length is correct, save the path
        if (curR == N && curC == 1) { // Reached endpoint (N, 1)
            if (pathIdx == PATH_LEN) {
                writePathToBinaryFile(writer, path.toString()); // Write the path to the binary file
                return 1;
            }
            return 0;
        }

        if (pathIdx == PATH_LEN) return 0; // If the path is too long, stop

        int ret = 0;
        onPath[curR][curC] = true;

        // Turn already determined:
        if (p[pathIdx] < 4) {
            int nxtR = curR + dr[p[pathIdx]];
            int nxtC = curC + dc[p[pathIdx]];
            path.append("URDL".charAt(p[pathIdx])); // Append current direction
            if (!onPath[nxtR][nxtC]) ret += tryPath(writer, pathIdx + 1, nxtR, nxtC, path);
            path.deleteCharAt(path.length() - 1); // Backtracking
        } else { // Iterate through all four possible turns
            for (int i = 0; i < DIR_LEN; i++) {
                int nxtR = curR + dr[i];
                int nxtC = curC + dc[i];
                if (onPath[nxtR][nxtC]) continue;
                path.append("URDL".charAt(i)); // Append current direction
                ret += tryPath(writer, pathIdx + 1, nxtR, nxtC, path);
                path.deleteCharAt(path.length() - 1); // Backtracking
            }
        }

        // Reset and return
        onPath[curR][curC] = false;
        return ret;
    }

    // Method to check if the file exists
    public void checkingFile() {
        // Check if the binary file already exists
        if (Files.exists(Paths.get(FILE_NAME))) {
            System.out.println("The file " + FILE_NAME + " already exists. Skipping pathfinding.");
            return; // Exit the method if the file exists
        }

        System.out.println("No valid paths data found, now start to pre-caculating, please wait...");

        String line = "***************************************************************"; // Worst case line to convert to path

        // Convert path to integers for representation (e.g., U -> 0, R -> 1, D -> 2, L -> 3, ? -> 4)
        for (int i = 0; i < PATH_LEN; i++) {
            char cur = line.charAt(i);
            if (cur == 'U') p[i] = 0;
            else if (cur == 'R') p[i] = 1;
            else if (cur == 'D') p[i] = 2;
            else if (cur == 'L') p[i] = 3;
            else p[i] = 4; // cur == '?' - represent as 4 (wildcard)
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

        try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(FILE_NAME))) {
            long t1, t2;

            int startIdx = 0;
            int startR = 1;
            int startC = 1; // Always start path at (1, 1)
            StringBuilder path = new StringBuilder();

            t1 = System.currentTimeMillis();
            int ans = tryPath(writer, startIdx, startR, startC, path);

            t2 = System.currentTimeMillis();
            System.out.println("Finding and Storing time (ms): " + (t2 - t1));
            System.out.println("Total paths found: " + ans);
            System.out.println("All valid paths saved to " + FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileName() {
        return FILE_NAME;
    }
}

