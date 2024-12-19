package org.example.Benchmarking.BenchmarkSuite;

import org.example.Benchmarking.BenchmarkCases.BinaryPathMatcherBenchmark;
import org.example.Utils.PathGenerator;

// Small benchmark suite, only runs bit masking cases
public class SmallBenchmarkSuite extends BenchmarkSuite{
    public SmallBenchmarkSuite() {
        super(3);
    }

    @Override
    protected void initializeCases() {
        PathGenerator generator = new PathGenerator(63);
        String randomInput = generator.randomInputPath();
        String bestCaseInput = generator.fill('L');

        cases[0] = new BinaryPathMatcherBenchmark("Binary Path Matcher Worst Case", 5, "***************************************************************");
        cases[1] = new BinaryPathMatcherBenchmark("Binary Path Matcher Random Case", 5, randomInput);
        cases[2] = new BinaryPathMatcherBenchmark("Binary Path Matcher Best Case", 5, bestCaseInput);
    }

    public static void main(String[] args) {
        BenchmarkSuite suite = new SmallBenchmarkSuite();
        suite.run();
    }
}
