package org.example.Benchmarking.BenchmarkSuite;

import org.example.Benchmarking.BenchmarkCases.BinaryPathMatcherBenchmark;
import org.example.Benchmarking.BenchmarkCases.CSESSolutionBenchmark;
import org.example.Benchmarking.BenchmarkCases.StringPathMatcherBenchmark;
import org.example.Utils.PathGenerator;

// WARNING: Running this class will run all benchmark cases, which might take a long time.
// If you only need a minimal benchmark, run SmallBenchmarkSuite.

public class FullBenchmarkSuite extends BenchmarkSuite{
    public FullBenchmarkSuite() {
        super(9);
    }

    @Override
    protected void initializeCases() {
        PathGenerator generator = new PathGenerator(63);
        String randomInput = generator.randomInputPath();
        String bestCaseInput = generator.fill('L');

        cases[0] = new BinaryPathMatcherBenchmark("Binary Path Matcher Worst Case", 10, "***************************************************************");
        cases[1] = new BinaryPathMatcherBenchmark("Binary Path Matcher Random Case", 10, randomInput);
        cases[2] = new BinaryPathMatcherBenchmark("Binary Path Matcher Random Case", 10, bestCaseInput);

        cases[3] = new StringPathMatcherBenchmark("String Path Matcher Worst Case", 10, "***************************************************************");
        cases[4] = new StringPathMatcherBenchmark("String Path Matcher Random Case", 10, randomInput);
        cases[5] = new StringPathMatcherBenchmark("String Path Matcher Best Case", 10, bestCaseInput);

        cases[6] = new CSESSolutionBenchmark("CSES Solution Worst Case", 10, "***************************************************************");
        cases[7] = new CSESSolutionBenchmark("CSES Solution Random Case", 10, randomInput);
        cases[8] = new CSESSolutionBenchmark("CSES Solution Best Case", 10, bestCaseInput);
    }

    public static void main(String[] args) {
        BenchmarkSuite suite = new FullBenchmarkSuite();
        suite.run();
    }
}
