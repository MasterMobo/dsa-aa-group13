package org.example.PathMatching.BinaryPathMatcher;

import org.example.PathPrecomputation.GridPathSolver;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

// Class to read binary file into a buffer
public class BinaryPathReader {
    private String fileName;

    private ByteBuffer buffer = null;

    public BinaryPathReader(String fileName) {
        this.fileName = fileName;
    }

    public ByteBuffer getBuffer() {
        if (buffer == null) initializeBuffer();
        buffer.rewind(); // Prepare buffer for reading

        return buffer;
    }

    public void initializeBuffer() {
        try (FileChannel channel = new FileInputStream(fileName).getChannel()) {
            long t1, t2;

            t1 = System.currentTimeMillis();

            // Allocate a ByteBuffer for the entire file
            buffer = ByteBuffer.allocate((int) channel.size());

            // Read the whole file into the buffer
            channel.read(buffer);
            buffer.flip(); // Prepare buffer for reading

            t2 = System.currentTimeMillis();

            System.out.println("Reading time (ms): " + (t2 - t1));
            System.out.println("Buffer size: " + buffer.capacity()/ (1024.0 * 1024.0) + " MB");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
