package org.example.PathPrecomputation;

import org.example.PathMatching.BinaryPathMatcher.BinaryConverter;
import org.example.PathPrecomputation.PathWriter;

import java.io.*;

public class BinaryPathWriter extends DataStreamWriter {
    private final BinaryConverter converter = new BinaryConverter();

    public BinaryPathWriter(String fileName) {
        super(fileName);
    }

    @Override
    public void onPathFound(String path) {
        try {
            long[] binArray = converter.toBinArray(path);

            // Write each long value to the stream
            for (long value : binArray) {
                getDataStream().writeLong(value);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to write path to file: " + fileName, e);
        }
    }
}