package org.example.Benchmarking;

import java.util.List;

public class BenchmarkResult<T> {
    private final List<T> results;
    private final List<Long> executionTimes;
    private final double averageTime;
    private final long minTime;
    private final long maxTime;

    public BenchmarkResult(List<T> results, List<Long> executionTimes,
                           double averageTime, long minTime, long maxTime) {
        this.results = results;
        this.executionTimes = executionTimes;
        this.averageTime = averageTime;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    public List<T> getResults() {
        return results;
    }

    public List<Long> getExecutionTimes() {
        return executionTimes;
    }

    public double getAverageTime() {
        return averageTime;
    }

    public long getMinTime() {
        return minTime;
    }

    public long getMaxTime() {
        return maxTime;
    }

    @Override
    public String toString() {
        return String.format(
                "BenchmarkResult{iterations=%d, avgTime=%.2fms, minTime=%dms, maxTime=%dms}",
                executionTimes.size(), averageTime, minTime, maxTime);
    }
}