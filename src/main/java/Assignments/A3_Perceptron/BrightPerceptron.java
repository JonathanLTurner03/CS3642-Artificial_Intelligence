package Assignments.A3_Perceptron;

import java.text.DecimalFormat;

/**
 * This class is a perceptron training for recognizing a bright or dark "image".
 *
 * @author Jonathan Turner
 * @version Spring 2024
 */
public class BrightPerceptron {

    /**
     * The main method that begins the training of the perceptron.
     * <p>
     *     This method initializes the weights and then begins the training of the perceptron.
     *     It will loop until the perceptron no longer has any errors in an epoch.
     *     After each epoch it will print out the results of the epoch in the table format.
     * </p>
     *
     * @see #epoch(double, double, double, double, double, double)
     */
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

    /**
     * The epoch method that performs the iteration over the truth table.
     * <p>
     *     this method will iterate over the truth table and perform the calculations for each of the inputs.
     *     It will then print out the results of the iteration in a table format.
     * </p>
     * @param weight0       The bias weight
     * @param weight1       The weight for the first input
     * @param weight2       The weight for the second input
     * @param weight3       The weight for the third input
     * @param weight4       The weight for the fourth input
     * @param learningRate  The learning rate for the perceptron
     * @return The updated weights and if there was an error in the epoch
     *
     * @see #iterate(double, double, double, double, double, double, int, int, int, int, int)
     */
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

        /* Initializes the weights */
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
        System.out.printf(" %-7s| %-12s| %-13s| %-27s| %-7s| %-15s| %-7s| %-27s| %-27s%n",
                "Sample", "Input", "Target (y)", "Weights (w0,w1,w2,w3,w4)", "S", "Output (o)", "Error",
                "Delta (d1,d2,d3,d4)", "Updated Weights (w0,w1,w2,w3,w4)");
        System.out.println("--------------------------------------------------------------------------------------" +
                "-------------------------------------------------------------------------------");
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

    /**
     * Formats the results of the iteration into a table format.
     *
     * @param sample The iteration number
     * @param result The iteration values/results
     * @return The formatted results of the iteration
     *
     * @see Results
     */
    private String formatResults(int sample, Results result) {
        DecimalFormat formatVal = new DecimalFormat("##.##");
        return String.format(" %-7s| %-12s| %-13s| %-27s| %-7s| %-15s| %-7s| %-27s| %-27s",
                sample, // The Sample/Iteration Number
                result.input1() + "," + result.input2() + "," + result.input3 + "," + result.input4, // The Input Values
                result.target(), // The Target Value
                formatVal.format(result.weight0) + "," + formatVal.format(result.weight1()) + "," +
                        formatVal.format(result.weight2()) + "," + formatVal.format(result.weight3) + "," +
                        formatVal.format(result.weight4), // The Original Weights
                formatVal.format(result.s()), // The S Value
                result.output(), // The Output Value
                result.error(), // The Error Value
                formatVal.format(result.delta0()) + "," + formatVal.format(result.delta1()) + "," + formatVal.format(result.delta2()) + ","
                    + formatVal.format(result.delta3()) + "," + formatVal.format(result.delta4()), // The Delta Values
                formatVal.format(result.newWeight0) + "," + formatVal.format(result.newWeight1) + "," +
                        formatVal.format(result.newWeight2) + "," + formatVal.format(result.newWeight3) + "," +
                        formatVal.format(result.newWeight4)); // The Updated Weights
    }

    /**
     * The iterate method that performs the calculations for the perceptron.
     *
     * <p>
     *     this method will calculate the weights for the perceptron based on the inputs and the target value.
     *     It will calculate the error and the delta values and weights for each input and the bias.
     *     It will then return the results of the iteration.
     * </p>
     *
     * @param weight0       The bias weight
     * @param weight1       The weight for the first input
     * @param weight2       The weight for the second input
     * @param weight3       The weight for the third input
     * @param weight4       The weight for the fourth input
     * @param learningRate  The learning rate for the perceptron
     * @param input1        The first input value
     * @param input2        The second input value
     * @param input3        The third input value
     * @param input4        The fourth input value
     * @param target        The target value
     *
     * @return The results of the iteration
     * @see Results
     */
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
                weight4, s, output, error, delta0, delta1, delta2, delta3, delta4, newWeight0, newWeight1, newWeight2, newWeight3,
                newWeight4);
    }

    /**
     * The Results class that holds the results of an iteration.
     */
    public record Results (double learningRate, int input1, int input2, int input3, int input4, int target,
                           double weight0, double weight1, double weight2, double weight3, double weight4,
                           double s, int output, int error, double delta0, double delta1, double delta2, double delta3,
                           double delta4, double newWeight0, double newWeight1, double newWeight2, double newWeight3,
                           double newWeight4)
    {}
}
