package org.example.Benchmarking;

import org.example.Benchmarking.BenchmarkCases.BenchmarkCase;
import org.example.Benchmarking.BenchmarkCases.BinaryPathMatcherBenchmark;
import org.example.Benchmarking.BenchmarkCases.CSESSolutionBenchmark;
import org.example.Benchmarking.BenchmarkCases.StringPathMatcherBenchmark;

public class BenchmarkSuite {
    private final int numCases = 3;

    private final BenchmarkCase[] cases;

    public BenchmarkSuite() {
        cases = new BenchmarkCase[numCases];

        cases[0] = new BinaryPathMatcherBenchmark("Binary Path Matcher Worst Case", 10, "***************************************************************");
        cases[1] = new StringPathMatcherBenchmark("String Path Matcher Worst Case", 10,"***************************************************************");
        cases[2] = new CSESSolutionBenchmark("CSES Solution Worst Case", 10, "***************************************************************");
    }

    public void run() {
        for (BenchmarkCase benchmarkCase: cases) {
            benchmarkCase.run();
        }

        for (BenchmarkCase benchmarkCase: cases) {
            benchmarkCase.displayResult();
        }
    }

    public static void main(String[] args) {
        BenchmarkSuite suite = new BenchmarkSuite();
        suite.run();
    }
}
