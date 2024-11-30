package org.example.PathMatching;

import java.io.*;

public class PathReader {
    private String fileName;
    private BufferedReader reader;


    public PathReader(String fileName) {
        this.fileName = fileName;
        reader = getReader();
    }

    private BufferedReader getReader() {
        PathWriter writer = new PathWriter(fileName);
        writer.writePaths();

        try {
            return new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public String nextLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            return null;
        }
    }
}
