package org.example.PrecalculationPath;

import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HighlyOptimizedPathGenerator {
    private static final int GRID_SIZE = 8;
    private static final int TOTAL_MOVES = 63;
    private static final int SPLIT_POINT = 31;
    private static final int[] dr = {-1, 0, 1, 0}; // Up, Right, Down, Left
    private static final int[] dc = {0, 1, 0, -1};
    private static final char[] DIRECTIONS = {'U', 'R', 'D', 'L'};
    private static final int THREAD_COUNT = 4;
    private static final int BATCH_SIZE = 10_000;
    private static final int INITIAL_DEPTH = 4; // Split work after 4 moves
    
    private final AtomicInteger pathCount;
    private final ExecutorService executor;
    private final CountDownLatch completionLatch;
    private final ConcurrentLinkedQueue<String> leftPathBuffer;
    private final ConcurrentLinkedQueue<String> rightPathBuffer;
    private final BufferedWriter leftWriter;
    private final BufferedWriter rightWriter;
    private final AtomicInteger[] threadProgress;
    
    public HighlyOptimizedPathGenerator(String leftFile, String rightFile) throws IOException {
        this.pathCount = new AtomicInteger(0);
        this.executor = Executors.newFixedThreadPool(THREAD_COUNT);
        this.completionLatch = new CountDownLatch(THREAD_COUNT);
        this.leftPathBuffer = new ConcurrentLinkedQueue<>();
        this.rightPathBuffer = new ConcurrentLinkedQueue<>();
        this.leftWriter = new BufferedWriter(new FileWriter(leftFile));
        this.rightWriter = new BufferedWriter(new FileWriter(rightFile));
        this.threadProgress = new AtomicInteger[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threadProgress[i] = new AtomicInteger(0);
        }
        
        // Start progress monitor
        startProgressMonitor();
    }
    
    private void startProgressMonitor() {
        Thread monitor = new Thread(() -> {
            while (!executor.isShutdown()) {
                try {
                    Thread.sleep(10000); // Report every 10 seconds
                    System.out.println("\nProgress Report:");
                    for (int i = 0; i < THREAD_COUNT; i++) {
                        System.out.printf("Thread %d: %d paths%n", 
                            i, threadProgress[i].get());
                    }
                    System.out.printf("Total paths: %d%n", pathCount.get());
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        monitor.setDaemon(true);
        monitor.start();
    }

    public void generateOptimizedPaths() {
        System.out.println("Starting path generation with " + THREAD_COUNT + " threads...");
        
        // Only need to start with D and R from (0,0)
        submitPathGenerationTasks('D', 1, 0, 0);
        submitPathGenerationTasks('R', 0, 1, 1);
        
        try {
            completionLatch.await();
            flushBuffers();
            leftWriter.close();
            rightWriter.close();
        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }
    }
    
    private synchronized void flushBuffers() throws IOException {
        while (!leftPathBuffer.isEmpty()) {
            leftWriter.write(leftPathBuffer.poll());
            leftWriter.newLine();
        }
        leftWriter.flush();
        
        while (!rightPathBuffer.isEmpty()) {
            rightWriter.write(rightPathBuffer.poll());
            rightWriter.newLine();
        }
        rightWriter.flush();
    }

    private void submitPathGenerationTasks(char firstMove, int startRow, int startCol, int threadIdx) {
        System.out.println("Submitting tasks starting with " + firstMove);
        
        // Create initial state
        boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];
        visited[0][0] = true;
        visited[startRow][startCol] = true;
        
        // Start with simpler distribution - just 2 moves deep
        for (int dir = 0; dir < 4; dir++) {
            int newRow = startRow + dr[dir];
            int newCol = startCol + dc[dir];
            
            if (isValidMove(newRow, newCol, visited)) {
                visited[newRow][newCol] = true;
                String initialPath = firstMove + "" + DIRECTIONS[dir];
                
                System.out.println("Creating task for path: " + initialPath);
                executor.submit(new PathGenerationTask(
                    initialPath, 
                    copyVisited(visited), 
                    newRow, 
                    newCol, 
                    threadIdx
                ));
                visited[newRow][newCol] = false;
            }
        }
    }

    private void generateInitialPaths(int row, int col, StringBuilder path, boolean[][] visited, 
            int depth, int threadIdx) {
        if (depth == INITIAL_DEPTH) {
            executor.submit(new PathGenerationTask(
                path.toString(), copyVisited(visited), row, col, threadIdx));
            return;
        }

        for (int dir = 0; dir < 4; dir++) {
            int newRow = row + dr[dir];
            int newCol = col + dc[dir];
            
            if (isValidMove(newRow, newCol, visited) && 
                canReachEnd(newRow, newCol, depth, visited)) {
                
                visited[newRow][newCol] = true;
                path.append(DIRECTIONS[dir]);
                
                generateInitialPaths(newRow, newCol, path, visited, depth + 1, threadIdx);
                
                path.setLength(path.length() - 1);
                visited[newRow][newCol] = false;
            }
        }
    }

    private class PathGenerationTask implements Runnable {
        private final StringBuilder currentPath;
        private final boolean[][] visited;
        private final int startRow;
        private final int startCol;
        private final int threadIdx;

        PathGenerationTask(String initialPath, boolean[][] visited, int row, int col, int threadIdx) {
            this.currentPath = new StringBuilder(initialPath);
            this.visited = visited;
            this.startRow = row;
            this.startCol = col;
            this.threadIdx = threadIdx;
        }

        @Override
        public void run() {
            try {
                System.out.println("Starting task with path: " + currentPath);
                generateLeftPaths(startRow, startCol, currentPath.length());
            } finally {
                completionLatch.countDown();
            }
        }

        private void generateLeftPaths(int row, int col, int moveCount) {
            if (moveCount % 10 == 0) {
                System.out.println("Generating at depth: " + moveCount + 
                                 " Current path: " + currentPath);
            }
            
            if (moveCount == SPLIT_POINT) {
                // Store left path
                leftPathBuffer.offer(currentPath.toString());
                if (leftPathBuffer.size() >= BATCH_SIZE) {
                    try {
                        flushBuffers();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                generateRightPaths(row, col, SPLIT_POINT);
                return;
            }

            if (!canReachEnd(row, col, moveCount, visited)) return;

            for (int dir = 0; dir < 4; dir++) {
                int newRow = row + dr[dir];
                int newCol = col + dc[dir];

                if (isValidMove(newRow, newCol, visited)) {
                    visited[newRow][newCol] = true;
                    currentPath.append(DIRECTIONS[dir]);

                    generateLeftPaths(newRow, newCol, moveCount + 1);

                    visited[newRow][newCol] = false;
                    currentPath.setLength(currentPath.length() - 1);
                }
            }
        }

        private void generateRightPaths(int row, int col, int moveCount) {
            if (moveCount == TOTAL_MOVES) {
                if (row == GRID_SIZE - 1 && col == 0) {
                    rightPathBuffer.offer(currentPath.substring(SPLIT_POINT));
                    pathCount.incrementAndGet();
                    threadProgress[threadIdx].incrementAndGet();
                }
                return;
            }

            if (!canReachEnd(row, col, moveCount, visited)) return;

            for (int dir = 0; dir < 4; dir++) {
                int newRow = row + dr[dir];
                int newCol = col + dc[dir];

                if (isValidMove(newRow, newCol, visited)) {
                    visited[newRow][newCol] = true;
                    currentPath.append(DIRECTIONS[dir]);

                    generateRightPaths(newRow, newCol, moveCount + 1);

                    visited[newRow][newCol] = false;
                    currentPath.setLength(currentPath.length() - 1);
                }
            }
        }
    }

    private static boolean[][] copyVisited(boolean[][] original) {
        boolean[][] copy = new boolean[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, GRID_SIZE);
        }
        return copy;
    }

    private static boolean isValidMove(int row, int col, boolean[][] visited) {
        return row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE && !visited[row][col];
    }
    
    private static boolean canReachEnd(int row, int col, int moveCount, boolean[][] visited) {
        int targetRow = GRID_SIZE - 1;
        int targetCol = 0;
        
        // Calculate Manhattan distance to target
        int distance = Math.abs(targetRow - row) + Math.abs(targetCol - col);
        int remainingMoves = TOTAL_MOVES - moveCount;
        
        // If we can't reach in remaining moves, prune this path
        if (distance > remainingMoves) return false;
        
        // If we can't use all remaining moves to reach target, prune
        if ((remainingMoves - distance) % 2 != 0) return false;
        
        // Check if we've trapped ourselves
        if (isTrapped(row, col, visited)) return false;
        
        return true;
    }
    
    private static boolean isTrapped(int row, int col, boolean[][] visited) {
        int availableMoves = 0;
        for (int dir = 0; dir < 4; dir++) {
            int newRow = row + dr[dir];
            int newCol = col + dc[dir];
            if (isValidMove(newRow, newCol, visited)) availableMoves++;
        }
        return availableMoves == 0;
    }
    public static void main(String[] args) {
        String leftFile = "left_paths.txt";
        String rightFile = "right_paths.txt";
        
        System.out.println("Starting optimized path generation...");
        long startTime = System.currentTimeMillis();
        Runtime runtime = Runtime.getRuntime();
        
        try {
            // Print initial memory state
            System.out.printf("Initial memory usage: %d MB%n", 
                (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024);
            System.out.printf("Maximum available memory: %d MB%n", 
                runtime.maxMemory() / 1024 / 1024);
            
            // Create and run generator
            HighlyOptimizedPathGenerator generator = new HighlyOptimizedPathGenerator(leftFile, rightFile);
            generator.generateOptimizedPaths();
            
            // Print final statistics
            long totalTime = System.currentTimeMillis() - startTime;
            long memory = runtime.totalMemory() - runtime.freeMemory();
            
            System.out.println("\nGeneration completed!");
            System.out.printf("Total execution time: %.2f minutes%n", totalTime / 60000.0);
            System.out.printf("Final memory usage: %d MB%n", memory / 1024 / 1024);
            
            // Print file sizes
            java.io.File left = new java.io.File(leftFile);
            java.io.File right = new java.io.File(rightFile);
            
            System.out.printf("Left path file size: %.2f MB%n", left.length() / (1024.0 * 1024.0));
            System.out.printf("Right path file size: %.2f MB%n", right.length() / (1024.0 * 1024.0));
            
        } catch (Exception e) {
            System.err.println("Error during path generation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
