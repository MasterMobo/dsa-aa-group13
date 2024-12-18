package org.example.PathPrecomputation;

import org.example.PathPrecomputation.PathWriter.PathWriter;

import java.io.File;

// Class to perform pre-computation
public class PathPrecomputor {
    private final PathWriter writer;

    public PathPrecomputor(PathWriter writer) {
        this.writer = writer;
    }

    // Perform pre-computation
    // If path does not exist, run the grid path solver and write paths to file.
    // If path already exists, does nothing.
    public void precompute() {
        String fileName = writer.getFileName();

        File f = new File(fileName);
        if (f.exists() && !f.isDirectory()) {
            System.out.println("Path file already exists at " + fileName + ". Skipping path generation.");
            return;
        }

        System.out.println("Path file not found at " + fileName + ". Generating paths.");

        long t1 = System.currentTimeMillis();
        GridPathSolver solver = new GridPathSolver(writer);
        int pathsFound = solver.solve();
        long t2 = System.currentTimeMillis();

        System.out.println("Finding and Storing time (ms): " + (t2 - t1));
        System.out.println("Total paths found: " + pathsFound);

        System.out.println("Path file saved at " + fileName);
    }

}
