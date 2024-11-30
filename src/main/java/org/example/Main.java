package org.example;

import org.example.PathMatching.PathGenerator;
import org.example.PathMatching.PathReader;
import org.example.PathMatching.Trie;

public class Main {

    public static void main(String[] args) {
        int length = 63;
        int pathCount = 500_000;

        long t1, t2;
        Trie trie = new Trie(length);

        PathGenerator pathGenerator = new PathGenerator(length);
        PathReader pathReader = new PathReader("paths.txt");

        String newString;

        System.out.println("Inserting paths to trie.");

        t1 = System.currentTimeMillis();

        for (int i = 0; i < pathCount; i++) {
            newString = pathReader.nextLine();
            if (newString == null) break;
            trie.insert(newString);
        }

        t2 = System.currentTimeMillis();

        System.out.println("Inserted " + pathCount + " paths");
        System.out.println("Inserting took: " + (t2 - t1) + " ms");
        System.out.println();

        // Change your inputs here
        String input = pathGenerator.fill('*');
//        String input = "*****DR******R******R********************R*D************L******";

        System.out.println("Searching for path:");
        System.out.println(input);

        System.out.println();
        t1 = System.currentTimeMillis();

        int matchCount = trie.countMatches(input);

        t2 = System.currentTimeMillis();

        System.out.println("Match count: " + matchCount);


        System.out.println("Searching took: " + (t2 - t1) + " ms");
    }
}