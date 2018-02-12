package main.java.ga;

@FunctionalInterface
public interface TerminationFunction {
    
    boolean terminate(double fitnessValue);

}
