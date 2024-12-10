import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private StoreToBinaryFile storeToBinaryFile;

    public Menu() {
        scanner = new Scanner(System.in);
        storeToBinaryFile = new StoreToBinaryFile(); 
    }

    public void run() {
        displayWelcomeMessage();
        storeToBinaryFile.checkingFile();
        displayMenu();
    }

    private void displayWelcomeMessage() {
        clearScreen();
        System.out.println("Welcome to the 8x8 grid's mysterious forest's paths finder System!");
    }

    private void displayMenu() {
        PathGenerator generator = new PathGenerator(63);
        BinaryPathReader reader = new BinaryPathReader(storeToBinaryFile);
        BinaryPathMatcher matcher = new BinaryPathMatcher(reader);
        while (true) {
            System.out.println("\nPlease choose an operation:");
            System.out.println("1. Generate a random sequence");
            System.out.println("2. Use your sequence");
            System.out.println("0. Exit");
            System.out.println("Your choice:");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                switch (choice) {
                    case 1:
                        String randomString = generator.randomInputPath();
                        System.out.println("Your new random sequence is:" + randomString);
                        break;
                    case 2:
                        System.out.print("Enter the sequence: ");
                        String input = scanner.nextLine();

                        System.out.println();
                
                        for (int i = 0; i < 5; i++) {
                            System.out.println("Run #" + (i + 1));
                            System.out.println("Total matches: " + matcher.countMatches(input));
                            System.out.println();
                        }
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // consume invalid input
            }
        }
    }


    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                // for windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // for unix, linux, mac
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException ex) {
        }
    }

}