package org.example.Benchmarking;

import org.example.PathMatching.BinaryPathMatcher.BinaryPathMatcher;
import org.example.PathMatching.BinaryPathMatcher.BinaryPathReader;
import org.example.PathMatching.PathMatcher;
import org.example.PathPrecomputation.PathPrecomputor;
import org.example.PathPrecomputation.PathWriter.BinaryPathWriter;
import org.example.PathPrecomputation.PathWriter.PathWriter;

public class BinaryPathMatcherBenchmark extends BenchmarkCase<Integer> {

    private String input;

    public BinaryPathMatcherBenchmark(String name, String input) {
        super(name);
        this.input = input;
    }

    @Override
    public void run() {
        Benchmarker benchmarker = new Benchmarker(20);

        // Setup
        String fileName = "validPaths.bin";

        PathWriter writer = new BinaryPathWriter(fileName);
        PathPrecomputor precomputor = new PathPrecomputor(writer);
        precomputor.precompute();

        BinaryPathReader reader = new BinaryPathReader(fileName);

        PathMatcher matcher = new BinaryPathMatcher(reader);


        // Measure
        result = benchmarker.measure(() -> {
            return matcher.countMatches(input);
        });
    }
}
