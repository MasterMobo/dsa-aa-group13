package org.example.PathMatching;

import java.util.Random;

public class PathGenerator {
    private static final String selection = "URLD";
    private int length = 63;

    public PathGenerator() {
    }

    public PathGenerator(int length) {
        this.length = length;
    }

    public String randomPath() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        int x = 0;
        int y = 0;


        for (int i = 0; i < length; i++) {

            char c;
            int newX = 0;
            int newY = 0;

            do {
                c = selection.charAt(random.nextInt(selection.length()));

                newX = x;
                newY = y;

                switch (c) {
                    case 'U':
                        newY++;
                        break;
                    case 'D':
                        newY--;
                        break;
                    case 'L':
                        newX--;
                        break;
                    case 'R':
                        newX++;
                        break;
                    default:
                        break;
                }

            } while (Math.min(newX, newY) >= 0 && Math.max(newX, newY) < 8);

            x = newX;
            y = newY;

            builder.append(c);
        }

        return builder.toString();
    }

    public String fill(char c) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(c);
        }

        return builder.toString();
    }
}
