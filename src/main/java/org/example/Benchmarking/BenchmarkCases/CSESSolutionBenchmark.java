package org.example.Benchmarking.BenchmarkCases;

import org.example.PathMatching.CSESSolution;
import org.example.PathMatching.PathMatcher;

// CSES solution benchmark
public class CSESSolutionBenchmark extends Benchmark<Integer> {

    private String input;

    public CSESSolutionBenchmark(String name, int runs, String input) {
        super(name, runs);
        this.input = input;
    }

    @Override
    public void run() {
        PathMatcher matcher = new CSESSolution();

        result = benchmarkRunner.measure(() -> {
            return matcher.countMatches(input);
        });
    }
}
