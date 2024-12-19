package org.example.Benchmarking.BenchmarkCases;

import org.example.PathMatching.BinaryPathMatcher.BinaryPathMatcher;
import org.example.PathMatching.BinaryPathMatcher.BinaryPathReader;
import org.example.PathMatching.PathMatcher;
import org.example.PathPrecomputation.PathPrecomputor;
import org.example.PathPrecomputation.PathWriter.BinaryPathWriter;
import org.example.PathPrecomputation.PathWriter.PathWriter;

// Bit masking benchmark
public class BinaryPathMatcherBenchmark extends Benchmark<Integer> {

    private String input;

    public BinaryPathMatcherBenchmark(String name, int runs, String input) {
        super(name, runs);
        this.input = input;
    }

    @Override
    public void run() {
        // Setup
        String fileName = "validPaths.bin";

        PathWriter writer = new BinaryPathWriter(fileName);
        PathPrecomputor precomputor = new PathPrecomputor(writer);
        precomputor.precompute();

        BinaryPathReader reader = new BinaryPathReader(fileName);

        PathMatcher matcher = new BinaryPathMatcher(reader);


        // Measure
        result = benchmarkRunner.measure(() -> {
            return matcher.countMatches(input);
        });

        // Clean-up
        reader.clearBuffer();
    }
}
