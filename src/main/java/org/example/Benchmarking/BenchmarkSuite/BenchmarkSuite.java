package org.example.Benchmarking.BenchmarkSuite;

import org.example.Benchmarking.BenchmarkCases.Benchmark;

public abstract class BenchmarkSuite {
    protected final int numCases;

    protected final Benchmark[] cases;

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
