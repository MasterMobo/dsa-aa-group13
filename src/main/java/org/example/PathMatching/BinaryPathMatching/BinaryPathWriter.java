package org.example.PathMatching.BinaryPathMatching;

import org.example.PathMatching.PathGenerator;

import java.io.*;
import java.util.Random;

public class BinaryPathWriter {

    private int pathCount = 0;
    private String fileName;

    public BinaryPathWriter(String fileName, int pathCount) {
        this.fileName = fileName;
        this.pathCount = pathCount;
    }

    public void writeToFile() {
        BinaryConverter converter =  new BinaryConverter();

        File f = new File(fileName);
        if (f.exists() && !f.isDirectory()) {
            System.out.println("Path file already exists at " + fileName + ". Skipping path writing.");
            return;
        }

        System.out.println("Path file not found at " + fileName + ". Trying to write paths.");

        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(fileName)))) {

            PathGenerator generator = new PathGenerator(63);
            long t1, t2;

            t1 = System.currentTimeMillis();

            for (int i = 0; i < pathCount; i++) {
                // Generate a random string
                String randomString = generator.randomPath();

                // Convert the string to a long[] binary representation
                long[] binArray = converter.toBinArray(randomString);

                // Write each long in the array to the binary file
                for (long value : binArray) {
                    out.writeLong(value);
                }
            }

            t2 = System.currentTimeMillis();
            System.out.println("Successfully wrote " + pathCount + " paths to " + fileName);
            System.out.println("Writing time (ms): " + (t2 - t1));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileName() {
        return fileName;
    }
}
