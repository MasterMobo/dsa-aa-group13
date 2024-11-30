// package org.example.PrecalculationPath;

// import java.io.*;
// import java.util.concurrent.ConcurrentLinkedQueue;
// import java.util.concurrent.CountDownLatch;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;
// import java.util.concurrent.atomic.AtomicInteger;

// import org.example.PathMatching.Trie;

// public class OptimizedPathGenerator {
//     private static final int GRID_SIZE = 8;
//     private static final int TOTAL_MOVES = 63;
//     private static final int SPLIT_POINT = 31;
//     private static final int[] dr = {-1, 0, 1, 0}; // Up, Right, Down, Left
//     private static final int[] dc = {0, 1, 0, -1};
//     private static final char[] DIRECTIONS = {'U', 'R', 'D', 'L'};
//     private static final int THREAD_COUNT = 4;
//     private static final int BATCH_SIZE = 10000;

//     private final AtomicInteger pathCount;
//     private final ExecutorService executor;
//     private final CountDownLatch completionLatch;
//     private final ConcurrentLinkedQueue<String> leftPathBuffer;
//     private final ConcurrentLinkedQueue<String> rightPathBuffer;
//     private final BufferedWriter leftWriter;
//     private final BufferedWriter rightWriter;

//     public OptimizedPathGenerator(String leftFile, String rightFile) throws IOException {
//         this.pathCount = new AtomicInteger(0);
//         this.executor = Executors.newFixedThreadPool(THREAD_COUNT);
//         this.completionLatch = new CountDownLatch(THREAD_COUNT);
//         this.leftPathBuffer = new ConcurrentLinkedQueue<>();
//         this.rightPathBuffer = new ConcurrentLinkedQueue<>();
//         this.leftWriter = new BufferedWriter(new FileWriter(leftFile));
//         this.rightWriter = new BufferedWriter(new FileWriter(rightFile));
//     }

//     public void generateOptimizedPaths() {
//         System.out.println("Starting path generation with " + THREAD_COUNT + " threads...");

//         // Start buffer monitor thread
//         Thread monitorThread = new Thread(this::monitorBuffers);
//         monitorThread.start();

//         // For paths starting with D
//         submitPathGenerationTasks('D', 1, 0);

//         // For paths starting with R
//         submitPathGenerationTasks('R', 0, 1);

//         try {
//             // Wait for all generation tasks to complete
//             completionLatch.await();

//             // Final flush of buffers
//             flushBuffers();

//             // Close writers
//             leftWriter.close();
//             rightWriter.close();

//         } catch (InterruptedException e) {
//             Thread.currentThread().interrupt();
//         } catch (IOException e) {
//             System.err.println("Error writing to files: " + e.getMessage());
//         } finally {
//             executor.shutdown();
//         }

//         System.out.println("Total paths generated: " + pathCount.get());
//     }

//     private void monitorBuffers() {
//         while (!executor.isTerminated() || !leftPathBuffer.isEmpty() || !rightPathBuffer.isEmpty()) {
//             try {
//                 flushBuffers();
//                 Thread.sleep(100); // Check every 100ms
//             } catch (IOException | InterruptedException e) {
//                 Thread.currentThread().interrupt();
//                 break;
//             }
//         }
//     }

//     private synchronized void flushBuffers() throws IOException {
//         // Write left paths
//         String path;
//         while ((path = leftPathBuffer.poll()) != null) {
//             leftWriter.write(path);
//             leftWriter.newLine();
//         }
//         leftWriter.flush();

//         // Write right paths
//         while ((path = rightPathBuffer.poll()) != null) {
//             rightWriter.write(path);
//             rightWriter.newLine();
//         }
//         rightWriter.flush();
//     }

//     private void submitPathGenerationTasks(char firstMove, int startRow, int startCol) {
//         PathGenerationTask task = new PathGenerationTask(firstMove, startRow, startCol);
//         executor.submit(() -> {
//             task.generate();
//             completionLatch.countDown();
//         });
//     }

//     private class PathGenerationTask {
//         private final StringBuilder currentPath;
//         private final boolean[][] visited;
//         private final int startRow;
//         private final int startCol;

//         PathGenerationTask(char firstMove, int row, int col) {
//             this.currentPath = new StringBuilder();
//             this.visited = new boolean[GRID_SIZE][GRID_SIZE];
//             this.startRow = row;
//             this.startCol = col;

//             // Initialize start position
//             visited[0][0] = true;
//             visited[row][col] = true;
//             currentPath.append(firstMove);
//         }

//         void generate() {
//             generateLeftPaths(startRow, startCol, 1);
//         }

//         private void generateLeftPaths(int row, int col, int moveCount) {
//             if (moveCount == SPLIT_POINT) {
//                 leftPathBuffer.offer(currentPath.toString());
//                 generateRightPaths(row, col, SPLIT_POINT);
//                 return;
//             }

//             for (int dir = 0; dir < 4; dir++) {
//                 int newRow = row + dr[dir];
//                 int newCol = col + dc[dir];

