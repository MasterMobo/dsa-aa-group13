package org.example.PathPrecomputation;

import java.io.IOException;

public class StringPathWriter extends DataStreamWriter{
    public StringPathWriter(String fileName) {
        super(fileName);
    }

    @Override
    public void onPathFound(String path) {
        try {
            getDataStream().writeChars(path + '\n');
        } catch (IOException e) {
            throw new RuntimeException("Failed to write path to file: " + fileName, e);
        }
    }
}
