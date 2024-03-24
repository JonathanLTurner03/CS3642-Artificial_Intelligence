package Assignments.A3_Perceptron;

import java.text.DecimalFormat;

public class BrightPerceptron {

    /* TODO: Add the JavaDoc */
    public void beginTraining() {
        double weight0 = -0.5; // Bias
        double weight1 = 0.7;
        double weight2 = -0.2;
        double weight3 = 0.1;
        double weight4 = 0.9;

        int count = 1;
        double hasError;

        /* Loops until the perceptron no longer has any errors in an epoch */
        do {
            System.out.println("\nEpoch " + count + ":");

            /* Calls the epoch method to perform the iteration */
            double[] epoch = epoch(weight0, weight1, weight2, weight3, weight4, 0.05);

            /* Gets the last updated weights of the epoch */
            weight1 = epoch[0];
            weight2 = epoch[1];
            weight3 = epoch[2];
            weight4 = epoch[3];

            /* Gets if there were any errors in the epoch */
            hasError = epoch[4];
            count++;
        } while (hasError >= 1);
    }

    /* TODO: Add the JavaDoc */
    public double[] epoch(double weight0, double weight1, double weight2, double weight3, double weight4,
                          double learningRate) {

        /* Input data sets for the truth table */
        int[][] inputs = new int[][]
                {
                        {-1,-1,-1,-1},
                        {-1,-1,-1,1},
                        {-1,-1,1,-1},
                        {-1,1,-1,-1},
                        {1,-1,-1,-1},
                        {-1,-1,1,1},
                        {-1,1,-1,1},
                        {-1,1,-1,1},
                        {-1,1,1,1},
                        {1,-1,-1,1},
                        {1,-1,1,1},
                        {1,-1,1,1},
                        {1,1,-1,1},
                        {1,1,-1,1},
                        {1,1,1,-1},
                        {1,1,1,1}
                };

        double currWeight0 = weight0;
        double currWeight1 = weight1;
        double currWeight2 = weight2;
        double currWeight3 = weight3;
        double currWeight4 = weight4;

        /* Stores the results of the 5 iterations */
        Results[] results = new Results[inputs.length];

        /* Calls the iterate method to perform the iteration over the 4 truth table values */
        int iterCount = 0;
        for (int[] input : inputs) {
            int target = input[0] + input[1] + input[2] + input[3] >= 0 ? 1 : -1;
            Results iter = iterate(currWeight0, currWeight1, currWeight2, currWeight3, currWeight4, learningRate, input[0], input[1],
                    input[2], input[3], target);
            currWeight0 = iter.newWeight0;
            currWeight1 = iter.newWeight1;
            currWeight2 = iter.newWeight2;
            currWeight3 = iter.newWeight3;
            currWeight4 = iter.newWeight4;
            results[iterCount] = iter;
            iterCount++;
        }

        /* Prints out the table for each of the epochs. */
        System.out.printf(" %-7s| %-12s| %-13s| %-23s| %-7s| %-15s| %-7s| %-23s| %-20s%n",
                "Sample", "Input", "Target (y)", "Weights (w1,w2,w3,w4)", "S", "Output (o)", "Error",
                "Delta (d1,d2,d3,d4)", "Updated Weights (w1,w2,w3,w4)");
        System.out.println("--------------------------------------------------------------------------------------" +
                "------------------------------------------");
        StringBuilder table = new StringBuilder();
        for (int i = 0; i < results.length; i++) {
            table.append(formatResults(i + 1, results[i])).append("\n");
        }

        /* Stores if there was an error or not, holds only 1.0 or 0.0 */
        double hasError = 0.0;
        System.out.println(table);
        for (Results result : results) {
            if (Math.abs(result.error) > 0) {
                hasError = 1.0;
                break;
            }
        }

        /* Returns the new weight and if there was an error */
        return new double[]{results[results.length-1].weight1, results[results.length-1].weight2, results[results.length-1].weight3, results[results.length-1].weight4, hasError};
    }

    /* TODO: Add the JavaDoc */
    private String formatResults(int sample, Results result) {
        DecimalFormat formatVal = new DecimalFormat("##.##");
        return String.format(" %-7s| %-12s| %-13s| %-23s| %-7s| %-15s| %-7s| %-23s| %-20s",
                sample, // The Sample/Iteration Number
                result.input1() + "," + result.input2() + "," + result.input3 + "," + result.input4, // The Input Values
                result.target(), // The Target Value
                formatVal.format(result.weight1()) + "," + formatVal.format(result.weight2()) + ","
                    + formatVal.format(result.weight3) + "," + formatVal.format(result.weight4), // The Original Weights
                formatVal.format(result.s()), // The S Value
                result.output(), // The Output Value
                result.error(), // The Error Value
                formatVal.format(result.delta1()) + "," + formatVal.format(result.delta2()) + ","
                    + formatVal.format(result.delta3()) + "," + formatVal.format(result.delta4()), // The Delta Values
                formatVal.format(result.newWeight1) + "," + formatVal.format(result.newWeight2) + ","
                    + formatVal.format(result.newWeight3) + "," + formatVal.format(result.newWeight4)); // The Updated Weights
    }

    /* TODO: Add the JavaDoc */
    public Results iterate(double weight0, double weight1, double weight2, double weight3, double weight4,
                           double learningRate, int input1, int input2, int input3, int input4, int target) {

        /* Handles the weights of the values */
        double s = (weight0 * 1) + weight1 * input1 + weight2 * input2 + weight3 * input3 + weight4 * input4;

        /* Handles the S function and calculates the error */
        int output = s > 0 ? 1 : -1;
        int error = target - output;

        /* Calculates the delta values */
        double delta0 = learningRate * (double) error * 1;
        double delta1 = learningRate * (double) error * input1;
        double delta2 = learningRate * (double) error * input2;
        double delta3 = learningRate * (double) error * input3;
        double delta4 = learningRate * (double) error * input4;

        /* Calculates the new weights */
        double newWeight0 = weight0 + delta0;
        double newWeight1 = weight1 + delta1;
        double newWeight2 = weight2 + delta2;
        double newWeight3 = weight3 + delta3;
        double newWeight4 = weight4 + delta4;

        /* Returns the results of the iteration */
        return new Results(learningRate, input1, input2, input3, input4, target, weight0, weight1, weight2, weight3,
                weight4, s, output, error, delta1, delta2, delta3, delta4, newWeight0, newWeight1, newWeight2, newWeight3,
                newWeight4);
    }

    /* TODO: Add the JavaDoc */
    public record Results (double learningRate, int input1, int input2, int input3, int input4, int target,
                           double weight0, double weight1, double weight2, double weight3, double weight4,
                           double s, int output, int error, double delta1, double delta2, double delta3, double delta4,
                           double newWeight0, double newWeight1, double newWeight2, double newWeight3, double newWeight4)
    {}
}
