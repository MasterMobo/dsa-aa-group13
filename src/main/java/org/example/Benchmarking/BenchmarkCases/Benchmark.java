package org.example.Benchmarking.BenchmarkCases;

import org.example.Benchmarking.BenchmarkResult;
import org.example.Benchmarking.BenchmarkRunner;

public abstract class Benchmark<T> {

    protected String name;
    protected int runs;

    protected BenchmarkRunner benchmarkRunner;
    protected BenchmarkResult<T> result;

    public Benchmark(String name, int runs) {
        this.name = name;
        this.runs = runs;
        benchmarkRunner = new BenchmarkRunner(runs);
    }

    public abstract void run();

    public void displayResult() {
        // Access specific metrics
        System.out.println();
        System.out.println("=================== Benchmark Result ===================");
        System.out.println(name);
        System.out.println("Average time: " + result.getAverageTime() + " ms");
        System.out.println("Best time: " + result.getMinTime() + " ms");
        System.out.println("Worst time: " + result.getMaxTime() + " ms");
        System.out.println("========================================================");
        System.out.println();
    }
}
