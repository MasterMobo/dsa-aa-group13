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
        int matchCount = 0;

        long t1, t2;

        t1 = System.currentTimeMillis();

        // Process the data in the buffer
        while (buffer.remaining() >= 8) { // Ensure there's at least one long left to read
            boolean matches = true;

            for (int i = 0; i < 4; i++) {
                if (!converter.isMatch(buffer.getLong(), input[i])) {
                    // Skip to next path early if this doesn't match
                    matches = false;
                    buffer.position(buffer.position() + (4 - i - 1) * 8);
                    break;
                }
            }

            if (matches) matchCount++;
        }

        t2 = System.currentTimeMillis();
        System.out.println("Searching time (ms): " + (t2 - t1));

        return matchCount;
    }
}
