package org.example.Benchmarking;

import java.util.function.Supplier;

public class BenchmarkRunner {
    private final int iterations;

    public BenchmarkRunner(int iterations) {
        this.iterations = iterations;
    }

    public <T> BenchmarkResult<T> measure(Supplier<T> function) {
        // Due to Java's limitations with generic arrays, we're using Object[] internally and casting it.
        // This is safe in this context since we're only storing the results and not manipulating them.
        T[] results = (T[]) new Object[iterations];
        long[] executionTimes = new long[iterations];

        for (int i = 0; i < iterations; i++) {
            long startTime = System.currentTimeMillis();
            results[i] = function.get();
            long endTime = System.currentTimeMillis();
            executionTimes[i] = endTime - startTime;
        }

        // Calculate statistics
        double averageTime = 0;
        long minTime = executionTimes[0];
        long maxTime = executionTimes[0];

        for (long time : executionTimes) {
            averageTime += time;
            if (time < minTime) minTime = time;
            if (time > maxTime) maxTime = time;
        }
        averageTime /= iterations;

        return new BenchmarkResult<>(results, executionTimes, averageTime, minTime, maxTime);
    }
}