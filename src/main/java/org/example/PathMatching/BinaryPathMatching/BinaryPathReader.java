package org.example.PathMatching.BinaryPathMatching;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class BinaryPathReader {
    private BinaryPathWriter writer;
    private ByteBuffer buffer = null;

    public BinaryPathReader(BinaryPathWriter writer) {
        writer.writeToFile();

        try (FileChannel channel = new FileInputStream(writer.getFileName()).getChannel()) {
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

    public ByteBuffer getBuffer() {
        buffer.rewind(); // Prepare buffer for reading
        return buffer;
    }
}
