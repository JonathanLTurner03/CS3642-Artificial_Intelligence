package Assignments.A3_Perceptron;

import java.text.DecimalFormat;

public class LogicOrPerceptron {

    public void beginTraining() {
        double weight0 = -0.2;
        double weight1 = 0.3;
        double weight2 = -0.1;
        int count = 1;
        double hasError = 1;
        while (hasError == 1) {
            System.out.println("\nEpoch " + count + ":");
            double[] epoch = epoch(weight0, weight1, weight2, 0.1);
            weight1 = epoch[0];
            weight2 = epoch[1];
            hasError = epoch[2];
            count++;
        }
    }

    public double[] epoch(double weight0, double weight1, double weight2, double learningRate) {

        Results first = iterate(weight0, weight1, weight2, learningRate, 0, 0, 0);
        Results second = iterate(weight0, first.newWeight1(), first.newWeight2(), learningRate, 0, 1, 0);
        Results third = iterate(weight0, second.newWeight1(), second.newWeight2(), learningRate, 1, 0, 0);
        Results fourth = iterate(weight0, third.newWeight1(), third.newWeight2(), learningRate, 1, 1, 1);

        Results[] results = new Results[]{first, second, third, fourth};

        System.out.println(String.format(" %-7s| %-7s| %-13s| %-18s| %-7s| %-15s| %-7s| %-15s| %-20s",
                "Sample", "Input", "Target (y)", "Weights (w1,w2)", "S", "Output (o)", "Error",
                "Delta (d1,d2)", "Updated Weights (w1,w2)"));
        System.out.println("--------------------------------------------------------------------------------------" +
                "------------------------------------------");
        DecimalFormat formatVal = new DecimalFormat("##.##");
        for (int i = 0; i < 4; i++) {
            System.out.println(String.format(" %-7s| %-7s| %-13s| %-18s| %-7s| %-15s| %-7s| %-15s| %-20s",
                    i+1, results[i].input1() + "," + results[i].input2(), results[i].target(),
                    formatVal.format(results[i].weight1()) + "," + formatVal.format(results[i].weight2()),
                    formatVal.format(results[i].s()), results[i].output(),
                    results[i].error(), formatVal.format(results[i].delta1()) + "," + formatVal.format(results[i].delta2()),
                    formatVal.format(results[i].newWeight1) + "," + formatVal.format(results[i].newWeight2)));
        }

        double hasError =
                Math.abs(first.error()) == 1 || Math.abs(second.error()) == 1 ||
                        Math.abs(third.error()) == 1 || Math.abs(fourth.error()) == 1  ? 1 : 0;

        return new double[]{fourth.newWeight1(), fourth.newWeight2(), hasError};
    }



    public Results iterate(double weight0, double weight1, double weight2, double learningRate,
                            int input1, int input2, int target) {

        double s = (weight0 * 1) + weight1 * input1 + weight2 * input2;
        int output = s > 0 ? 1 : 0;
        int error = target - output;

        double delta1 = learningRate * (double) error * input1;
        double delta2 = learningRate * (double) error * input2;

        double newWeight1 = weight1 + delta1;
        double newWeight2 = weight2 + delta2;

        return new Results(learningRate, input1, input2, target, weight0, weight1, weight2, s, output, error, delta1, delta2, newWeight1, newWeight2);
    }

    public record Results (double learningRate, int input1, int input2, int target, double weight0,
                            double weight1, double weight2, double s, int output, int error,
                            double delta1, double delta2, double newWeight1, double newWeight2) {}
}