//                 if (isValidMove(newRow, newCol)) {
//                     visited[newRow][newCol] = true;
//                     currentPath.append(DIRECTIONS[dir]);

//                     generateLeftPaths(newRow, newCol, moveCount + 1);

//                     visited[newRow][newCol] = false;
//                     currentPath.setLength(currentPath.length() - 1);
//                 }
//             }
//         }

//         private void generateRightPaths(int startRow, int startCol, int moveCount) {
//             if (moveCount == TOTAL_MOVES) {
//                 if (startRow == GRID_SIZE - 1 && startCol == 0) {
//                     rightPathBuffer.offer(currentPath.substring(SPLIT_POINT));
//                     pathCount.incrementAndGet();

//                     // If buffer gets too large, try to flush
//                     if (rightPathBuffer.size() >= BATCH_SIZE) {
//                         try {
//                             flushBuffers();
//                         } catch (IOException e) {
//                             System.err.println("Error flushing buffers: " + e.getMessage());
//                         }
//                     }
//                 }
//                 return;
//             }

//             for (int dir = 0; dir < 4; dir++) {
//                 int newRow = startRow + dr[dir];
//                 int newCol = startCol + dc[dir];

//                 if (isValidMove(newRow, newCol)) {
//                     visited[newRow][newCol] = true;
//                     currentPath.append(DIRECTIONS[dir]);

//                     generateRightPaths(newRow, newCol, moveCount + 1);

//                     visited[newRow][newCol] = false;
//                     currentPath.setLength(currentPath.length() - 1);
//                 }
//             }
//         }

//         private boolean isValidMove(int row, int col) {
//             return row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE && !visited[row][col];
//         }
//     }

//     public static Trie buildTrieFromFile(String filename, int pathLength) throws IOException {
//         Trie trie = new Trie(pathLength);
//         try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 trie.insert(line);
//             }
//         }
//         return trie;
//     }

//     public static void main(String[] args) {
//         String leftFile = "left_paths.txt";
//         String rightFile = "right_paths.txt";

//         try {
//             System.out.println("Starting optimized path generation with multithreading...");
//             long startTime = System.currentTimeMillis();

//             // Generate paths
//             OptimizedPathGenerator generator = new OptimizedPathGenerator(leftFile, rightFile);
//             generator.generateOptimizedPaths();

//             System.out.println("\nBuilding tries from files...");

//             // Build tries from files
//             Trie leftTrie = OptimizedPathGenerator.buildTrieFromFile(leftFile, 31);
//             Trie rightTrie = OptimizedPathGenerator.buildTrieFromFile(rightFile, 32);

//             // Test some sample inputs
//             testPath(leftTrie, rightTrie, "*****DR******R******R********************R*D************L******");
//             testPath(leftTrie, rightTrie, "DDDDDDDRRRRRRR************************************LLLLLLLUUUUU");

//             long totalTime = System.currentTimeMillis() - startTime;
//             System.out.println("\nTotal execution time: " + totalTime + "ms");

//             // Print memory usage
//             Runtime runtime = Runtime.getRuntime();
//             long memory = runtime.totalMemory() - runtime.freeMemory();
//             System.out.println("Used memory: " + (memory / 1024 / 1024) + "MB");

//             // Delete temporary files
//             deleteFile(leftFile);
//             deleteFile(rightFile);

//         } catch (Exception e) {
//             System.err.println("Error: " + e.getMessage());
//             e.printStackTrace();
//         }
//     }

//     private static void testPath(Trie leftTrie, Trie rightTrie, String input) {
//         System.out.println("\nTesting path: " + input);

//         // Split input into left and right parts
//         String leftHalf = input.substring(0, 31);
//         String rightHalf = input.substring(31);

//         // Count stars in each half
//         int leftStars = countStars(leftHalf);
//         int rightStars = countStars(rightHalf);

//         System.out.println("Stars in left half: " + leftStars);
//         System.out.println("Stars in right half: " + rightStars);

//         // Determine which half to check first based on number of wildcards
//         long startTime = System.currentTimeMillis();
//         int matches;

//         if (leftStars > rightStars) {
//             System.out.println("Checking right half first (fewer wildcards)");
//             matches = rightTrie.countMatches(rightHalf);
//         } else {
//             System.out.println("Checking left half first (fewer wildcards)");
//             matches = leftTrie.countMatches(leftHalf);
//         }

//         long searchTime = System.currentTimeMillis() - startTime;
//         System.out.println("Found " + matches + " matches in " + searchTime + "ms");
//     }

//     private static int countStars(String s) {
//         int count = 0;
//         for (char c : s.toCharArray()) {
//             if (c == '*') count++;
//         }
//         return count;
//     }

//     private static void deleteFile(String filename) {
//         try {
//             java.io.File file = new java.io.File(filename);
//             if (file.exists()) {
//                 file.delete();
//             }
//         } catch (Exception e) {
//             System.err.println("Warning: Could not delete file " + filename);
//         }
//     }
// }