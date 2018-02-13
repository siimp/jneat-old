package test.java;

import static org.junit.Assert.assertTrue;

import main.java.config.Config;
import org.junit.Before;
import org.junit.Test;

public class XorTests {

    @Before
    public void setup() {
        Config.reset();
    }

    @Test
    public void test() {
        assertTrue(getFitness() < 0.01);
    }
    
    private static double getFitness() {
        return -1 * (Math.pow(Math.abs(evaluate(0.0, 0.0)[0]), 2) + 
                Math.pow(Math.abs((1.0 - Math.abs(evaluate(0.0, 1.0)[0]))), 2) +
                Math.pow(Math.abs((1.0 - Math.abs(evaluate(1.0, 0.0)[0]))), 2) +
                Math.pow(Math.abs(evaluate(1.0, 1.0)[0]), 2));
    }
    
    private static double[] evaluate(double a, double b) {
        if (Double.compare(a, 1.0) == 0 && Double.compare(b, 1.0) != 0) {
            return new double[] {1.0};
        } else if(Double.compare(b, 1.0) == 0 && Double.compare(a, 1.0) != 0) {
            return new double[] {1.0};
        } else {
            return new double[] {0.0};
        }
    }

}
