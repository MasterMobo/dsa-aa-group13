package org.example.Benchmarking;

import org.example.PathMatching.BinaryPathMatcher.BinaryPathMatcher;
import org.example.PathMatching.BinaryPathMatcher.BinaryPathReader;
import org.example.PathMatching.PathMatcher;
import org.example.PathMatching.StringPathMatcher;
import org.example.PathPrecomputation.PathPrecomputor;
import org.example.PathPrecomputation.PathWriter.BinaryPathWriter;
import org.example.PathPrecomputation.PathWriter.PathWriter;
import org.example.PathPrecomputation.PathWriter.StringPathWriter;

public class StringPathMatcherBenchmark extends BenchmarkCase<Integer>{
    private final String input;
    public StringPathMatcherBenchmark(String name, String input) {
        super(name);
        this.input = input;
    }

    public void run() {
        Benchmarker benchmarker = new Benchmarker(20);

        // Setup
        String fileName = "validPathStrings.txt";

        PathWriter writer = new StringPathWriter(fileName);
        PathPrecomputor precomputor = new PathPrecomputor(writer);
        precomputor.precompute();

        PathMatcher matcher = new StringPathMatcher(fileName);

        result = benchmarker.measure(() -> {
            return matcher.countMatches(input);
        });
    }
}
