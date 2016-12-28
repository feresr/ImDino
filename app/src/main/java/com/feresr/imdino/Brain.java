package com.feresr.imdino;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

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

    float shouldJump(float[] inputs) {
        float[] result = inputs;

        for (int i = 0; i < neuronRows.size(); i ++) {
            result = neuronRows.get(i).process(result);
        }

        Log.e("Brain output", Arrays.toString(result));
        return result[0];
    }
}
