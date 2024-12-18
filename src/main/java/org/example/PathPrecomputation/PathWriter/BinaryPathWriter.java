package org.example.PathPrecomputation.PathWriter;

import org.example.PathMatching.BinaryPathMatcher.BinaryConverter;

import java.io.*;


// Class to write paths as binary representation to a file
public class BinaryPathWriter implements PathWriter {
    private final String fileName;
    private DataOutputStream dataStream = null;
    private final BinaryConverter converter = new BinaryConverter();

    public BinaryPathWriter(String fileName) {
        this.fileName = fileName;
    }

    // Converts the path to binary, then write it to a binary file
    // Time Complexity: O(N)
    //  - N is the length of the path
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

    // Clean-up after finish writing
    @Override
    public void onSearchComplete() {
        try {
            getDataStream().flush();
            getDataStream().close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to close writer for file: " + fileName, e);
        }
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    private DataOutputStream getDataStream() {
        if (dataStream == null) initializeDataStream();
        return dataStream;
    }

    private void initializeDataStream() {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            this.dataStream = new DataOutputStream(bos);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize writer for file: " + fileName, e);
        }
    }
}