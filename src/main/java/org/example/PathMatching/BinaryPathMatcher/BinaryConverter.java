package org.example.PathMatching.BinaryPathMatcher;

public class BinaryConverter {

    // Encodes a path character into 4-bit integer
    // Time Complexity: O(1)
    public int encode(char c) {
        return switch (c) {
            case 'U' -> 1; // 0b0001
            case 'D' -> 2; // 0b0010
            case 'L' -> 4; // 0b0100
            case 'R' -> 8; // 0b1000
            default -> 0;
        };
    }

    private long toBinary(String s) {
        long bin = 0;

        for (int i = 0; i < s.length(); i++) {
            // Shift 4 bits to the left to make room for next char
            bin <<= 4;
            bin += encode(s.charAt(i));
        }

        return bin;
    }

    public long[] toBinArray(String s) {
        // Calculate how many full longs we need (16 chars per long)
        int numFullLongs = (s.length() + 15) / 16;  // Round up division
        long[] res = new long[numFullLongs];

        for (int i = 0; i < numFullLongs; i++) {
            int startIndex = i * 16;
            int endIndex = Math.min(startIndex + 16, s.length());  // Don't go past string end
            res[i] = toBinary(s.substring(startIndex, endIndex));
        }

        return res;
    }

    public boolean isMatch(long[] n1, long[] n2) {
        for (int i = 0; i < n1.length; i++) {
            if (!isMatch(n1[i], n2[i]))
                return false;
        }

        return true;
    }

    public boolean isMatch(long b1, long b2) {
        return b1 == (b1 | b2);
    }
}