package Assignments.A3_Perceptron;

import java.text.DecimalFormat;

public class LogicOrPerceptron {

    /* TODO: Add the JavaDoc */
    public void beginTraining() {
        /* Initial weights TODO: accept these values as a parameter */
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

    /* TODO: Add the JavaDoc */
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

    /* TODO: Add the JavaDoc */
    private String formatResults(int sample, Results result) {
        DecimalFormat formatVal = new DecimalFormat("##.##");
        return String.format(" %-7s| %-7s| %-13s| %-18s| %-7s| %-15s| %-7s| %-15s| %-20s",
                sample, result.input1() + "," + result.input2(), result.target(),
                formatVal.format(result.weight1()) + "," + formatVal.format(result.weight2()),
                formatVal.format(result.s()), result.output(),
                result.error(), formatVal.format(result.delta1()) + "," + formatVal.format(result.delta2()),
                formatVal.format(result.newWeight1) + "," + formatVal.format(result.newWeight2));
    }

    /* TODO: Add the JavaDoc */
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

    /* TODO: Add the JavaDoc */
    public record Results (double learningRate, int input1, int input2, int target, double weight0,
                            double weight1, double weight2, double s, int output, int error,
                            double delta1, double delta2, double newWeight1, double newWeight2) {}
}
