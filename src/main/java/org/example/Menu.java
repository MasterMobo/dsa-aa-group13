package org.example;

import org.example.PathMatching.BinaryPathMatcher.BinaryPathMatcher;
import org.example.PathMatching.BinaryPathMatcher.BinaryPathReader;
import org.example.Utils.PathGenerator;
import org.example.PathPrecomputation.BinaryPathWriter;
import org.example.PathPrecomputation.PathPrecomputor;
import org.example.PathPrecomputation.PathWriter;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    private Scanner scanner;

    BinaryPathMatcher matcher;

    public Menu() {
        scanner = new Scanner(System.in);

        String fileName = "validPaths.bin";

        PathWriter writer = new BinaryPathWriter(fileName);
        PathPrecomputor precomputor = new PathPrecomputor(writer);
        precomputor.precompute();

        BinaryPathReader reader = new BinaryPathReader(fileName);
        matcher = new BinaryPathMatcher(reader);
    }

    public void run() {
        displayWelcomeMessage();
        displayMenu();
    }

    private void displayWelcomeMessage() {
        System.out.println("Welcome to the 8x8 grid's mysterious forest's path finder System!");
    }

    private void displayMenu() {
        PathGenerator generator = new PathGenerator(63);

        while (true) {
            System.out.println("\nPlease choose an operation:");
            System.out.println("1. Use a random sequence");
            System.out.println("2. Use your sequence");
            System.out.println("0. Exit");
            System.out.println("Your choice:");

            int choice;

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    String randomString = generator.randomInputPath();
                    System.out.println("Your new random sequence is:" + randomString);
                    System.out.println();
                    executeMatcher(randomString);
                }
                case 2 -> {
                    System.out.print("Enter the sequence: ");
                    String input = scanner.nextLine();
                    System.out.println();
                    executeMatcher(input);
                }
                case 0 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }

        }
    }

    private void executeMatcher(String path) {
        for (int i = 0; i < 5; i++) {
            System.out.println("Run #" + (i + 1));
            System.out.println("Total matches: " + matcher.countMatches(path));
            System.out.println();
        }
    }

}