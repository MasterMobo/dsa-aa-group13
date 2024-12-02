package org.example.PathMatching.BinaryPathMatching;

import java.nio.ByteBuffer;

public class BinaryPathMatcher {

    private BinaryPathReader reader;

    public BinaryPathMatcher(BinaryPathReader reader) {
        this.reader = reader;
    }

    public int countMatches(String s) {
        ByteBuffer buffer = reader.getBuffer();
        if (buffer == null) {
            System.out.println("ERROR: Buffer not initialized");
            return 0;
        }

        BinaryConverter converter = new BinaryConverter();

        long[] input = converter.toBinArray(s);
        long[] target = new long[4];
        int matchCount = 0;

        long t1, t2;

        t1 = System.currentTimeMillis();

        // Process the data in the buffer
        while (buffer.remaining() >= 8) { // Ensure there's at least one long left to read
            for (int i = 0; i < 4; i++) {
                target[i] = buffer.getLong(); // Read one long
            }

            if (converter.isMatch(target, input))
                matchCount++;
        }

        t2 = System.currentTimeMillis();
        System.out.println("Searching time (ms): " + (t2 - t1));

        return matchCount;
    }

    public static void main(String[] args) {
        long t1, t2;

        String input = "*".repeat(63);

        t1 = System.currentTimeMillis();
        BinaryPathWriter writer = new BinaryPathWriter("paths.bin", 8_000_000);
        BinaryPathReader reader = new BinaryPathReader(writer);
        BinaryPathMatcher matcher = new BinaryPathMatcher(reader);

        int result = matcher.countMatches(input);
        t2 = System.currentTimeMillis();

        System.out.println("Total matches: " + result);
        System.out.println("Total time (ms): " + (t2 - t1));
    }
}
