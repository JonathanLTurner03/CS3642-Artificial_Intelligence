package Assignments.A3_Perceptron;

import java.text.DecimalFormat;

/**
 * This class is a perceptron training for recognizing a representing logical OR gate.
 *
 * @author Jonathan Turner
 * @version Spring 2024
 */
public class LogicOrPerceptron {

    /**
     * The main method that begins the training of the perceptron.
     * <p>
     *     This method initializes the weights and then begins the training of the perceptron.
     *     It will loop until the perceptron no longer has any errors in an epoch.
     *     After each epoch it will print out the results of the epoch in the table format.
     * </p>
     *
     * @see #epoch(double, double, double, double) 
     */
    public void beginTraining() {
        double weight0 = -0.2;
        double weight1 = 0.3;
        double weight2 = -0.1;
        int count = 1;
        double hasError;

        /* Loops until the perceptron no longer has any errors in an epoch */
        do {
            System.out.println("\nEpoch " + count + ":");

            /* Calls the epoch method to perform the iteration */
            double[] epoch = epoch(weight0, weight1, weight2, 0.1);

            /* Gets the last updated weights of the epoch */
            weight1 = epoch[0];
            weight2 = epoch[1];

            /* Gets if there were any errors in the epoch */
            hasError = epoch[2];
            count++;
        } while (hasError == 1);
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
     * @param learningRate  The learning rate for the perceptron
     * @return The updated weights and if there was an error in the epoch
     *
     * @see #iterate(double, double, double, double, int, int, int)
     */
    public double[] epoch(double weight0, double weight1, double weight2, double learningRate) {

        /* Calls the iterate method to perform the iteration over the 4 truth table values */
        Results first = iterate(weight0, weight1, weight2, learningRate, 0, 0, 0);
        Results second = iterate(weight0, first.newWeight1(), first.newWeight2(), learningRate, 0, 1, 0);
        Results third = iterate(weight0, second.newWeight1(), second.newWeight2(), learningRate, 1, 0, 0);
        Results fourth = iterate(weight0, third.newWeight1(), third.newWeight2(), learningRate, 1, 1, 1);

        /* Stores the results of the 4 iterations */
        Results[] results = new Results[]{first, second, third, fourth};

        /* Prints out the table for each of the epochs. */
        System.out.printf(" %-7s| %-7s| %-13s| %-18s| %-7s| %-15s| %-7s| %-15s| %-20s%n",
                "Sample", "Input", "Target (y)", "Weights (w1,w2)", "S", "Output (o)", "Error",
                "Delta (d1,d2)", "Updated Weights (w1,w2)");
        System.out.println("--------------------------------------------------------------------------------------" +
                "------------------------------------------");
        StringBuilder table = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            table.append(formatResults(i + 1, results[i])).append("\n");
        }
        System.out.println(table);

        /* Stores if there was an error or not, holds only 1.0 or 0.0 */
        double hasError = Math.abs(first.error()) == 1 || Math.abs(second.error()) == 1 ||
                Math.abs(third.error()) == 1 || Math.abs(fourth.error()) == 1  ? 1 : 0;

        /* Returns the new weight and if there was an error */
        return new double[]{fourth.newWeight1(), fourth.newWeight2(), hasError};
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
        return String.format(" %-7s| %-7s| %-13s| %-18s| %-7s| %-15s| %-7s| %-15s| %-20s",
                sample, // The Sample/Iteration Number
                result.input1() + "," + result.input2(), // The Input Values
                result.target(), // The Target Value
                formatVal.format(result.weight1()) + "," + formatVal.format(result.weight2()), // The Original Weights
                formatVal.format(result.s()), // The S Value
                result.output(), // The Output Value
                result.error(), // The Error Value
                formatVal.format(result.delta1()) + "," + formatVal.format(result.delta2()), // The Delta Values
                formatVal.format(result.newWeight1) + "," + formatVal.format(result.newWeight2)); // The Updated Weights
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
     * @param learningRate  The learning rate for the perceptron
     * @param input1        The first input value
     * @param input2        The second input value
     * @param target        The target value
     *
     * @return The results of the iteration
     * @see Results
     */
    public Results iterate(double weight0, double weight1, double weight2, double learningRate,
                            int input1, int input2, int target) {

        /* Handles the weights of the values */
        double s = (weight0 * 1) + weight1 * input1 + weight2 * input2;

        /* Handles the S function and calculates the error */
        int output = s > 0 ? 1 : 0;
        int error = target - output;

        /* Calculates the delta values */
        double delta1 = learningRate * (double) error * input1;
        double delta2 = learningRate * (double) error * input2;

        /* Calculates the new weights */
        double newWeight1 = weight1 + delta1;
        double newWeight2 = weight2 + delta2;

        /* Returns the results of the iteration */
        return new Results(learningRate, input1, input2, target, weight0, weight1, weight2, s, output, error,
                delta1, delta2, newWeight1, newWeight2);
    }

    /**
     * The Results class that holds the results of an iteration.
     */
    public record Results (double learningRate, int input1, int input2, int target, double weight0,
                            double weight1, double weight2, double s, int output, int error,
                            double delta1, double delta2, double newWeight1, double newWeight2) {}
}
