package org.example.Benchmarking;

public class SampleBenchmark {
    public static void main(String[] args) {
        // Create a benchmarker that will run the function 1000 times
        Benchmarker benchmarker = new Benchmarker(1000);

        // Example: Benchmark a function that performs some calculation
        BenchmarkResult<Integer> result = benchmarker.measure(() -> {
            // Your function here
            int sum = 0;
            for (int i = 0; i < 1000000; i++) {
                sum += i;
            }
            return sum;
        });

        // Print the results
        System.out.println(result);
        // Access specific metrics
        System.out.println("Average time: " + result.getAverageTime() + "ms");
        System.out.println("Min time: " + result.getMinTime() + "ms");
        System.out.println("Max time: " + result.getMaxTime() + "ms");
    }
}
