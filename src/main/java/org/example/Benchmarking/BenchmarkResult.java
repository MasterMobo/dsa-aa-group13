package org.example.Benchmarking;

public class BenchmarkResult<T> {
    private final T[] results;
    private final long[] executionTimes;
    private final double averageTime;
    private final long minTime;
    private final long maxTime;

    public BenchmarkResult(T[] results, long[] executionTimes,
                           double averageTime, long minTime, long maxTime) {
        this.results = results;
        this.executionTimes = executionTimes;
        this.averageTime = averageTime;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    public T[] getResults() {
        return results;
    }

    public long[] getExecutionTimes() {
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
                executionTimes.length, averageTime, minTime, maxTime);
    }
}