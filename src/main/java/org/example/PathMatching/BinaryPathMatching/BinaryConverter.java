package org.example.PathMatching.BinaryPathMatching;

public class BinaryConverter {
    public int map(char c) {
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
            bin += map(s.charAt(i));

            // Shift 4 bits to the left to make room for next char
            bin <<= 4;
        }

        return bin;
    }

    public long[] toBinArray(String s) {
        long[] res = new long[4];

        res[0] = toBinary(s.substring(0, 16));
        res[1] = toBinary(s.substring(16, 32));
        res[2] = toBinary(s.substring(32, 48));
        res[3] = toBinary(s.substring(48, 63));

        return res;
    }

    public boolean isMatch(long[] n1, long[] n2) {
        for (int i = 0; i < n1.length; i++) {
            if (!isMatch(n1[i], n2[i]))
                return false;
        }

        return true;
    }

    private boolean isMatch(long b1, long b2) {
        return b1 == (b1 | b2);
    }
}
