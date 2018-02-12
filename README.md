# NEAT - Evolving Neural Networks through Augmenting Topologies

## Materials used
* http://nn.cs.utexas.edu/downloads/papers/stanley.ec02.pdf
* http://it-ebooks.info/book/6588/
* http://it-ebooks.info/book/6808/
* https://en.wikipedia.org/wiki/Artificial_neuron
* https://en.wikipedia.org/wiki/Synaptic_weight

## Backlog
* usage sample

```
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
```
