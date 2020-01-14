package NeuralNetwork;

import java.util.ArrayList;
import java.util.Arrays;

public class Dataset {
    private ArrayList<int[][][]> states;
    private ArrayList<Integer> values;
    private ArrayList<float[]> policies;

    public Dataset() {
        states = new ArrayList<>();
        values = new ArrayList<>();
        policies = new ArrayList<>();
    }

    public void add(int[][][] state, int value){
        float[] uniformPolicy = new float[state.length];
        Arrays.fill(uniformPolicy, 1/uniformPolicy.length);
        this.add(state, value, new float[state.length]);
    }

    public void add(int[][][] state, int value, float[] policy){
        states.add(state);
        values.add(value);
        policies.add(policy);
    }

    public Dataset(ArrayList<int[][][]> states, ArrayList<Integer> values, ArrayList<float[]> policies) {
        this.states = states;
        this.values = values;
        this.policies = policies;
    }
}
