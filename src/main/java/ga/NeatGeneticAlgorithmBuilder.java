package main.java.ga;

public class NeatGeneticAlgorithmBuilder {

    private NeatGeneticAlgorithm neatGeneticAlgorithm;

    private NeatGeneticAlgorithm getNeatGeneticAlgorithm() {
        if (neatGeneticAlgorithm == null) {
            neatGeneticAlgorithm = new NeatGeneticAlgorithm();
        }
        return neatGeneticAlgorithm;
    }

    public NeatGeneticAlgorithm build() {
        getNeatGeneticAlgorithm().initialize();
        return neatGeneticAlgorithm;
    }

    public NeatGeneticAlgorithmBuilder withFitnessFunction(FitnessFunction fitnessFunction) {
        getNeatGeneticAlgorithm().setFitnessFunction(fitnessFunction);
        return this;
    }

    public NeatGeneticAlgorithmBuilder withTerminationFunction(TerminationFunction terminationFunction) {
        getNeatGeneticAlgorithm().setTerminationFunction(terminationFunction);
        return this;
    }

    public NeatGeneticAlgorithmBuilder withFitnessCompareFunction(FitnessCompareFunction fitnessCompareFunction) {
        getNeatGeneticAlgorithm().setFitnessCompareFunction(fitnessCompareFunction);
        return this;
    }

    public NeatGeneticAlgorithmBuilder withInputs(int intputNeuronCount) {
        getNeatGeneticAlgorithm().setInputNeuronCount(intputNeuronCount);
        return this;
    }

    public NeatGeneticAlgorithmBuilder withOutputs(int outputNeuronCount) {
        getNeatGeneticAlgorithm().setOutputNeuronCount(outputNeuronCount);
        return this;
    }

    public NeatGeneticAlgorithmBuilder withBiasInputValue(double biasNodeInputValue) {
        getNeatGeneticAlgorithm().setBiasNodeInputValue(biasNodeInputValue);
        return this;
    }

}
