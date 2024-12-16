package org.example.Benchmarking.BenchmarkCases;

import org.example.Benchmarking.BenchmarkRunner;
import org.example.PathMatching.PathMatcher;
import org.example.PathMatching.StringPathMatcher;
import org.example.PathPrecomputation.PathPrecomputor;
import org.example.PathPrecomputation.PathWriter.PathWriter;
import org.example.PathPrecomputation.PathWriter.StringPathWriter;

public class StringPathMatcherBenchmark extends Benchmark<Integer> {
    private final String input;

    public StringPathMatcherBenchmark(String name, int runs, String input) {
        super(name, runs);
        this.input = input;
    }

    public void run() {
        BenchmarkRunner benchmarkRunner = new BenchmarkRunner(runs);

        // Setup
        String fileName = "validPathStrings.txt";

        PathWriter writer = new StringPathWriter(fileName);
        PathPrecomputor precomputor = new PathPrecomputor(writer);
        precomputor.precompute();

        PathMatcher matcher = new StringPathMatcher(fileName);

        result = benchmarkRunner.measure(() -> {
            return matcher.countMatches(input);
        });
    }
}
