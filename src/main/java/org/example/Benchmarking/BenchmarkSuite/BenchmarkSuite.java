package org.example.Benchmarking.BenchmarkSuite;

import org.example.Benchmarking.BenchmarkCases.Benchmark;

// Class representing a collection of benchmarks
public abstract class BenchmarkSuite {
    protected final int numCases;   // Number of benchmark cases to be run

    protected final Benchmark[] cases;  // Array of benchmark cases to be run

    public BenchmarkSuite(int numCases) {
        this.numCases = numCases;
        cases = new Benchmark[numCases];

        initializeCases();
    }

    protected abstract void initializeCases();

    public void run() {
        for (Benchmark benchmark : cases) {
            benchmark.run();
        }

        for (Benchmark benchmark : cases) {
            benchmark.displayResult();
        }
    }
}
