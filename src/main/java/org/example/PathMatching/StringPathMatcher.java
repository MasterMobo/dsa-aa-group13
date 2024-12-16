package org.example.PathMatching;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StringPathMatcher implements PathMatcher{
    private String fileName;

    public StringPathMatcher(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public int countMatches(String input) {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String path;

            long t1 = System.currentTimeMillis();
            while ((path = reader.readLine()) != null) {
                if (matchesPattern(path, input)) {
                    count++;
                }
            }
            long t2 = System.currentTimeMillis();

            System.out.println("Searching time (ms): " + (t2 - t1));
            System.out.println("Total paths: " + count);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    private boolean matchesPattern(String path, String pattern) {
        if (path.length() != pattern.length()) return false;
        for (int i = 0; i < path.length(); i++) {
            if (pattern.charAt(i) != '*' && path.charAt(i) != pattern.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}
