package com.feresr.imdino;

import java.util.ArrayList;

/**
 * Created by feresr on 23/10/2016.
 */

public class Brain {

    private ArrayList<NeuronRow> neuronRows = new ArrayList<>();

    Brain() {
        super();
    }

    void addNeuronRow(NeuronRow neuronRow) {
        neuronRows.add(neuronRow);
    }

    int getNumberOfSynapses() {
        int synapses = 0;
        for (int i = 0; i < neuronRows.size(); i++) {
            synapses += neuronRows.get(i).getNumberOfSynapses();
        }

        return synapses;
    }

    float shouldJump(float[] inputs) {
        float[] result = inputs;

        for (int i = 0; i < neuronRows.size(); i++) {
            result = neuronRows.get(i).process(result);
        }
        return result[0];
    }
}
