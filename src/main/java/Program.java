package main.java;

import main.java.ga.Individual;
import main.java.ga.NeatGeneticAlgorithm;
import main.java.ga.NeatGeneticAlgorithmBuilder;

public class Program {

    public static void main(String[] args) {
        //Ülesanne Population.generateRandomIndividual bias node tegemine
        
        NeatGeneticAlgorithm geneticAlgorithm = new NeatGeneticAlgorithmBuilder()
            .withFitnessFunction(Program::xor)
            .withTerminationFunction(fitnessValue -> Math.abs(fitnessValue) < 0.0001)
            .withFitnessCompareFunction(Double::compare)
            .withInputs(2)
            .withOutputs(1)
            .withBiasInputValue(1.0)
            .build();

        System.out.println("XOR:");
        System.out.println(geneticAlgorithm.solve());

        geneticAlgorithm = new NeatGeneticAlgorithmBuilder()
                .withFitnessFunction(Program::or)
                .withTerminationFunction(fitnessValue -> Math.abs(fitnessValue) < 0.0001)
                .withFitnessCompareFunction(Double::compare)
                .withInputs(2)
                .withOutputs(1)
                .withBiasInputValue(1.0)
                .build();

        System.out.println("OR:");
        System.out.println(geneticAlgorithm.solve());

    }
    
    private static double xor(Individual individual) {
        return -(Math.pow(individual.evaluate(0.0, 0.0)[0], 2) +
                Math.pow((1.0 - Math.abs(individual.evaluate(0.0, 1.0)[0])), 2) +
                Math.pow((1.0 - Math.abs(individual.evaluate(1.0, 0.0)[0])), 2) +
                Math.pow(individual.evaluate(1.0, 1.0)[0], 2));
    }

    private static double or(Individual individual) {
        return -(Math.pow(individual.evaluate(0.0, 0.0)[0], 2) +
                Math.pow((1.0 - Math.abs(individual.evaluate(0.0, 1.0)[0])), 2) +
                Math.pow((1.0 - Math.abs(individual.evaluate(1.0, 0.0)[0])), 2) +
                Math.pow((1.0 - Math.abs(individual.evaluate(1.0, 0.0)[0])), 2));
    }

}
