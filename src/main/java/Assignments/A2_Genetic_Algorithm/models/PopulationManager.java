package Assignments.A2_Genetic_Algorithm.models;

import java.util.*;

/**
 * A class to manage the population of individuals and performs Genetic Algorithm operations and functions.
 *
 * @author Jonathan Turner
 * @version Spring 2024
 */
public class PopulationManager {

    public static double p_m = 0.0;
    public static int n_pop = 0;

    /**
     * Performs the Genetic Algorithm (GA) using a pre-provided population and a mutation percentage
     * and size of population. It first finds the Elite individuals, crossover's their values,
     * and mutates them. Lastly it adds the values back to the population and checks if the
     * desired value is presents.
     *
     * @precondition p_m != 0.0 && n_pop != 0
     * @postcondition none
     *
     * @param population the population being operated on
     * @return the number of generations.
     */
    public static int evolution(BinaryVector[] population) {
        if (p_m == 0.0 || n_pop == 0) {
            throw new IllegalArgumentException("the population size and mutation probability must be set before running.");
        }

        int ancestryCount = 0;
        BinaryVector[] generation = population;

        /* Begins loop for Genetic Algorithm */
        while (!check(generation)) {

            /* Performs an Elite Selection to choose the ancestors for the next generation */
            generation = selection(generation);
            List<BinaryVector> offspring = new ArrayList<>();
            for (int parents = 0; parents < generation.length-1; parents+=2) {
                BinaryVector[] directDescendants = crossover(generation[parents], generation[parents+1]);
                offspring.addAll(List.of(directDescendants));
            }

            /* Used when a list is even and the split is on an odd number. */
            if ((n_pop/2) % 2 == 1) {
                int secondToLast = generation.length-2;
                offspring.add(crossover(generation[secondToLast], generation[secondToLast+1])[0]);
            }

            /* Adds the values of the next Elite and their offspring to the next generation */
            BinaryVector[] nextGen = new BinaryVector[n_pop];
            for (int i = 0; i < generation.length; i++) {
                /* Mutates the BinaryVector when added to next generation */
                nextGen[i] = mutation(generation[i], p_m);
            }

            /* Uses both the index and continued index (from the previous loop) to add the offspring. */
            for (int i = 0, continued_index = generation.length; i < offspring.size(); i++, continued_index++) {
                /* Mutates the BinaryVector when added to next generation */
                nextGen[continued_index] = mutation(offspring.get(i), p_m);
            }

            generation = nextGen; // Progresses the generation to the next generation.
            ancestryCount++; // Increments the number of generations
        }
        return ancestryCount;
    }

