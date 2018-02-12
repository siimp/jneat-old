package main.java.genetics;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class GenePool {

    private AtomicLong innovation = new AtomicLong(1);
    private Map<Connection, Long> innovationHistory = new HashMap<>();

    public NeuronGene getNewNeuronInputGene() {
        NeuronGene neuronGene = new NeuronGene();
        neuronGene.setInput(true);
        neuronGene.setInnovation(getNextInnovationNumber());
        return neuronGene;
    }
    
    public NeuronGene getNewNeuronBiasGene(Double biasValue) {
        NeuronGene neuronGene = new NeuronGene();
        neuronGene.setInput(true);
        neuronGene.setBiasValue(biasValue);
        neuronGene.setInnovation(getNextInnovationNumber());
        return neuronGene;
    }

    public NeuronGene getNewNeuronOutputGene() {
        NeuronGene neuronGene = new NeuronGene();
        neuronGene.setOutput(true);
        neuronGene.setInnovation(getNextInnovationNumber());
        return neuronGene;
    }

    public NeuronGene getNewNeuronGene() {
        NeuronGene neuronGene = new NeuronGene();
        neuronGene.setInnovation(getNextInnovationNumber());
        return neuronGene;
    }

    /**
     * However, by keeping a list of the innovations that occurred in the
     * current generation, it is possible to ensure that when the same structure
     * arises more than once through independent mutations in the same
     * generation, each identical mutation is assigned the same innovation
     * number. Thus, there is no resultant explosion of innovation numbers
     */

    public DendriteGene getNewDendtriteGene(NeuronGene from, NeuronGene to) {
        DendriteGene dendriteGene = new DendriteGene();
        dendriteGene.setFrom(from);
        dendriteGene.setTo(to);

        Connection connection = Connection.of(from.getInnovation(), to.getInnovation());
        Long existingConnectionInnovation = innovationHistory.get(connection);
        if (existingConnectionInnovation != null) {
            dendriteGene.setInnovation(existingConnectionInnovation.longValue());
        } else {
            dendriteGene.setInnovation(getNextInnovationNumber());
            innovationHistory.put(connection, Long.valueOf(dendriteGene.getInnovation()));
        }
        return dendriteGene;
    }

    private long getNextInnovationNumber() {
        return innovation.getAndIncrement();
    }



//    public DendriteGene getNewRandomWeightDendtriteGene(NeuronGene from, NeuronGene to) {
//        DendriteGene result = getNewDendtriteGene(from, to);
//        result.randomize
//        return result;
//    }

    // public static void reset() {
    // innovation.set(1);
    // innovationHistory.clear();
    // }

}

class Connection {
    private final Long from;
    private final Long to;

    public Connection(long from, long to) {
        super();
        this.from = Long.valueOf(from);
        this.to = Long.valueOf(to);
    }

    public Long getFrom() {
        return from;
    }

    public static Connection of(long from, long to) {
        return new Connection(from, to);
    }

    public Long getTo() {
        return to;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Connection other = (Connection) obj;
        return Objects.equals(getFrom(), other.getFrom()) && Objects.equals(getTo(), other.getTo());
    }

}
