package org.example;

import org.example.Benchmarking.BenchmarkSuite.BenchmarkSuite;
import org.example.Benchmarking.BenchmarkSuite.SmallBenchmarkSuite;
import org.example.PathMatching.BinaryPathMatcher.BinaryPathMatcher;
import org.example.PathMatching.BinaryPathMatcher.BinaryPathReader;
import org.example.PathMatching.PathMatcher;
import org.example.Utils.PathGenerator;
import org.example.PathPrecomputation.PathWriter.BinaryPathWriter;
import org.example.PathPrecomputation.PathPrecomputor;
import org.example.PathPrecomputation.PathWriter.PathWriter;

import java.util.InputMismatchException;
import java.util.Scanner;

// Class for the command line interface
public class Menu {
    private Scanner scanner;    // Used for reading inputs

    private final PathMatcher matcher;  // Used to calculate paths
    private final BenchmarkSuite benchmarkSuite;    // Used to run benchmark

    public Menu() {
        scanner = new Scanner(System.in);

        // Set up program
        String fileName = "validPaths.bin";

        PathWriter writer = new BinaryPathWriter(fileName);
        PathPrecomputor precomputor = new PathPrecomputor(writer);
        // Run pre-computation
        precomputor.precompute();

        BinaryPathReader reader = new BinaryPathReader(fileName);
        matcher = new BinaryPathMatcher(reader);

        benchmarkSuite = new SmallBenchmarkSuite();
    }

    public void run() {
        displayWelcomeMessage();
        displayMenu();
    }

    private void displayWelcomeMessage() {
        System.out.println("Welcome to the 8x8 grid's mysterious forest's path finder System!");
    }

    private void displayMenu() {
        // Generator for random path generation
        PathGenerator generator = new PathGenerator(63);

        // Main event loop
        while (true) {
            System.out.println("\nPlease choose an operation:");
            System.out.println("1. Use your sequence");
            System.out.println("2. Use a random sequence");
            System.out.println("3. Run benchmark");
            System.out.println("0. Exit");
            System.out.println("Your choice:");

            int choice;

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline
            } catch (InputMismatchException e) {
                // Handle invalid number input
                scanner.nextLine(); // Consume the invalid input
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter the sequence: ");
                    String input = scanner.nextLine();
                    if (!validateInput(input)) continue;
                    System.out.println();
                    System.out.print("Enter the number of runs: ");
                    int runs = getValidRunsInput();
                    executeMatcher(input, runs);
                }
                case 2 -> {
                    String randomString = generator.randomInputPath();
                    System.out.println("Your new random sequence is:" + randomString);
                    System.out.println();
                    System.out.print("Enter the number of runs: ");
                    int runs = getValidRunsInput();
                    executeMatcher(randomString, runs);
                }
                case 3 -> benchmarkSuite.run();
                case 0 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private int getValidRunsInput() {
        while (true) {
            try {
                int runs = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline
                if (runs <= 0) {
                    System.out.print("Please enter a positive number: ");
                    continue;
                }
                return runs;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Consume the invalid input
                System.out.print("Invalid input. Please enter a positive number: ");
            }
        }
    }

    private void executeMatcher(String path, int numberOfRuns) {
        for (int i = 0; i < numberOfRuns; i++) {
            System.out.println("Run #" + (i + 1));
            matcher.countMatches(path);
            System.out.println();
        }
    }

    private boolean validateInput(String input) {
        if (input.length() != 63) {
            System.out.println("Input path must have exactly 63 characters!");
            return false;
        }

        String validCharacters = "UDLR*";
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (validCharacters.indexOf(c) == -1) {
                System.out.println("Invalid path! Path can not contain character: " + c);
                return false;
            }
        }

        return true;
    }

}