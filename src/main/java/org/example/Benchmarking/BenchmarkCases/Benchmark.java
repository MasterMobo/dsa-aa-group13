package org.example.Benchmarking.BenchmarkCases;

import org.example.Benchmarking.BenchmarkResult;
import org.example.Benchmarking.Benchmarker;

public abstract class BenchmarkCase<T> {

    protected String name;
    protected int runs;

    protected Benchmarker benchmarker;
    protected BenchmarkResult<T> result;

    public BenchmarkCase(String name, int runs) {
        this.name = name;
        this.runs = runs;
        benchmarker = new Benchmarker(runs);
    }

    public abstract void run();

    public void displayResult() {
        // Access specific metrics
        System.out.println();
        System.out.println("================================================");
        System.out.println(name);
        System.out.println("Average time: " + result.getAverageTime() + " ms");
        System.out.println("Best time: " + result.getMinTime() + " ms");
        System.out.println("Worst time: " + result.getMaxTime() + " ms");
        System.out.println("================================================");
        System.out.println();
    }
}
