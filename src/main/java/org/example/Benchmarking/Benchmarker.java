package org.example.Benchmarking;

import java.util.function.Supplier;
import java.util.ArrayList;
import java.util.List;

public class Benchmarker {
    private final int iterations;

    public Benchmarker(int iterations) {
        this.iterations = iterations;
    }

    public <T> BenchmarkResult<T> measure(Supplier<T> function) {
        List<Long> executionTimes = new ArrayList<>();
        List<T> results = new ArrayList<>();

        for (int i = 0; i < iterations; i++) {
            long startTime = System.currentTimeMillis();
            T result = function.get();
            long endTime = System.currentTimeMillis();

            executionTimes.add(endTime - startTime);
            results.add(result);
        }

        double averageTime = executionTimes.stream()
                .mapToLong(Long::valueOf)
                .average()
                .orElse(0.0);

        long minTime = executionTimes.stream()
                .mapToLong(Long::valueOf)
                .min()
                .orElse(0);

        long maxTime = executionTimes.stream()
                .mapToLong(Long::valueOf)
                .max()
                .orElse(0);

        return new BenchmarkResult<>(results, executionTimes, averageTime, minTime, maxTime);
    }
}