    /**
     * Checks if there is an individual in the population that has a fitness of 0 (wanted).
     *
     * @precondition none
     * @postcondition none
     *
     * @param population the population being checked
     * @return if there is an individual with fitness 0.
     */
    public static boolean check(BinaryVector[] population) {
        for (BinaryVector binaryVector : population) {
            if (binaryVector != null && fitness(binaryVector) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create a new population of n_pop individuals with random genes.
     *
     * @precondition n_pop > 0
     * @postcondition none
     *
     * @param n_pop the number of individuals in the population.
     * @return a population is returned with n_pop individuals
     */
    public static BinaryVector[] generate_population(int n_pop) {
        /* Check if the population size is positive */
        if (n_pop < 1) {
            throw new IllegalArgumentException("Population size must be positive and even.");
        }

        /* Initialize and creates the population of n_pop individuals with random genes */
        BinaryVector[] population = new BinaryVector[n_pop];
        for (int i = 0; i < n_pop; i++) {
            population[i] = new BinaryVector();
        }
        return population;
    }

    /**
     * Returns the list of elite selected individuals from the population.
     *
     * @precondition population != null
     * @postcondition none
     *
     * @param population the current population.
     * @return the elite selection population.
     */
    public static BinaryVector[] selection(BinaryVector[] population) {
        if (population == null) {
            throw new IllegalArgumentException("The population must be valid and even.");
        }

        /* Turns the order of the population to a list and sorts it based on the default order in BinaryVector */
        List<BinaryVector> unorderedPop = new ArrayList<>(List.of(population));
        Collections.sort(unorderedPop);

        /* Sets the length of the array based on if its odd or even. */
        BinaryVector[] selected;
        if ((population.length) % 2 == 1) {
            selected = new BinaryVector[(population.length + 1) / 2];
        } else {
            selected = new BinaryVector[population.length / 2];
        }

        /* Selects the elite elements from the start of the ordered list and places them into the selected array */
        for (int i = 0; i < selected.length; i++) {
            selected[i] = unorderedPop.get(i);
        }
        return selected;
    }

    /**
     * Calculate the fitness of an BinaryVector based on the genes' value in binary form
     * and powers it by 2.
     *
     * @precondition individual != null
     * @postcondition none
     *
     * @param individual the BinaryVector to calculate the fitness
     * @return the fitness of the individual
     */
    public static int fitness(BinaryVector individual) {
        if (individual == null) {
            throw new IllegalArgumentException("The individual cannot be null.");
        }

        /* Calculate the decimal value of the binary genes */
        int decimal = 0;
        for (int i = individual.genes.length-1; i >= 0; i--) {
            if (individual.genes[i] == 1) {
                /* Add the value of the gene to the decimal value based on its position */
                decimal += (int) Math.pow(2, (individual.genes.length-1)-i);
            }
        }

        /* Return the fitness of the individual, which is f(x) = x^2. */
        return (int) Math.pow(decimal, 2);
    }

    /**
     * Mutates the genes of a BinaryVector with a probability p_m.
     *
     * @precondition individual != null && p_m >= 0 && p_m <= 1
     * @postcondition none
     *
     * @param individual the BinaryVector to mutate
     * @param p_m the probability of mutation
     * @return the mutated BinaryVector
     */
    public static BinaryVector mutation(BinaryVector individual, double p_m) {
        /* Does the pre- and post-condition checks */
        if (individual == null) {
            throw new IllegalArgumentException("Individual cannot be null.");
        }
        if (p_m < 0 || p_m > 1) {
            throw new IllegalArgumentException("Mutation probability must be between 0 and 1.");
        }

        /* Creates the mutated binaryvector as a copy of the original */
        Random generator = new Random();
        BinaryVector mutated = new BinaryVector(individual.genes);

        /* Mutate the genes of the individual with a probability p_m by generating a random double between 0 and 1. */
        for (int i = 0; i < individual.genes.length; i++) {
            if (generator.nextDouble(0, 1) < p_m) {
                /* flips the gene from 0 to 1 or vise versa */
                if (mutated.genes[i] == 0) {
                    mutated.genes[i] = 1;
                } else {
                    mutated.genes[i] = 0;
                }
            }
        }
        return mutated;
    }

    /**
     * Breeds the two parent individuals to two new child vectors.
     *
     * @precondition firstParent != null && secondParent != null
     * @postcondition none
     *
     * @param firstParent the first parent individual
     * @param secondParent the second parent individual
     *
     * @return an array of two new BinaryVectors that are the children of the parents.
     */
    public static BinaryVector[] crossover(BinaryVector firstParent, BinaryVector secondParent) {
        /* Check if the parents are null */
        if (firstParent == null || secondParent == null) {
            throw new IllegalArgumentException("Parents cannot be null.");
        }

        /* Two int arrays to hold the genes of the children */
        int[] firstChild = new int[6];
        int[] secondChild = new int[6];

        /* Randomly select a crossover index */
        Random generator = new Random();
        int crossover_point = generator.nextInt(0, 6);

        /* Copy the genes of the same side parents to the same side child */
        for (int i = 0; i < crossover_point; i++) {
            firstChild[i] = firstParent.genes[i];
            secondChild[i] = secondParent.genes[i];
        }

        /* Copy the genes of the opposite side parents to the opposite side child */
        for (int i = crossover_point; i < 6; i++) {
            firstChild[i] = secondParent.genes[i];
            secondChild[i] = firstParent.genes[i];
        }

        /* Returns the two new values as BinaryVectors */
        return new BinaryVector[] {
                new BinaryVector(firstChild),
                new BinaryVector(secondChild)
        };
    }
}
