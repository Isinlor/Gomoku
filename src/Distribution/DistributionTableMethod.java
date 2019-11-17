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

    private Random rand = new Random();

    private double weightSum;
    private List<T> events = new ArrayList<>();
    private List<Double> weights = new ArrayList<>();

    public DistributionTableMethod(Map<T, Double> eventsWeights) {
        for(T event: eventsWeights.keySet()) {
            weightSum += eventsWeights.get(event);
            weights.add(eventsWeights.get(event));
            events.add(event);
        }
    }

    public T sample() {
        double prob = rand.nextDouble() * weightSum;
        int i;
        for(i=0; prob > 0; i++){
            prob -= weights.get(i);
        }
        return events.get(i-1);
    }

}