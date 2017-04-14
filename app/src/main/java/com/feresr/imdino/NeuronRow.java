package com.feresr.imdino;

import java.util.ArrayList;

/**
 * Created by feresr on 23/10/2016.
 */

public class NeuronRow {

    private ArrayList<Neuron> neurons = new ArrayList<>();

    public void addNeuron(Neuron neuron) {
        neurons.add(neuron);
    }

    public float[] process(float[] inputs) {
        float[] result = new float[neurons.size()];
        for (int i = 0; i < neurons.size(); i++) {
            result[i] = neurons.get(i).process(inputs);
        }

        return result;
    }

    public int getNumberOfSynapses() {
        return neurons.get(0).getNumberOfSynapses() * neurons.size();
    }
}
