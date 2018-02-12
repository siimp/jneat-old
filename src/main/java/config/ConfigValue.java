package main.java.config;

/**
 * @author siim
 *
 */
public enum ConfigValue {

    WEIGHTS_ABSOLUTE_RANGE,
    WEIGHTS_PERTURB_MAX_AMOUNT,
    WEIGHTS_MUTATION_PROBABILITY,
    RANDOM_WEIGHT_PROBABILITY,
    PERTURB_WEIGHT_PROBABILITY,
    ACTIVATION_FUNCTION,
    POPULATION_SIZE, 
    TIME_LIMIT_IN_SECONDS, 
    CROSSOVER_PROBABILITY,
    MUTATION_PROBABILITY, 
    NEW_CONNECTION_PROBABILITY,
    NEW_NODE_PROBABILITY,
    SIGMOID_MULTIPLIER;

    @Override
    public String toString() {
        return toCamelCase(this.name());
    }

    static String toCamelCase(String s) {
        String[] parts = s.split("_");
        String camelCaseString = parts[0].toLowerCase();

        for (int i = 1; i < parts.length; i++) {
            camelCaseString = camelCaseString + toProperCase(parts[i]);
        }
        return camelCaseString;
    }

    static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

}
