package Distribution;

import Contract.Distribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Source: https://stackoverflow.com/a/35701777/893222
 */
public class DistributionTableMethod<T> implements Distribution {

    static public Random rand = new Random();

    private double weightSum;
    private List<T> events = new ArrayList<>();
    private List<Double> weights = new ArrayList<>();

    public DistributionTableMethod(Map<T, Double> eventsWeights) {
        if(eventsWeights.isEmpty()) {
            throw new RuntimeException("Given empty map of events! Nothing to sample from.");
        }
        for(T event: eventsWeights.keySet()) {
            weightSum += eventsWeights.get(event);
            weights.add(eventsWeights.get(event));
            events.add(event);
        }
        if(!Double.isFinite(weightSum)) {
            throw new RuntimeException("Sum of event weights is not a finite double! WeightedSum: " + weightSum);
        }
    }

    public T sample() {

        double prob = rand.nextDouble() * weightSum;
        if(prob == 0) return events.get(rand.nextInt(events.size()));

        int i;
        for(i = 0; prob > 0; i++) {
            prob -= weights.get(i);
        }
        return events.get(i-1);

    }

}