package org.example.PathMatching;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PathWriter {

    // File to store the paths
    private String fileName;

    // Total number of paths
    private int pathCount = 500_000;

    // Length of each path
    private int length = 63;

    public PathWriter(String fileName) {
        this.fileName = fileName;
    }

    public PathWriter(String fileName, int pathCount) {
        this.fileName = fileName;
        this.pathCount = pathCount;
    }

    public PathWriter(String fileName, int pathCount, int length) {
        this.fileName = fileName;
        this.pathCount = pathCount;
        this.length = length;
    }

    // Write paths to file
    // Will NOT write when file already exists
    public void writePaths() {
        File file = new File(fileName);
        if (file.exists()) {
            System.out.println("Path file already exists at " + fileName + ". Skipping path writing.");
            return;
        }

        System.out.println("Path file not found at " + fileName + ". Trying to write to paths.");
        forceWritePaths();
    }

    // Write paths to file regardless if file exists
    public void forceWritePaths() {
        PathGenerator pathGenerator = new PathGenerator(length);
        try {
            FileWriter myWriter = new FileWriter(fileName);

            for (int i = 0; i < pathCount; i++) {
                myWriter.write(pathGenerator.randomPath());
                myWriter.append('\n');
            }

            myWriter.close();
            System.out.println("Successfully wrote " + pathCount + " paths to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
