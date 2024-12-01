package org.example.PrecalculationPath;

import org.example.PathMatching.TrieNode;
import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PathFinder {
    private static final int GRID_SIZE = 8;
    private static final int TOTAL_MOVES = 63;
    private static final int DIR_LEN = 4;
    private static final int[] dr = {-1, 0, 1, 0}; // Up, Right, Down, Left
    private static final int[] dc = {0, 1, 0, -1};
    private static final int BATCH_SIZE = 100_000;
    private static final String TEMP_DIR = "temp_tries";
    private static final String FINAL_FILE = "all_paths.ser";

    static class Generator {
        private final boolean[][] onPath;
        private TrieNode currentTrie;
        private final AtomicInteger pathCount = new AtomicInteger(0);
        private final AtomicInteger batchCount = new AtomicInteger(0);
        private int currentBatchSize = 0;

        Generator() {
            this.onPath = new boolean[GRID_SIZE + 2][GRID_SIZE + 2];
            this.currentTrie = new TrieNode();
            new File(TEMP_DIR).mkdirs();
            initializeGrid();
        }

        private void initializeGrid() {
            for (int i = 0; i < GRID_SIZE + 2; i++) {
                onPath[0][i] = onPath[GRID_SIZE + 1][i] = true;
                onPath[i][0] = onPath[i][GRID_SIZE + 1] = true;
            }
            for (int i = 1; i <= GRID_SIZE; i++) {
                for (int j = 1; j <= GRID_SIZE; j++) {
                    onPath[i][j] = false;
                }
            }
        }

        private void tryPath(int pathIdx, int curR, int curC, StringBuilder currentPath) {
            if ((onPath[curR][curC - 1] && onPath[curR][curC + 1]) &&
                (!onPath[curR - 1][curC] && !onPath[curR + 1][curC])) return;
            if ((onPath[curR - 1][curC] && onPath[curR + 1][curC]) &&
                (!onPath[curR][curC - 1] && !onPath[curR][curC + 1])) return;

            if (curR == GRID_SIZE && curC == 1) {
                if (pathIdx == TOTAL_MOVES) {
                    addPathToTrie(currentPath.toString());
                }
                return;
            }

            if (pathIdx == TOTAL_MOVES) return;

            onPath[curR][curC] = true;
            int originalLength = currentPath.length();

            for (int i = 0; i < DIR_LEN; i++) {
                int nxtR = curR + dr[i];
                int nxtC = curC + dc[i];

                if (!onPath[nxtR][nxtC]) {
                    currentPath.append((char)('0' + i));
                    tryPath(pathIdx + 1, nxtR, nxtC, currentPath);
                    currentPath.setLength(originalLength);
                }
            }

            onPath[curR][curC] = false;
        }

        private void addPathToTrie(String path) {
            TrieNode node = currentTrie;
            for (char c : path.toCharArray()) {
                int index = c - '0';
                TrieNode child = node.getChild(index);
                if (child == null) {
                    child = new TrieNode();
                    node.addChild(index, child);
                }
                node = child;
            }
            node.pathCount++;
            currentBatchSize++;
            int total = pathCount.incrementAndGet();
            
            if (currentBatchSize >= BATCH_SIZE) {
                saveBatch();
            }
            
            if (total % 100000 == 0) {
                System.out.println("Found " + total + " valid paths");
            }
        }

        private void saveBatch() {
            String batchFile = TEMP_DIR + "/batch_" + batchCount.getAndIncrement() + ".ser";
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(batchFile))) {
                out.writeObject(currentTrie);
                System.out.println("Saved batch with " + currentBatchSize + " paths to " + batchFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            currentTrie = new TrieNode();
            currentBatchSize = 0;
            System.gc();
        }

        private void mergeBatchesAndSave() {
            System.out.println("Merging " + batchCount.get() + " batches...");
            TrieNode finalTrie = new TrieNode();
            
            for (int i = 0; i < batchCount.get(); i++) {
                String batchFile = TEMP_DIR + "/batch_" + i + ".ser";
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(batchFile))) {
                    TrieNode batchTrie = (TrieNode) in.readObject();
                    mergeTries(finalTrie, batchTrie, "");
                    new File(batchFile).delete();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                
                if (i % 10 == 0) {
                    System.gc();
                }
            }
            
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FINAL_FILE))) {
                out.writeObject(finalTrie);
                System.out.println("Saved all " + pathCount.get() + " paths to " + FINAL_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            new File(TEMP_DIR).delete();
        }

        private void mergeTries(TrieNode target, TrieNode source, String currentPath) {
            if (source.pathCount > 0) {
                TrieNode node = target;
                for (char c : currentPath.toCharArray()) {
                    int index = c - '0';
                    TrieNode child = node.getChild(index);
                    if (child == null) {
                        child = new TrieNode();
                        node.addChild(index, child);
                    }
                    node = child;
                }
                node.pathCount += source.pathCount;
            }

            for (int i = 0; i < 4; i++) {
                TrieNode child = source.getChild(i);
                if (child != null) {
                    mergeTries(target, child, currentPath + i);
                }
            }
        }

        public void generate() {
            System.out.println("Generating paths with batched storage...");
            tryPath(0, 1, 1, new StringBuilder());
            
            if (currentBatchSize > 0) {
                saveBatch();
            }
            
            mergeBatchesAndSave();
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting path generation...");
        System.out.println("Max memory: " + (Runtime.getRuntime().maxMemory() / 1024 / 1024) + "MB");
        
        long startTime = System.currentTimeMillis();
        Generator generator = new Generator();
        generator.generate();
        long duration = System.currentTimeMillis() - startTime;
        System.out.printf("Generation completed in %.2f seconds%n", duration / 1000.0);
    }
}