package Assignments.A3_Perceptron;

import java.text.NumberFormat;

// Temp file to create the Perceptron package.
public class Driver {

    /* TODO: Add the sentinel Loop */
    public static void main (String[] args) {
//        LogicOrPerceptron perceptron = new LogicOrPerceptron();
        BrightPerceptron perceptron = new BrightPerceptron();
        perceptron.beginTraining();
    }

}
